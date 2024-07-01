package ru.mai.pvk.robot.service.impl;

import com.taskadapter.redmineapi.RedmineException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.model.dto.ProjectDto;
import ru.mai.pvk.robot.model.dto.ProjectIterationsDto;
import ru.mai.pvk.robot.property.RobotProperties;
import ru.mai.pvk.robot.redmine.ConnectionWithRedmine;
import ru.mai.pvk.robot.securingweb.security.domain.model.User;
import ru.mai.pvk.robot.securingweb.security.service.UserService;
import ru.mai.pvk.robot.service.ProjectService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final RobotProperties robotProperties;
    private final ConnectionWithRedmine connection;
    private final UserService userService;

    private final List<ProjectDto> projectList = new ArrayList<>();

    @Override
    public List<ProjectDto> getProjects() {
        return projectList;
    }

    @Override
    public ProjectDto getProjectbyId(Integer id) {
        return projectList.get(id);
    }

    @Override
    public void updateProject(ProjectDto projectDto) {
        for (int i = 0; i < projectList.size(); i++) {
            if (projectList.get(i).getProjectId().equals(projectDto.getProjectId())) {
                projectList.set(i, projectDto);
                break;
            }
        }
    }

    @Override
    public void addProject(ProjectDto projectDto) {
        projectList.add(projectDto);
    }

    @Override
    public ProjectIterationsDto getProjectIterations(String projectId) {
        User user = userService.getCurrentUser();
        if (!connection.isInitialized()) {
            try {
                connection.init(robotProperties.getUrl(), user.getRedmineApiKey(), projectId);
            } catch (RedmineException e) {
                throw new RuntimeException(e);
            }
        }
        List<String> iterations = connection.getVersions(projectId).stream().toList();

        ProjectIterationsDto result = ProjectIterationsDto.of(
                projectId,
                iterations
        );

        return result;
    }
}
