package ru.mai.pvk.robot.model.dto;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public final class ProjectDto {

	private final String projectId;
    private final String projectName;

}
