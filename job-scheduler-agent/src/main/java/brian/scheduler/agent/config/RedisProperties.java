package brian.scheduler.agent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Properties defining the Redis job cache key and event stream key
 */
@Configuration
@PropertySource("classpath:redis.properties")
public class RedisProperties {

	private final String jobEventStreamKey;
	
	/**
	 * @param jobEventStreamKeyIn the Redis stream key where job events are published
	 */
	public RedisProperties(
			@Value("${scheduler.redis.jobEventStreamKey}")
			final String jobEventStreamKeyIn) {
		
		jobEventStreamKey = jobEventStreamKeyIn;
	}

	/**
	 * @return the Redis stream key where job events are published
	 */
	public String getJobEventStreamKey() {
		return jobEventStreamKey;
	}
	
}
