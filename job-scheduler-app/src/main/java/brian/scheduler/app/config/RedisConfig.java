package brian.scheduler.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import brian.scheduler.app.domain.Job;
import brian.scheduler.comm.event.ExecuteJobEvent;

@Configuration
public class RedisConfig {

	private final RedisConnectionFactory connectionFactory;
	
	public RedisConfig(final RedisConnectionFactory connectionFactoryIn) {
		connectionFactory = connectionFactoryIn;
	}
	
	@Bean
	public RedisTemplate<String, Job> redisJobTemplate() {
		
	    final RedisTemplate<String, Job> template = new RedisTemplate<>();
	    template.setConnectionFactory(connectionFactory);
	    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Job.class));
	    template.setKeySerializer(new StringRedisSerializer());
	    template.setHashKeySerializer(new StringRedisSerializer());
	    template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Job.class));
	    return template;
	}
	
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
