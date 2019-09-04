package brian.scheduler.app.mapper;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import brian.scheduler.app.domain.Job;
import brian.scheduler.app.domain.JobDto;

@Component
public class JobDtoToDomainMapper {

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
