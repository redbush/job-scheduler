package brian.scheduler.app.repository;

import java.util.List;

import brian.scheduler.app.domain.Job;

public interface JobRepository {

	public void add(final Job job) throws Exception;
	
	public List<Job> read() throws InterruptedException;
	
}
