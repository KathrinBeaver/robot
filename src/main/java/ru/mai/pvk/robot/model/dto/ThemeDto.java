package ru.mai.pvk.robot.model.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public final class ThemeDto {
	private final String themeName;
    private final String themeId;
}
