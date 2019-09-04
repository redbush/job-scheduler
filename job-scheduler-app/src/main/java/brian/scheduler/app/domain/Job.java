package brian.scheduler.app.domain;

import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import brian.scheduler.app.repository.StreamingJobRepository;

/**
 * The job which is added to and read from the {@link StreamingJobRepository}. It contains the schedule and command to execute.
 */
@JsonDeserialize(builder = Job.Builder.class)
public class Job {

	private final String id;
	private final long interval;
	private final TimeUnit timeUnit;
	private final String command;
	
	private Job(final Builder builder) {
		
		id = builder.id;
		interval = builder.interval;
		timeUnit = builder.timeUnit;
		command = builder.command;
	}
	
	/**
	 * @return the unique ID of the job
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * How often the job is to execute. This unit is defined using {@link Job#getTimeUnit()}
	 * 
	 * @return how often the job is to execute
	 */
	public long getInterval() {
		return interval;
	}
	
	/**
	 * @return the time unit for the interval
	 */
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	/**
	 * @return the linux command to execute
	 */
	public String getCommand() {
		return command;
	}
	
	/**
	 * @return the builder to construct a job
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * The builder to construct a job
	 */
	@JsonPOJOBuilder
	public static class Builder {
		
		private String id;
		private long interval;
		private TimeUnit timeUnit;
		private String command;
		
		/**
		 * @param idIn the unique ID of the job
		 * @return the builder
		 */
		public Builder withId(final String idIn) {
			id = idIn;
			return this;
		}

		/**
		 * @param intervalIn How often the job is to execute. This unit is defined using
		 *                   {@link Builder#withTimeUnit(TimeUnit)}
		 * @return the builder
		 */
		public Builder withInterval(final long intervalIn) {
			interval = intervalIn;
			return this;
		}
		
		/**
		 * @param timeUnitIn the time unit for the interval
		 * @return the builder
		 */
		public Builder withTimeUnit(final TimeUnit timeUnitIn) {
			timeUnit = timeUnitIn;
			return this;
		}
		
		/**
		 * @param commandIn the linux command to execute
		 * @return the builder
		 */
		public Builder withCommand(final String commandIn) {
			command = commandIn;
			return this;
		}
		
		/**
		 * @return the constructed job
		 */
		public Job build() {
			return new Job(this);
		}
		
	}
	
}
