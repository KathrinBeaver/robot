package ru.mai.pvk.robot.model.dto;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public final class ProjectDto {

	private final Integer id;
    private final String name;

}
