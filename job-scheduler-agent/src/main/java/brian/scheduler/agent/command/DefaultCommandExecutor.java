package brian.scheduler.agent.command;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import brian.scheduler.agent.domain.JobCommand;

@Component
public class DefaultCommandExecutor implements CommandExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCommandExecutor.class);
	
	@Override
	@Async
	public void execute(final JobCommand jobCommand) throws Exception {
		
		LOGGER.debug("Executing command. ID: {}", jobCommand.getId());
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("sh", "-c", jobCommand.getCommand());
		Process process = processBuilder.start();
		process.waitFor(1, TimeUnit.MINUTES); // TODO: configurable
		LOGGER.debug("Command executed for ID: {}, ExitCode: {}", jobCommand.getId(), process.exitValue());
	}
	
}
