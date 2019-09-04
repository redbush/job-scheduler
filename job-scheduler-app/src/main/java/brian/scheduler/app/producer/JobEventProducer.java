package brian.scheduler.app.producer;

import java.util.List;

import brian.scheduler.comm.event.JobEvent;

public interface JobEventProducer<T extends JobEvent> {

	void send(final List<T> jobEvents) throws Exception;
	
}
