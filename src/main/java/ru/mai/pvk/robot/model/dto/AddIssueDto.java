package ru.mai.pvk.robot.model.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class AddIssueDto {
    String taskId;
    String iterationId;
}
