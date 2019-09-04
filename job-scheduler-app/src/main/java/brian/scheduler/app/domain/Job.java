package brian.scheduler.app.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Job.Builder.class)
public class Job {

	private final String id;
	private final long interval;
	private final String timeUnit;
	private final String command;
	
	private Job(final Builder builder) {
		
		id = builder.id;
		interval = builder.interval;
		timeUnit = builder.timeUnit;
		command = builder.command;
	}
	
	public String getId() {
		return id;
	}
	
	public long getInterval() {
		return interval;
	}
	
	public String getTimeUnit() {
		return timeUnit;
	}

	public String getCommand() {
		return command;
	}
	
	public static Builder builder() {
		return new Builder();
	}

	@JsonPOJOBuilder
	public static class Builder {
		
		private String id;
		private long interval;
		private String timeUnit;
		private String command;
		
		public Builder withId(final String idIn) {
			id = idIn;
			return this;
		}

		public Builder withInterval(final long intervalIn) {
			interval = intervalIn;
			return this;
		}
		
		public Builder withTimeUnit(final String timeUnitIn) {
			timeUnit = timeUnitIn;
			return this;
		}
		
		public Builder withCommand(final String commandIn) {
			command = commandIn;
			return this;
		}
		
		public Job build() {
			return new Job(this);
		}
		
	}
	
}
