package ru.mai.pvk.robot.model.dto;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class ProjectIterationsDto {
    private String ProjectId;
    private List<String> projectIterations;
}
