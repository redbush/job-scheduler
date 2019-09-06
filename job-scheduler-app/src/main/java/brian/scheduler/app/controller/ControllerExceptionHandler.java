package brian.scheduler.app.controller;

import java.text.MessageFormat;
import java.util.StringJoiner;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import brian.scheduler.app.domain.ErrorDto;

/**
 * Configures exception handling and error responses for the {@link SchedulerController}
 */
@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String ERROR_MSG_DELIMITER = ",";
	private static final String VALIDATION_ERROR_MSG = "[{0} {1}]";

	/**
	 * Creates an error response for validation failures
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		StringJoiner stringJoiner = new StringJoiner(ERROR_MSG_DELIMITER);
		ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
			stringJoiner.add(MessageFormat.format(VALIDATION_ERROR_MSG, fieldError.getField(), fieldError.getDefaultMessage()));
		});
		return new ResponseEntity<>(new ErrorDto(stringJoiner.toString()), status);
	}

	/**
	 * @param exception the exception that was thrown in the controller
	 * @return the generic error response
	 */
	@ExceptionHandler
	public ResponseEntity<ErrorDto> handleError(final Exception exception) {
		return new ResponseEntity<>(new ErrorDto("Generic error"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
