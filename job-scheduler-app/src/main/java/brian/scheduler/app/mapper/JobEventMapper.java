package brian.scheduler.app.mapper;

import java.util.List;

import brian.scheduler.app.domain.Job;
import brian.scheduler.comm.event.JobEvent;

public interface JobEventMapper<T extends JobEvent> {

	List<T> map(final List<Job> jobs);
	
}
