package brian.scheduler.app.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import brian.scheduler.app.domain.Job;
import brian.scheduler.comm.event.ExecuteJobEvent;

@Component
public class ExecuteJobEventMapper implements JobEventMapper<ExecuteJobEvent> {

	@Override
	public List<ExecuteJobEvent> map(final List<Job> jobs) {
		
		return jobs
			.stream()
			.map(this::mapJobToEvent)
			.collect(Collectors.toList());
	}
	
	private ExecuteJobEvent mapJobToEvent(final Job job) {
		return ExecuteJobEvent
				.builder()
				.withId(job.getId())
				.withCommand(job.getCommand())
				.build();
	}
	
}
