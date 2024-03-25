package ru.mai.pvk.robot.service;

import ru.mai.pvk.robot.model.dto.IssueCheckerSettingsDto;
import ru.mai.pvk.robot.model.dto.SettingDto;

public interface RobotSettingsService {

    public IssueCheckerSettingsDto getRobotSettings();
    public void setRobotSettings(IssueCheckerSettingsDto aSettings);
}
