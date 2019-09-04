package brian.scheduler.app.producer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import brian.scheduler.comm.event.ExecuteJobEvent;

/**
 * Event producer which sends {@link ExecuteJobEvent}s to a Redis Stream. The
 * Redis Stream is consumed by the job-agent services which executes the linux
 * commands on the host they are running on.
 */
@Component
public class RedisJobEventProducer implements JobEventProducer<ExecuteJobEvent> {

	private static final String REDIS_JOB_STREAM_NAME = "JOB_STREAM";
	private final RedisTemplate<String, ExecuteJobEvent> redisTemplate;
	
	/**
	 * @param redisTemplateIn the Redis template to write the events to a stream 
	 */
	public RedisJobEventProducer(final RedisTemplate<String, ExecuteJobEvent> redisTemplateIn) {
		
		redisTemplate = redisTemplateIn;
	}
	
	/**
	 * Writes each of the job events to a Redis Stream. The job-agent services
	 * consume events added to the stream and execute the commands on that host.
	 */
	@Override
	public void send(final List<ExecuteJobEvent> jobEvents) throws Exception {
		
		Map<Object, Object> jobsForStream = new LinkedHashMap<>();
		for(ExecuteJobEvent jobEvent : jobEvents) {
			jobsForStream.put(jobEvent.getId(), jobEvent);
		}
		redisTemplate.opsForStream().add(REDIS_JOB_STREAM_NAME, jobsForStream);
	}
	
}
