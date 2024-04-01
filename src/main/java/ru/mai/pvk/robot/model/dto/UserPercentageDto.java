package ru.mai.pvk.robot.model.dto;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public final class UserPercentageDto {
	private final String userName;
    private final double percent;
}
