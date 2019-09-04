package brian.scheduler.app.repository;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import brian.scheduler.app.domain.Job;

@Component
public class RedisStreamingRepository implements JobRepository, AutoCloseable {

	private static final String REDIS_JOBS_CACHE_KEY = "JOBS";
	
	private final RedisTemplate<String, Job> redisTemplate;
	private final BlockingQueue<List<Job>> jobExecutionQueue = new LinkedBlockingQueue<>();
	private final ExecutorService threadPool;
	
	public RedisStreamingRepository(final RedisTemplate<String, Job> redisTemplateIn) {
		redisTemplate = redisTemplateIn;
		threadPool = Executors.newSingleThreadExecutor();
		threadPool.submit(new JobQueryDaemon(jobExecutionQueue, redisTemplate));
	}
	
	@Override
	public void add(final Job job) throws Exception {
		redisTemplate.opsForZSet().add(REDIS_JOBS_CACHE_KEY, job, ScoreUtil.score(job));
	}

	@Override
	public List<Job> read() throws InterruptedException {
		return jobExecutionQueue.poll(1, TimeUnit.MINUTES); // verify impact on caller
	}
	
	static class JobQueryDaemon implements Callable<Void>  {
		
		private final BlockingQueue<List<Job>> jobExecutionQueue;
		private final RedisTemplate<String, Job> redisTemplate;
		
		public JobQueryDaemon(
				final BlockingQueue<List<Job>> jobExecutionQueueIn,
				final RedisTemplate<String, Job> redisTemplateIn) {
			
			jobExecutionQueue = jobExecutionQueueIn;
			redisTemplate = redisTemplateIn;
		}

		@Override
		public Void call() throws Exception {
			
			while(true) {
				try {
					
					Set<TypedTuple<Job>> jobs = redisTemplate.opsForZSet().rangeByScoreWithScores(REDIS_JOBS_CACHE_KEY, 0, Long.valueOf(System.nanoTime()).doubleValue());
					if(!jobs.isEmpty()) {
						Set<TypedTuple<Job>> updatedJobs = new LinkedHashSet<>();
						List<Job> jobsForRead = new LinkedList<>();
						jobs.forEach(jobTuple -> {
							
							Job job = jobTuple.getValue();
							double newScore = ScoreUtil.score(job);
							System.out.println("Setting new score - ID: " + job.getId() + ", OldScore: " + jobTuple.getScore() + ", NewScore: " + newScore);
							updatedJobs.add(new DefaultTypedTuple<>(job, newScore));
							jobsForRead.add(job);
						});
						redisTemplate.opsForZSet().add(REDIS_JOBS_CACHE_KEY, updatedJobs);
						jobExecutionQueue.add(jobsForRead);
					}

				} catch(Exception e) {
					e.printStackTrace(); // WHAT CAN BE THROWN
				}
				
				Thread.sleep(1000);
			}
		}
		
	}

	@Override
	public void close() throws Exception {
		threadPool.shutdownNow();
	}
	
	static class ScoreUtil {
		
		private ScoreUtil() {}
		
		public static double score(final Job job) {
			// Overflow?
			return Long.valueOf(System.nanoTime() + TimeUnit.valueOf(job.getTimeUnit()).toNanos(job.getInterval())).doubleValue();
		}
	}

}
