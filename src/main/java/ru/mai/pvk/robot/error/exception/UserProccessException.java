package ru.mai.pvk.robot.error.exception;

import static ru.mai.pvk.robot.error.ErrorType.USER_ERROR;

public class UserProccessException extends CommonRuntimeException {

	public UserProccessException() {
		super(USER_ERROR);
	}

}
