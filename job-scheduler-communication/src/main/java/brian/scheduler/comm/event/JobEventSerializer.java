package brian.scheduler.comm.event;

public interface JobEventSerializer<T extends JobEvent> {

	byte[] serialize(T jobEvent) throws Exception;
	
}
