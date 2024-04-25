package ru.mai.pvk.robot.service;

import ru.mai.pvk.robot.model.dto.*;

public interface PlagiatService {
    PlagiatDto getPlagiat(String taskId);

    PlagiatTwoIssuesComparisonDto getComparison(String taskId, String firstStudent, String secondStudent);
}
