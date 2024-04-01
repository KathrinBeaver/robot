package ru.mai.pvk.robot.model.dto;

import lombok.Value;

import java.util.List;
import java.util.Map;

@Value(staticConstructor = "of")
public final class PlagiatDto {
	private final TaskDto task;
    private final Map<String, List<UserPercentageDto>> studentPlagiatPercentage;
}
