package brian.scheduler.app.service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import brian.scheduler.app.domain.Job;
import brian.scheduler.app.mapper.JobEventMapper;
import brian.scheduler.app.producer.JobEventProducer;
import brian.scheduler.app.repository.StreamingJobRepository;
import brian.scheduler.comm.event.ExecuteJobEvent;

/**
 * Daemon service which reads Jobs that are ready to execute from the Job
 * repository and publishes events for consumers to act on
 */
@Component
public class JobReaderService implements AutoCloseable, Callable<Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobReaderService.class);
	
	private final StreamingJobRepository jobRepository;
	private final JobEventMapper<ExecuteJobEvent> eventMapper;
	private final JobEventProducer<ExecuteJobEvent> eventProducer;
	private final ExecutorService threadPool;
	
	/**
	 * @param jobRepositoryIn the job repository to read Jobs that are ready to
	 *                        execute from
	 * @param eventMapperIn   the mapper to convert a Job to an ExecuteJobEvent
	 * @param eventProducerIn the producer to send the ExecuteJobEvent to consumers
	 *                        to act on
	 */
	public JobReaderService(
			final StreamingJobRepository jobRepositoryIn,
			final JobEventMapper<ExecuteJobEvent> eventMapperIn,
			final JobEventProducer<ExecuteJobEvent> eventProducerIn) {

		jobRepository = jobRepositoryIn;
		eventMapper = eventMapperIn;
		eventProducer = eventProducerIn;
		threadPool = Executors.newSingleThreadExecutor();
		threadPool.submit(this);
	}

	/**
	 * Reads Jobs to execute from the Job repository and publishes events
	 */
	@Override
	public Void call() throws Exception {
		
		while(true) {
			
			try {
				List<Job> jobs = jobRepository.read();
				if(jobs != null) {
					LOGGER.debug("Sending events");
					eventProducer.send(eventMapper.map(jobs));
				} 
			} catch(InterruptedException e) {
				throw e;
			} catch(Exception e) {
				LOGGER.error("An error occurred sending job events.", e);
			}
		}
	}

	/**
	 * Shuts down the thread pool running the reader
	 */
	@Override
	public void close() throws Exception {
		threadPool.shutdownNow();
	}
	
}
