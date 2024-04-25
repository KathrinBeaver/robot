package ru.mai.pvk.robot.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

@Value(staticConstructor = "of")
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TaskDto {
	private final String taskSubject;
    private final String taskId;
    private final String taskDescription;
    private final String themeId;
    private final String folderName;
    private final String config;
}
