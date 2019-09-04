package brian.scheduler.comm.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = ExecuteJobEvent.Builder.class)
public class ExecuteJobEvent implements JobEvent {

	private final String id;
	private final String command;

	private ExecuteJobEvent(final Builder builder) {

		id = builder.id;
		command = builder.command;
	}

	public String getId() {
		return id;
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
		private String command;

		public Builder withId(final String idIn) {
			id = idIn;
			return this;
		}

		public Builder withCommand(final String commandIn) {
			command = commandIn;
			return this;
		}

		public ExecuteJobEvent build() {
			return new ExecuteJobEvent(this);
		}

	}

}
