package ru.mai.pvk.robot.error.exception;

import static ru.mai.pvk.robot.error.ErrorType.PROJECT_ERROR;

public class ProjectProccessException extends CommonRuntimeException {

	public ProjectProccessException() {
		super(PROJECT_ERROR);
	}

}
