package brian.scheduler.app.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import brian.scheduler.app.controller.SchedulerController;

/**
 * The job which is submitted to the {@link SchedulerController} as JSON. It contains the schedule and command to execute.
 */
@JsonDeserialize(builder = JobDto.Builder.class)
public class JobDto {

	@NotNull
	private final String id;
	
	@Min(value = 1)
	private final long interval;
	
	@NotNull // regex
	private final String timeUnit;
	
	@NotNull
	private final String command;

	private JobDto(final Builder builder) {

		id = builder.id;
		interval = builder.interval;
		timeUnit = builder.timeUnit;
		command = builder.command;
	}
	
	/**
	 * @return the unique job ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return how often the job is to execute. This unit is defined using {@link JobDto#getTimeUnit()}
	 */
	public long getInterval() {
		return interval;
	}

	/**
	 * The time unit for the interval. Acceptable values: SECONDS, MINUTES, HOURS, DAYS
	 * @return the time unit for the interval
	 */
	public String getTimeUnit() {
		return timeUnit;
	}

	/**
	 * @return the linux command to execute
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @return the builder to construct a JobDto
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * The builder to construct a JobDto
	 */
	@JsonPOJOBuilder
	public static class Builder {

		private String id;
		private long interval;
		private String timeUnit;
		private String command;

		/**
		 * @param idIn the unique job ID
		 * @return the builder
		 */
		public Builder withId(final String idIn) {
			id = idIn;
			return this;
		}
		
		/**
		 * @param intervalIn how often the job is to execute. This unit is defined using {@link Builder#withTimeUnit(String)}
		 * @return the builder
		 */
		public Builder withInterval(final long intervalIn) {
			interval = intervalIn;
			return this;
		}
		
		/**
		 * @param timeUnitIn the time unit for the interval. Acceptable values: SECONDS, MINUTES, HOURS, DAYS
		 * @return the builder
		 */
		public Builder withTimeUnit(final String timeUnitIn) {
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
		 * @return the constructed JobDto
		 */
		public JobDto build() {
			return new JobDto(this);
		}

	}

}
