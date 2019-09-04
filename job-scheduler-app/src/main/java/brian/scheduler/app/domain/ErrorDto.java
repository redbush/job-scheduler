package brian.scheduler.app.domain;

public class ErrorDto {

	private final String errorMessage;
	
	public ErrorDto(final String errorMessageIn) {
		errorMessage = errorMessageIn;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
}
