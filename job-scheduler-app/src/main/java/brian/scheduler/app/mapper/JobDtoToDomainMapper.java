package brian.scheduler.app.mapper;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import brian.scheduler.app.domain.Job;
import brian.scheduler.app.domain.JobDto;

/**
 * Mapper to convert a JobDto to a Job
 */
@Component
public class JobDtoToDomainMapper {

	/**
	 * Maps a JobDto to a Job
	 * @param jobDto the jobDto to map
	 * @return the Job
	 */
	public Job map(final JobDto jobDto) {
		
		return Job
			.builder()
			.withId(jobDto.getId())
			.withInterval(jobDto.getInterval())
			.withTimeUnit(TimeUnit.valueOf(jobDto.getTimeUnit()))
			.withCommand(jobDto.getCommand())
			.build();
	}
	
}
