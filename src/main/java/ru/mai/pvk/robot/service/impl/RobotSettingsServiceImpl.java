package ru.mai.pvk.robot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.error.exception.UserProccessException;
import ru.mai.pvk.robot.model.dto.IssueCheckerSettingsDto;
import ru.mai.pvk.robot.model.dto.LintInfoType;
import ru.mai.pvk.robot.model.dto.ProjectDto;
import ru.mai.pvk.robot.model.dto.SettingDto;
import ru.mai.pvk.robot.service.RobotSettingsService;
import ru.mai.pvk.robot.service.SettingService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RobotSettingsServiceImpl implements RobotSettingsService {
    private IssueCheckerSettingsDto settings = IssueCheckerSettingsDto.of(
            true,
            true,
            true,
            true,
            LintInfoType.FULL_INFO,
            true
    );

    @Override
    public IssueCheckerSettingsDto getRobotSettings() {
        return settings;
    }

    @Override
    public void setRobotSettings(IssueCheckerSettingsDto aSettings) {
        IssueCheckerSettingsDto newSettings = IssueCheckerSettingsDto.of(
                aSettings.isShowErrorResponse(),
                aSettings.isCheckAllIterations(),
                aSettings.isNeedLint(),
                aSettings.isAssignTasksToStudent(),
                aSettings.getLintInformation(),
                aSettings.isNeedCloseTasks()
                );
        settings = newSettings;
    }

}
