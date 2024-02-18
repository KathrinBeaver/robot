package ru.mai.pvk.robot.error.exception;

import static ru.mai.pvk.robot.error.ErrorType.INVALID_JSON_PARSING;

public class JsonParsingException extends CommonRuntimeException {

	public JsonParsingException(String message, Throwable thr) {
		super(INVALID_JSON_PARSING, message, thr);
	}

}
