package brian.scheduler.comm.event;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExecuteJobEventSerializer implements JobEventSerializer<ExecuteJobEvent> {

	private final ObjectMapper objectMapper;
	
	public ExecuteJobEventSerializer(final ObjectMapper objectMapperIn) {
		objectMapper = objectMapperIn;
	}
	
	@Override
	public byte[] serialize(final ExecuteJobEvent jobEvent) throws Exception {
		
		return objectMapper.writeValueAsBytes(jobEvent);
	}

}
