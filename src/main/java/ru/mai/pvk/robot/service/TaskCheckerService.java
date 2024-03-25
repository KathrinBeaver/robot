package ru.mai.pvk.robot.service;

import ru.mai.pvk.robot.model.dto.TaskCheckerDto;

public interface TaskCheckerService {
    void startFullCheck(TaskCheckerDto settings);
    void startSingleIssueCheck(TaskCheckerDto settings);
}
