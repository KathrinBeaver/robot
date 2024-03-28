package ru.mai.pvk.robot.model.dto;


import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value(staticConstructor = "of")
public class StudentListDto {
    private final List<StudentDto> studentList;
}
