package brian.scheduler.app.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JobDtoTest {

	private Validator validator;
	
	@BeforeEach
	public void setup() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@Test
	void build() {
		
		String expectedId = UUID.randomUUID().toString();
		long expectedInterval = 10;
		String expectedTimeUnit = "MINUTES";
		String expectedCommand = "ls -al";
		
		JobDto actual = JobDto
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
	
	@Test
	void validation_IdNull() {
		
		JobDto jobDto = JobDto
			.builder()
			.withId(null)
			.withInterval(10)
			.withTimeUnit("MINUTES")
			.withCommand("ls -al")
			.build();
		
		Set<ConstraintViolation<JobDto>> actualViolations = validator.validate(jobDto);
		assertEquals(actualViolations.size(), 1);
		ConstraintViolation<JobDto> actualViolation = actualViolations.iterator().next();
		assertEquals(actualViolation.getPropertyPath().toString(), "id");
		assertEquals(actualViolation.getMessage(), "must not be null");
	}
	
	@Test
	void validation_IntervalSize() {
		
		JobDto jobDto = JobDto
			.builder()
			.withId(UUID.randomUUID().toString())
			.withInterval(0)
			.withTimeUnit("MINUTES")
			.withCommand("ls -al")
			.build();
		
		Set<ConstraintViolation<JobDto>> actualViolations = validator.validate(jobDto);
		assertEquals(actualViolations.size(), 1);
		ConstraintViolation<JobDto> actualViolation = actualViolations.iterator().next();
		assertEquals(actualViolation.getPropertyPath().toString(), "interval");
		assertEquals(actualViolation.getMessage(), "must be greater than or equal to 1");
	}
	
	@Test
	void validation_TimeUnitNull() {
		
		JobDto jobDto = JobDto
			.builder()
			.withId(UUID.randomUUID().toString())
			.withInterval(10)
			.withTimeUnit(null)
			.withCommand("ls -al")
			.build();
		
		Set<ConstraintViolation<JobDto>> actualViolations = validator.validate(jobDto);
		assertEquals(actualViolations.size(), 1);
		ConstraintViolation<JobDto> actualViolation = actualViolations.iterator().next();
		assertEquals(actualViolation.getPropertyPath().toString(), "timeUnit");
		assertEquals(actualViolation.getMessage(), "must not be null");
	}
	
	@Test
	void validation_TimeUnitRegex() {
		
		JobDto jobDto = JobDto
			.builder()
			.withId(UUID.randomUUID().toString())
			.withInterval(10)
			.withTimeUnit("somethingInvalid")
			.withCommand("ls -al")
			.build();
		
		Set<ConstraintViolation<JobDto>> actualViolations = validator.validate(jobDto);
		assertEquals(actualViolations.size(), 1);
		ConstraintViolation<JobDto> actualViolation = actualViolations.iterator().next();
		assertEquals(actualViolation.getPropertyPath().toString(), "timeUnit");
		assertEquals(actualViolation.getMessage(), "must match \"^(SECONDS|MINUTES|HOURS|DAYS)$\"");
	}
	
	@Test
	void validation_CommandNull() {
		
		JobDto jobDto = JobDto
			.builder()
			.withId(UUID.randomUUID().toString())
			.withInterval(10)
			.withTimeUnit("MINUTES")
			.withCommand(null)
			.build();
		
		Set<ConstraintViolation<JobDto>> actualViolations = validator.validate(jobDto);
		assertEquals(actualViolations.size(), 1);
		ConstraintViolation<JobDto> actualViolation = actualViolations.iterator().next();
		assertEquals(actualViolation.getPropertyPath().toString(), "command");
		assertEquals(actualViolation.getMessage(), "must not be null");
	}

}
