package ru.mai.pvk.robot.model.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public final class StudentDto {
	private final String studentId;
    private final String studentName;

}
