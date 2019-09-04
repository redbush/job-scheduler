package brian.scheduler.app.producer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import brian.scheduler.comm.event.ExecuteJobEvent;

@Component
public class RedisJobEventProducer implements JobEventProducer<ExecuteJobEvent> {

	private final RedisTemplate<String, ExecuteJobEvent> redisTemplate;
	
	public RedisJobEventProducer(
			final RedisTemplate<String, ExecuteJobEvent> redisTemplateIn,
			final JmsTemplate jmsTemplateIn) {
		
		redisTemplate = redisTemplateIn;
	}
	
	@Override
	public void send(final List<ExecuteJobEvent> jobEvents) throws Exception {
		
		Map<Object, Object> jobsForStream = new LinkedHashMap<>();
		for(ExecuteJobEvent jobEvent : jobEvents) {
			jobsForStream.put(jobEvent.getId(), jobEvent);
		}
		redisTemplate.opsForStream().add("JOB_STREAM", jobsForStream);
	}
	
}
