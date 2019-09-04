package brian.scheduler.app.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot JobScheduler application
 */
@SpringBootApplication(scanBasePackages = { "brian.scheduler.app" })
public class SchedulerApplication {

	/**
	 * @param args the args
	 */
	public static void main(final String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
	}

}
