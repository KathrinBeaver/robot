package ru.mai.pvk.robot.model.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public final class TaskCheckerDto {
	private final String projectId;
	private final String iteration;
	private final int issueId;
	private final IssueCheckerSettingsDto settings;
}


