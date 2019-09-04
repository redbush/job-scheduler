package brian.scheduler.app.service;

import org.springframework.stereotype.Service;

import brian.scheduler.app.domain.Job;
import brian.scheduler.app.repository.StreamingJobRepository;

/**
 * Service to add Jobs to the JobRepository
 */
@Service
public class JobService {

	private final StreamingJobRepository jobRepository;
	
	/**
	 * @param jobRepositoryIn the repository to add the Jobs to
	 */
	public JobService(final StreamingJobRepository jobRepositoryIn) {
		jobRepository = jobRepositoryIn;
	}
	
	/**
	 * Adds a Job to the repository
	 * 
	 * @param job the job to add
	 * @throws Exception thrown if an error occurs adding the job
	 */
	public void add(final Job job) throws Exception {
		jobRepository.add(job);
	}
	
}
