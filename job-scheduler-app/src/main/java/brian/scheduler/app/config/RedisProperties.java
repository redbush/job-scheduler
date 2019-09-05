package brian.scheduler.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Properties defining the Redis job cache key and event stream key
 */
@Configuration
@PropertySource("classpath:redis.properties")
public class RedisProperties {

	private final String jobCacheKey;
	private final String jobEventStreamKey;
	
	/**
	 * @param jobCacheKeyIn the Redis cache key where the Jobs are stored
	 * @param jobEventStreamKeyIn the Redis stream key where job events are published
	 */
	public RedisProperties(
			@Value("${scheduler.redis.jobCacheKey}")
			final String jobCacheKeyIn,
			@Value("${scheduler.redis.jobEventStreamKey}")
			final String jobEventStreamKeyIn) {
		
		jobCacheKey = jobCacheKeyIn;
		jobEventStreamKey = jobEventStreamKeyIn;
	}

	/**
	 * @return the Redis cache key where the Jobs are stored
	 */
	public String getJobCacheKey() {
		return jobCacheKey;
	}

	/**
	 * @return the Redis stream key where job events are published
	 */
	public String getJobEventStreamKey() {
		return jobEventStreamKey;
	}
	
}
