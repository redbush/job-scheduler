package brian.scheduler.comm.event;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExecuteJobEventDeserializer implements JobEventDeserializer<ExecuteJobEvent> {

	private final ObjectMapper objectMapper;
	
	public ExecuteJobEventDeserializer(final ObjectMapper objectMapperIn) {
		objectMapper = objectMapperIn;
	}
	
	@Override
	public ExecuteJobEvent deserialize(final byte[] jobEvent) throws Exception {
		
		return objectMapper.readValue(jobEvent, ExecuteJobEvent.class);
	}

}
