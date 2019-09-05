package brian.scheduler.app.producer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;

import brian.scheduler.app.config.RedisProperties;
import brian.scheduler.comm.event.ExecuteJobEvent;

@ExtendWith(MockitoExtension.class)
class RedisJobEventProducerTest {

	@Mock
	private RedisTemplate<String, ExecuteJobEvent> redisTemplateMock;
	
	@Mock
	private RedisProperties redisPropertiesMock;
	
	@InjectMocks
	private RedisJobEventProducer eventProducer;
	
	@Mock
	private ExecuteJobEvent jobEventOneMock;
	
	@Mock
	private ExecuteJobEvent jobEventTwoMock;
	
	@Mock
	private StreamOperations<String, Object, Object> streamOperationsMock;
	
	@Captor
	private ArgumentCaptor<Map<Object, Object>> eventsCaptor;
	
	@Test
	void send() throws Exception {
		
		String expectedIdOne = UUID.randomUUID().toString();
		String expectedIdTwo = UUID.randomUUID().toString();
		String expectedStreamKey = "JOB_STREAM";
		when(jobEventOneMock.getId()).thenReturn(expectedIdOne);
		when(jobEventTwoMock.getId()).thenReturn(expectedIdTwo);
		when(redisPropertiesMock.getJobEventStreamKey()).thenReturn(expectedStreamKey);
		when(redisTemplateMock.opsForStream()).thenReturn(streamOperationsMock);
		
		eventProducer.send(Arrays.asList(jobEventOneMock, jobEventTwoMock));
		
		verify(jobEventOneMock).getId();
		verify(jobEventTwoMock).getId();
		verify(redisPropertiesMock).getJobEventStreamKey();
		verify(redisTemplateMock).opsForStream();
		verify(streamOperationsMock).add(Mockito.eq(expectedStreamKey), eventsCaptor.capture());
		Map<Object, Object> actualEvents = eventsCaptor.getValue();
		assertEquals(actualEvents.size(), 2);
		assertEquals(actualEvents.get(expectedIdOne), jobEventOneMock);
		assertEquals(actualEvents.get(expectedIdTwo), jobEventTwoMock);
	}

}
