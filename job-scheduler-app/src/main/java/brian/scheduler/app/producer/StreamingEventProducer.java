package brian.scheduler.app.producer;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import brian.scheduler.app.domain.Job;
import brian.scheduler.app.mapper.JobEventMapper;
import brian.scheduler.app.repository.JobRepository;
import brian.scheduler.comm.event.ExecuteJobEvent;

@Component
public class StreamingEventProducer implements AutoCloseable, Callable<Void> {

	private final JobRepository jobRepository;
	private final JobEventMapper<ExecuteJobEvent> eventMapper;
	private final JobEventProducer<ExecuteJobEvent> eventProducer;
	private final ExecutorService threadPool;
	
	public StreamingEventProducer(
			final JobRepository jobRepositoryIn,
			final JobEventMapper<ExecuteJobEvent> eventMapperIn,
			final JobEventProducer<ExecuteJobEvent> eventProducerIn,
			final RedisJobEventProducer jobProducerIn) {

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
				System.out.println("sending event...");
				eventProducer.send(eventMapper.map(jobs));
			}
		}
	}

	@Override
	public void close() throws Exception {
		threadPool.shutdownNow();
	}
	
}
