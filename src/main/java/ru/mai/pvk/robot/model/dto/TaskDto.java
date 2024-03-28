package ru.mai.pvk.robot.model.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public final class TaskDto {
	private final String taskId;
    private final String taskSubject;

}
