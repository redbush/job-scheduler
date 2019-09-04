package brian.scheduler.app.service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import brian.scheduler.comm.event.ExecuteJobEvent;

@Component
public class JobConsumer implements AutoCloseable, Callable<Void> {

	private final RedisTemplate<String, ExecuteJobEvent> template;
	private final ExecutorService schedulerPool;
	
	public JobConsumer(
			final RedisTemplate<String, ExecuteJobEvent> templateIn) {

		template = templateIn;
		schedulerPool = Executors.newSingleThreadExecutor();
		schedulerPool.submit(this);
	}
	
	@Override
	public void close() throws Exception {
		schedulerPool.shutdownNow();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Void call() throws Exception {
		
		while(true) {
			
			try {
				StreamOffset<String> offset = StreamOffset.create("JOB_STREAM", ReadOffset.latest());
				List<MapRecord<String,Object,Object>> jobs = template.opsForStream().read(StreamReadOptions.empty().block(Duration.ofSeconds(1)), offset);
				jobs
					.stream()
					.forEach(mapRecord -> {
						mapRecord.getValue().values().stream().forEach(job -> {
							ExecuteJobEvent j = (ExecuteJobEvent) job;
							System.out.println("Stream job id: " + j.getId());
						});
					});
			} catch(Exception e) {
				System.out.println("consumer error: " + e.getClass());
				e.printStackTrace();
				throw e;
			}
			
			Thread.sleep(1000);
		}
	}

}
