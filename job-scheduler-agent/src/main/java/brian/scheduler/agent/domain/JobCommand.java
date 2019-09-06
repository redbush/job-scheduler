package brian.scheduler.agent.domain;

public class JobCommand {

	private final String id;
	private final String command;

	private JobCommand(final Builder builder) {

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

		public JobCommand build() {
			return new JobCommand(this);
		}

	}
	
}
