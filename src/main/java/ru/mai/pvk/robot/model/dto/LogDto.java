package ru.mai.pvk.robot.model.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class LogDto {
    int recordId;
    String log;
}
