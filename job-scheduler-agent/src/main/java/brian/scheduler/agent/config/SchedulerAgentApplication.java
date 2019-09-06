package brian.scheduler.agent.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot JobSchedulerAgent application
 */
@SpringBootApplication(scanBasePackages = { "brian.scheduler.agent" })
@EnableScheduling
public class SchedulerAgentApplication {

	/**
	 * @param args the args
	 */
	public static void main(final String[] args) {
		SpringApplication.run(SchedulerAgentApplication.class, args);
	}

}
