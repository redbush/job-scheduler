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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import brian.scheduler.app.domain.Job;

/**
 * Redis backed Job repository. Jobs are added to a Redis sorted set. The Jobs
 * are ordered by a score which is the next time the Job should execute. The
 * {@link JobQueryDaemon} runs every second and finds Jobs who have a score less
 * than or equal to the current time. Jobs that are ready to be executed are
 * written to a blocking queue which the {@link RedisJobRepository#read()} will
 * poll when called.
 */
@Component
public class RedisJobRepository implements StreamingJobRepository, AutoCloseable {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisJobRepository.class);
	private static final String REDIS_JOBS_CACHE_KEY = "JOBS";
	
	private final RedisTemplate<String, Job> redisTemplate;
	private final BlockingQueue<List<Job>> jobExecutionQueue = new LinkedBlockingQueue<>();
	private final ExecutorService threadPool;
	
	/**
	 * This constructor sets the RedisTemplate and also initializes a single
	 * threaded thread pool to run the JobQueryDaemon in.
	 * 
	 * @param redisTemplateIn the template used to add Jobs to Redis and find Jobs
	 *                        to execute
	 */
	public RedisJobRepository(final RedisTemplate<String, Job> redisTemplateIn) {
		
		redisTemplate = redisTemplateIn;
		threadPool = Executors.newSingleThreadExecutor();
		threadPool.submit(new JobQueryDaemon(jobExecutionQueue, redisTemplate));
	}
	
	/**
	 * Calculates the score(when the job should execute) for a Job and adds it to a Redis sorted set
	 */
	@Override
	public void add(final Job job) {
		redisTemplate.opsForZSet().add(REDIS_JOBS_CACHE_KEY, job, ScoreUtil.score(job));
	}

	/**
	 * Polls a blocking Job queue for Jobs to execute
	 * 
	 * @return the list of jobs to execute or null if the polling timeout of 1 minute is reached
	 */
	@Override
	public List<Job> read() throws InterruptedException {
		return jobExecutionQueue.poll(1, TimeUnit.MINUTES); 
	}

	/**
	 * A daemon that queries Redis to find jobs that are ready to execute and writes
	 * the Jobs to a blocking queue which the {@link RedisJobRepository#read()}
	 * method will poll. Jobs that are found will have their score(next execution
	 * time) recalculated and the Jobs will be updated in the sorted Redis set.
	 */
	static class JobQueryDaemon implements Callable<Void>  {
		
		private static final int QUERY_DELAY = 1000;
		private final BlockingQueue<List<Job>> jobExecutionQueue;
		private final RedisTemplate<String, Job> redisTemplate;
		
		/**
		 * @param jobExecutionQueueIn the blocking queue to write to which is read to find jobs to execute
		 * @param redisTemplateIn the template to find and update Jobs
		 */
		public JobQueryDaemon(
				final BlockingQueue<List<Job>> jobExecutionQueueIn,
				final RedisTemplate<String, Job> redisTemplateIn) {
			
			jobExecutionQueue = jobExecutionQueueIn;
			redisTemplate = redisTemplateIn;
		}

		/**
		 * Queries Redis for jobs that have a score less than or equal to the current
		 * system time. Jobs found are written to a blocking queue so the can be read.
		 * Jobs found have their score recalculated and are update in the sorted Redis
		 * set. This is called once a second.
		 */
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
							LOGGER.debug("Setting new score - ID: {}, OldScore: {}, NewScore: {}", job.getId(), jobTuple.getScore(), newScore);
							updatedJobs.add(new DefaultTypedTuple<>(job, newScore));
							jobsForRead.add(job);
						});
						redisTemplate.opsForZSet().add(REDIS_JOBS_CACHE_KEY, updatedJobs);
						jobExecutionQueue.add(jobsForRead);
					}
				} catch(Exception e) {
					e.printStackTrace(); // WHAT CAN BE THROWN
				}
				
				Thread.sleep(QUERY_DELAY);
			}
		}
		
	}

	/**
	 * Shuts down the thread pool executing the {@link JobQueryDaemon}
	 */
	@Override
	public void close() throws Exception {
		threadPool.shutdownNow();
	}
	
	/**
	 * Utility to calculate the score (next execution time) for a Job
	 */
	static class ScoreUtil {
		
		private ScoreUtil() {}
		
		/**
		 * Calculates the Job's score. The score is the Job's next execution time and is
		 * the sort key for a sorted Redis set
		 * 
		 * @param job the job to calculate the score for
		 * @return the score
		 */
		public static double score(final Job job) {
			// Overflow?
			return Long.valueOf(System.nanoTime() + job.getTimeUnit().toNanos(job.getInterval())).doubleValue();
		}
	}

}
