package brian.scheduler.comm.event;

public interface JobEventDeserializer<T extends JobEvent> {

	T deserialize(byte[] jobEvent) throws Exception;
	
}
