package ru.mai.pvk.robot.error;

import lombok.Value;

@Value
public class RestExceptionDto {

	ErrorType type;

	String message;
}
