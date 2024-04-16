package ru.mai.pvk.robot.service;

import ru.mai.pvk.robot.model.dto.SettingDto;

public interface SettingService {

    SettingDto getUserSettings();

    String saveSettings(SettingDto settingDto);
}
