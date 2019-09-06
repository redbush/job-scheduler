package brian.scheduler.agent.consumer;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import brian.scheduler.agent.command.CommandExecutor;
import brian.scheduler.agent.config.RedisProperties;
import brian.scheduler.agent.domain.JobCommand;
import brian.scheduler.comm.event.ExecuteJobEvent;

@Component
public class JobStreamConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobStreamConsumer.class);
	
	private final RedisTemplate<String, ExecuteJobEvent> redisTemplate;
	private final RedisProperties redisProperties;
	private final CommandExecutor commandExecutor;
	
	public JobStreamConsumer(
			final RedisTemplate<String, ExecuteJobEvent> redisTemplateIn,
			final RedisProperties redisPropertiesIn,
			final CommandExecutor commandExecutorIn) {
		
		redisTemplate = redisTemplateIn;
		redisProperties = redisPropertiesIn;
		commandExecutor = commandExecutorIn;
	}
	
	@Scheduled(fixedRate = 1000)
	@SuppressWarnings("unchecked")
	public void run() {
		
		try {
			StreamOffset<String> offset = StreamOffset.create(redisProperties.getJobEventStreamKey(), ReadOffset.latest());
			List<MapRecord<String,Object,Object>> eventRecords = redisTemplate.opsForStream().read(StreamReadOptions.empty().block(Duration.ofMinutes(1)), offset);
			LOGGER.debug("{} events read from stream", eventRecords.size());
			eventRecords
				.stream()
				.forEach(eventRecord -> {
					eventRecord.getValue().values().stream().forEach(jobEventObj -> {
						ExecuteJobEvent jobEvent = (ExecuteJobEvent) jobEventObj;
						try {
							commandExecutor.execute(buildCommand(jobEvent));
						} catch (Exception e) {
							LOGGER.error("An error occured executing job ID: {}", jobEvent.getId(), e);
						}
					});
				});
		} catch(QueryTimeoutException doNothing) {}
	}

	private JobCommand buildCommand(final ExecuteJobEvent jobEvent) {
		
		return JobCommand
			.builder()
			.withId(jobEvent.getId())
			.withCommand(jobEvent.getCommand())
			.build();
	}
	
}
