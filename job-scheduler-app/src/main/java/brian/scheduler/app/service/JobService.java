package brian.scheduler.app.service;

import org.springframework.stereotype.Service;

import brian.scheduler.app.domain.Job;
import brian.scheduler.app.repository.StreamingJobRepository;

@Service
public class JobService {

	private final StreamingJobRepository jobRepository;
	
	public JobService(final StreamingJobRepository jobRepositoryIn) {
		jobRepository = jobRepositoryIn;
	}
	
	public void add(Job job) throws Exception {
		jobRepository.add(job);
	}
	
}
