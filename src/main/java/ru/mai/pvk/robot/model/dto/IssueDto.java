package ru.mai.pvk.robot.model.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public final class IssueDto {
	private final String issueId;
    private final String issueSubject;

}
