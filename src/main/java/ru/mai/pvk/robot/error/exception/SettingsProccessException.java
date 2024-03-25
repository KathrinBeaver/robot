package ru.mai.pvk.robot.error.exception;

import static ru.mai.pvk.robot.error.ErrorType.SETTINGS_ERROR;

public class SettingsProccessException extends CommonRuntimeException {

	public SettingsProccessException() {
		super(SETTINGS_ERROR);
	}

}
