package brian.scheduler.app.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import brian.scheduler.app.domain.Job;
import brian.scheduler.app.domain.JobDto;
import brian.scheduler.app.mapper.JobDtoToDomainMapper;
import brian.scheduler.app.service.JobService;

@ExtendWith(MockitoExtension.class)
class SchedulerControllerTest {

	@Mock
	private JobDtoToDomainMapper jobMapperMock;
	
	@Mock
	private JobService jobServiceMock;
	
	@InjectMocks
	private SchedulerController controller;
	
	@Mock
	private JobDto jobDtoMock;
	
	@Mock
	private Job jobMock;
	
	@Test
	void submit() throws Exception {
		
		when(jobMapperMock.map(jobDtoMock)).thenReturn(jobMock);
		
		controller.submit(jobDtoMock);
		
		verify(jobMapperMock).map(jobDtoMock);
		verify(jobServiceMock).add(jobMock);
	}

}
