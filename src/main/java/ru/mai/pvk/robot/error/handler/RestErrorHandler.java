package ru.mai.pvk.robot.error.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ru.mai.pvk.robot.error.ErrorType;
import ru.mai.pvk.robot.error.RestExceptionDto;
import ru.mai.pvk.robot.error.exception.ProjectProccessException;
import ru.mai.pvk.robot.error.exception.UserProccessException;


@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class RestErrorHandler {

	@ExceptionHandler(HttpMessageConversionException.class)
	protected ResponseEntity<Object> handle(HttpMessageConversionException ex) {
		log.error("Exception: {}", ex.getMessage(), ex);
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new RestExceptionDto(ErrorType.INVALID_JSON_PARSING, "Deserialization error"));
	}

    @ExceptionHandler(ProjectProccessException.class)
    protected ResponseEntity<Object> handle(ProjectProccessException ex) {
        log.error("Exception: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new RestExceptionDto(ErrorType.PROJECT_ERROR, "Project processing error"));
    }

	@ExceptionHandler(UserProccessException.class)
	protected ResponseEntity<Object> handle(UserProccessException ex) {
		log.error("Exception: {}", ex.getMessage(), ex);
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new RestExceptionDto(ErrorType.USER_ERROR, "User processing error"));
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleException(Exception ex) {
		log.error("Exception: {}", ex.getMessage(), ex);
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
