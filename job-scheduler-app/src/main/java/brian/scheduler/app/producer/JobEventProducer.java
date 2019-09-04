package brian.scheduler.app.producer;

import java.util.List;

import brian.scheduler.comm.event.JobEvent;

/**
 * Producer to send a JobEvent to a destination
 * 
 * @param <T> the job event type
 */
public interface JobEventProducer<T extends JobEvent> {

	/**
	 * Sends the JobEvents to a destination
	 *  
	 * @param jobEvents the list of events to send
	 * @throws Exception thrown if an error occurs sending the events
	 */
	void send(final List<T> jobEvents) throws Exception;
	
}
