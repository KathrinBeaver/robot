package ru.mai.pvk.robot.model.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public final class TestDto {
	private final int testId;
    private final String inputData;
    private final String outPutData;
}
