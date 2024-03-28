package ru.mai.pvk.robot.model.dto;


import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class TaskAndStudentListDto {
    private final List<IssueDto> tasksList;
    private final List<StudentDto> studentList;
}
