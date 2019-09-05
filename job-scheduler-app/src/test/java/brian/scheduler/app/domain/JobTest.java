package brian.scheduler.app.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

class JobTest {

	@Test
	void build() {
		
		String expectedId = UUID.randomUUID().toString();
		long expectedInterval = 10;
		TimeUnit expectedTimeUnit = TimeUnit.MINUTES;
		String expectedCommand = "ls -al";
		
		Job actual = Job
			.builder()
			.withId(expectedId)
			.withInterval(expectedInterval)
			.withTimeUnit(expectedTimeUnit)
			.withCommand(expectedCommand)
			.build();
		
		assertEquals(actual.getId(), expectedId);
		assertEquals(actual.getInterval(), expectedInterval);
		assertEquals(actual.getTimeUnit(), expectedTimeUnit);
		assertEquals(actual.getCommand(), expectedCommand);
	}

}
