package brian.scheduler.agent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import brian.scheduler.comm.event.ExecuteJobEvent;

/**
 * Redis template bean configuration
 */
@Configuration
public class RedisConfig {

	private final RedisConnectionFactory connectionFactory;
	
	/**
	 * @param connectionFactoryIn the Redis connection factory to configure the templates
	 */
	public RedisConfig(final RedisConnectionFactory connectionFactoryIn) {
		connectionFactory = connectionFactoryIn;
	}
	
	/**
	 * @return the template to consume ExecuteJobEvents from a Redis Stream
	 */
	@Bean
	public RedisTemplate<String, ExecuteJobEvent> redisJobEventTemplate() {
		
	    final RedisTemplate<String, ExecuteJobEvent> template = new RedisTemplate<>();
	    template.setConnectionFactory(connectionFactory);
	    template.setKeySerializer(new StringRedisSerializer());
	    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(ExecuteJobEvent.class));
	    template.setHashKeySerializer(new StringRedisSerializer());
	    template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ExecuteJobEvent.class));
	    return template;
	}
	
}
