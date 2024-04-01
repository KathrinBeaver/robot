package ru.mai.pvk.robot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.error.exception.ProjectProccessException;
import ru.mai.pvk.robot.error.exception.UserProccessException;
import ru.mai.pvk.robot.model.dto.ProjectDto;
import ru.mai.pvk.robot.model.dto.SettingDto;
import ru.mai.pvk.robot.service.SettingService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    public SettingDto fillFakeUserSettings(String userId) {

        String name = "Ivan Petrov";
        String url = "https://hostedredmine.com";
        String apiKey = "4bb0d185-5de7-4dc3-b058-a426c9dea495";

        List<ProjectDto> projectsList = new ArrayList<>();
        projectsList.add(ProjectDto.of("pvk-redmine-tp-2020", "ТП-2020"));
        projectsList.add(ProjectDto.of("pvk-redmine-tp-2022", "ТП-2022"));
        projectsList.add(ProjectDto.of("pvk-redmine-opps-2020", "ОПППС"));

        SettingDto settingDto = SettingDto.of(42,
                name,
                name,
                url,
                apiKey,
                projectsList
        );

        return settingDto;
    }

    @Override
    public SettingDto getUserSettings(String userId) {
        if (Integer.parseInt(userId) == 42) {
            return fillFakeUserSettings(userId);
        }

        throw new UserProccessException();
    }

}
