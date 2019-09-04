package brian.scheduler.app.producer;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import brian.scheduler.app.domain.Job;
import brian.scheduler.app.mapper.JobEventMapper;
import brian.scheduler.app.repository.StreamingJobRepository;
import brian.scheduler.comm.event.ExecuteJobEvent;

@Component
public class StreamingEventProducer implements AutoCloseable, Callable<Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamingEventProducer.class);
	
	private final StreamingJobRepository jobRepository;
	private final JobEventMapper<ExecuteJobEvent> eventMapper;
	private final JobEventProducer<ExecuteJobEvent> eventProducer;
	private final ExecutorService threadPool;
	
	public StreamingEventProducer(
			final StreamingJobRepository jobRepositoryIn,
			final JobEventMapper<ExecuteJobEvent> eventMapperIn,
			final JobEventProducer<ExecuteJobEvent> eventProducerIn) {

		jobRepository = jobRepositoryIn;
		eventMapper = eventMapperIn;
		eventProducer = eventProducerIn;
		threadPool = Executors.newSingleThreadExecutor();
		threadPool.submit(this);
	}

	@Override
	public Void call() throws Exception {
		
		while(true) {
			
			List<Job> jobs = jobRepository.read();
			if(jobs != null) {
				LOGGER.debug("Sending events");
				eventProducer.send(eventMapper.map(jobs));
			} // need to bubble interrupt but catch EP errors
		}
	}

	@Override
	public void close() throws Exception {
		threadPool.shutdownNow();
	}
	
}
