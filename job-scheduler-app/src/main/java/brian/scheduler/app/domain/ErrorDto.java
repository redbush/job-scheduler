package brian.scheduler.app.domain;

import brian.scheduler.app.controller.SchedulerController;

/**
 * Contains the error message which is return as a JSON response when an
 * exception is raised in the {@link SchedulerController}
 */
public class ErrorDto {

	private final String errorMessage;
	
	/**
	 * @param errorMessageIn the error message
	 */
	public ErrorDto(final String errorMessageIn) {
		errorMessage = errorMessageIn;
	}

	/**
	 * @return the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
}
