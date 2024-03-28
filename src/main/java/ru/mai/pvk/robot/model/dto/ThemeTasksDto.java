package ru.mai.pvk.robot.model.dto;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public final class ThemeTasksDto {
	private final ThemeDto theme;
    private final List<TaskDto> tasksInTheme;
}
