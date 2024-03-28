package ru.mai.pvk.robot.model.dto;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public final class TaskTestsDto {
	private String taskId;
    private final List<TestDto> taskTests;
}
