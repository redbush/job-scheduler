package brian.scheduler.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import brian.scheduler.app.domain.JobDto;
import brian.scheduler.app.mapper.JobDtoToDomainMapper;
import brian.scheduler.app.service.JobService;

/**
 * REST controller to submit Jobs for execution
 */
@RestController
public class SchedulerController {

	private final JobDtoToDomainMapper jobMapper;
	private final JobService jobService;
	
	/**
	 * @param jobMapperIn the mapper to convert the JobDto to a Job
	 * @param jobServiceIn the service to add the Job to the repository for execution
	 */
	public SchedulerController(
			final JobDtoToDomainMapper jobMapperIn,
			final JobService jobServiceIn) {
		
		jobMapper = jobMapperIn;
		jobService = jobServiceIn;
	}
	
	/**
	 * Submits the job for execution
	 * 
	 * @param jobDto the job containing the command to execute and schedule
	 * @throws Exception thrown if an error occurs adding the job
	 */
	@PostMapping(path = "/submit", consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public void submit(@Validated @RequestBody final JobDto jobDto) throws Exception {

		jobService.add(jobMapper.map(jobDto));
	}
	
}
