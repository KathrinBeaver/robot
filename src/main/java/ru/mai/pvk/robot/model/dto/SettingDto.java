package ru.mai.pvk.robot.model.dto;

import lombok.Data;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public final class SettingDto {

	private final int  id;
	private final String name;
    private final String url;

	private final String APIKey;

	private final List<ProjectDto> projectsList;
}
