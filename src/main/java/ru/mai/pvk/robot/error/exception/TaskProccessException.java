package ru.mai.pvk.robot.error.exception;

import static ru.mai.pvk.robot.error.ErrorType.TASK_OR_ISSUE_PROCESSING_ERROR;

public class TaskProccessException extends CommonRuntimeException {

	public TaskProccessException() {
		super(TASK_OR_ISSUE_PROCESSING_ERROR);
	}

}
