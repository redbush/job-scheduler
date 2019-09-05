package brian.scheduler.app.producer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import brian.scheduler.app.config.RedisProperties;
import brian.scheduler.comm.event.ExecuteJobEvent;

/**
 * Event producer which sends {@link ExecuteJobEvent}s to a Redis Stream. The
 * Redis Stream is consumed by the job-agent services which executes the linux
 * commands on the host they are running on.
 */
@Component
public class RedisJobEventProducer implements JobEventProducer<ExecuteJobEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisJobEventProducer.class);
	
	private final RedisTemplate<String, ExecuteJobEvent> redisTemplate;
	private final RedisProperties redisProperties;
	
	/**
	 * @param redisTemplateIn the Redis template to write the events to a stream 
	 * @param redisPropertiesIn the properties containing the job event stream key
	 */
	public RedisJobEventProducer(
			final RedisTemplate<String, ExecuteJobEvent> redisTemplateIn,
			final RedisProperties redisPropertiesIn) {
		
		redisTemplate = redisTemplateIn;
		redisProperties = redisPropertiesIn;
	}
	
	/**
	 * Writes each of the job events to a Redis Stream. The job-agent services
	 * consume events added to the stream and execute the commands on that host.
	 */
	@Override
	public void send(final List<ExecuteJobEvent> jobEvents) throws Exception {
		
		Map<Object, Object> jobsForStream = new LinkedHashMap<>();
		for(ExecuteJobEvent jobEvent : jobEvents) {
			String jobId = jobEvent.getId();
			LOGGER.debug("Mapping event for Redis Stream. ID: {}", jobId);
			jobsForStream.put(jobId, jobEvent);
		}
		redisTemplate.opsForStream().add(redisProperties.getJobEventStreamKey(), jobsForStream);
	}
	
}
