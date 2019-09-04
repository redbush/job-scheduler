package brian.scheduler.app.mapper;

import java.util.List;

import brian.scheduler.app.domain.Job;
import brian.scheduler.comm.event.JobEvent;

/**
 * Mapper to convert the list of {@link Job}s to a list of {@link JobEvent}
 * @param <T> the job event type
 */
public interface JobEventMapper<T extends JobEvent> {

	/**
	 * Maps the list of jobs to a list of events
	 * 
	 * @param jobs the jobs to map
	 * @return the list of events
	 */
	List<T> map(final List<Job> jobs);
	
}
