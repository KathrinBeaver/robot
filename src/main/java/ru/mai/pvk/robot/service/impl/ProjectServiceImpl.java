package ru.mai.pvk.robot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.model.dto.ProjectDto;
import ru.mai.pvk.robot.service.ProjectService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

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
}
