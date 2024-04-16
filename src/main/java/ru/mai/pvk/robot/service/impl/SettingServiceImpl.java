package ru.mai.pvk.robot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.error.exception.ProjectProccessException;
import ru.mai.pvk.robot.error.exception.UserProccessException;
import ru.mai.pvk.robot.model.dto.ProjectDto;
import ru.mai.pvk.robot.model.dto.SettingDto;
import ru.mai.pvk.robot.property.RobotProperties;
import ru.mai.pvk.robot.securingweb.security.domain.model.User;
import ru.mai.pvk.robot.securingweb.security.domain.model.UserProjects;
import ru.mai.pvk.robot.securingweb.security.repository.UserProjectRepository;
import ru.mai.pvk.robot.securingweb.security.service.JwtService;
import ru.mai.pvk.robot.securingweb.security.service.UserService;
import ru.mai.pvk.robot.service.SettingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {
    private final UserService userService;
    private final UserProjectRepository userProjectRepository;
    private final RobotProperties robotProperties;

    public SettingDto fillFakeUserSettings(User user) {

        String name = user.getUsername();
        String url = robotProperties.getUrl();
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
        String name = user.getUsername();
        String url = robotProperties.getUrl();
        String apiKey = user.getRedmineApiKey();
        List<ProjectDto> projectsList = new ArrayList<>();
        Optional<List<UserProjects>> byUserId = userProjectRepository.findByUserId(user.getId());
        //TODO: Null check
        for (UserProjects userProjects : byUserId.get()) {
            projectsList.add(ProjectDto.of(
                    userProjects.getProjectId(),
                    userProjects.getProjectName()));
        }

        SettingDto settingDto = SettingDto.of(
                user.getId(), name, name, url, apiKey, projectsList);


        return settingDto;
    }

    @Override
    public String saveSettings(SettingDto settingDto) {
        if (settingDto.getUrl() != null && !settingDto.getUrl().isEmpty()) {
            robotProperties.setUrl(settingDto.getUrl());
        }
        User user = userService.getCurrentUser();
        user.setRedmineApiKey(settingDto.getApiKey());
        //TODO: Null check
        for (ProjectDto projectDto : settingDto.getProjectsList()) {
            UserProjects tmp = UserProjects.builder()
                    .userId(user.getId())
                    .projectId(projectDto.getProjectId())
                    .projectName(projectDto.getProjectName())
                    .build();

            userProjectRepository.save(tmp);
        }

        return "200 - OK";
    }

}
