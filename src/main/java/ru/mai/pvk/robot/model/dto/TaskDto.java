package ru.mai.pvk.robot.model.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public final class TaskDto {
	private final String taskSubject;
    private final String taskId;
    private final String taskDescription;
    private final String themeId;
    private final String folderName;
    private final String config;
}
