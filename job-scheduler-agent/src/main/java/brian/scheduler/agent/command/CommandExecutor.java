package brian.scheduler.agent.command;

import brian.scheduler.agent.domain.JobCommand;

public interface CommandExecutor {

	void execute(final JobCommand jobCommand) throws Exception;
	
}
