package ru.mai.pvk.robot.model.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public final class IssueCheckerSettingsDto {
	private final boolean showErrorResponse;
	private final boolean checkAllIterations;
	private final boolean needLint;
	private final boolean assignTasksToStudent;
}


