package brian.scheduler.app.repository;

import java.util.List;

import brian.scheduler.app.domain.Job;

/**
 * The repository to add and find Jobs to execute
 */
public interface StreamingJobRepository {

	/**
	 * Adds a Job
	 * 
	 * @param job the job to add
	 */
	void add(final Job job);
	
	/**
	 * Finds Jobs that are ready to be executed or blocks until Jobs are available
	 * 
	 * @return the list of Jobs to execute
	 * @throws InterruptedException thrown if the thread is interrupted
	 */
	List<Job> read() throws InterruptedException;
	
}
