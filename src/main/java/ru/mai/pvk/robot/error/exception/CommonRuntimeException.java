package ru.mai.pvk.robot.error.exception;

import lombok.Getter;
import ru.mai.pvk.robot.error.ErrorType;

@Getter
public class CommonRuntimeException extends RuntimeException {

	private final ErrorType type;

	CommonRuntimeException(ErrorType type, String message, Throwable cause) {
		super(message, cause);
		this.type = type;
	}

	CommonRuntimeException(ErrorType type, String message) {
		super(message);
		this.type = type;
	}

    CommonRuntimeException(ErrorType type) {
        this.type = type;
    }
}
