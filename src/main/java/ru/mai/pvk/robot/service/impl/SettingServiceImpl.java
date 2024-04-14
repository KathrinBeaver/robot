package ru.mai.pvk.robot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.error.exception.ProjectProccessException;
import ru.mai.pvk.robot.error.exception.UserProccessException;
import ru.mai.pvk.robot.model.dto.ProjectDto;
import ru.mai.pvk.robot.model.dto.SettingDto;
import ru.mai.pvk.robot.securingweb.security.domain.model.User;
import ru.mai.pvk.robot.securingweb.security.service.JwtService;
import ru.mai.pvk.robot.securingweb.security.service.UserService;
import ru.mai.pvk.robot.service.SettingService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {
    private final UserService userService;

    public SettingDto fillFakeUserSettings(User user) {

        String name = user.getUsername();
        String url = "https://hostedredmine.com";
        String apiKey = user.getRedmineApiKey();

        List<ProjectDto> projectsList = new ArrayList<>();
        projectsList.add(ProjectDto.of("pvk-redmine-tp-2020", "ТП-2020"));
        projectsList.add(ProjectDto.of("pvk-redmine-tp-2022", "ТП-2022"));
        projectsList.add(ProjectDto.of("pvk-redmine-opps-2020", "ОПППС"));

        SettingDto settingDto = SettingDto.of(user.getId(),
                name,
                name,
                url,
                apiKey,
                projectsList
        );

        return settingDto;
    }

    @Override
    public SettingDto getUserSettings() {
        User user = userService.getCurrentUser();
        return fillFakeUserSettings(user);
    }
}
