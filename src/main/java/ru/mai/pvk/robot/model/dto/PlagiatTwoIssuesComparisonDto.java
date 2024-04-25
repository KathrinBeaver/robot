package ru.mai.pvk.robot.model.dto;

import lombok.Value;

import java.util.Map;

@Value(staticConstructor = "of")
public final class PlagiatTwoIssuesComparisonDto {
    private final TaskDto task;
    private final Map<String, String> twoIssuesDto;
}
