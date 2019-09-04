package brian.scheduler.comm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import brian.scheduler.comm.event.ExecuteJobEvent;
import brian.scheduler.comm.event.ExecuteJobEventDeserializer;
import brian.scheduler.comm.event.ExecuteJobEventSerializer;
import brian.scheduler.comm.event.JobEventDeserializer;
import brian.scheduler.comm.event.JobEventSerializer;

@Configuration
public class SchedulerCommunicationConfig {

	private final ObjectMapper objectMapper;
	
	public SchedulerCommunicationConfig(final ObjectMapper objectMapperIn) {
		objectMapper = objectMapperIn;
	}
	
	@Bean
	public JobEventSerializer<ExecuteJobEvent> executeJobEventSerializer() {
		return new ExecuteJobEventSerializer(objectMapper);
	}
	
	@Bean
	public JobEventDeserializer<ExecuteJobEvent> executeJobEventDeserializer() {
		return new ExecuteJobEventDeserializer(objectMapper);
	}
	
}
