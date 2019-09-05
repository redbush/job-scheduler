package brian.scheduler.app.it;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import brian.scheduler.app.config.SchedulerApplication;
import brian.scheduler.app.domain.JobDto;
import brian.scheduler.app.it.JobSchedulerIT.TestRedisServer;
import redis.embedded.RedisServer;

@SpringBootTest(classes = {SchedulerApplication.class, TestRedisServer.class})
@AutoConfigureMockMvc
class JobSchedulerIT {

	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void subit() throws Exception {
		
		JobDto jobDto = JobDto
			.builder()
			.withId(UUID.randomUUID().toString())
			.withInterval(1)
			.withTimeUnit("SECONDS")
			.withCommand("ls -al")
			.build();
		
		mvc.perform(MockMvcRequestBuilders.post("/submit").content(objectMapper.writeValueAsString(jobDto))
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@TestConfiguration
	public static class TestRedisServer {
	 
	    private RedisServer redisServer;
	 
	    public TestRedisServer(@Value("${spring.redis.port}") int port) {
	        redisServer = new RedisServer(port);
	    }
	 
	    @PostConstruct
	    public void postConstruct() {
	        redisServer.start();
	    }
	 
	    @PreDestroy
	    public void preDestroy() {
	        redisServer.stop();
	    }
	}

}
