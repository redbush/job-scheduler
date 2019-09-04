package brian.scheduler.app.service;

import org.springframework.stereotype.Service;

import brian.scheduler.app.domain.Job;
import brian.scheduler.app.repository.JobRepository;

@Service
public class JobService {

	private final JobRepository jobRepository;
	
	public JobService(final JobRepository jobRepositoryIn) {
		jobRepository = jobRepositoryIn;
	}
	
	public void add(Job job) throws Exception {
		jobRepository.add(job);
	}
	
}
