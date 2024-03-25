package ru.mai.pvk.robot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.model.dto.IssueCheckerSettingsDto;
import ru.mai.pvk.robot.model.dto.TaskCheckerDto;
import ru.mai.pvk.robot.service.RobotSettingsService;
import ru.mai.pvk.robot.service.TaskCheckerService;

@Service
@RequiredArgsConstructor
public class TaskCheckerServiceImpl implements TaskCheckerService {
    @Override
    public void startSingleIssueCheck(TaskCheckerDto settings) {

    }

    @Override
    public void startFullCheck(TaskCheckerDto aSettings) {

    }
}
