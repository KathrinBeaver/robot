package ru.mai.pvk.robot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.model.dto.ProjectDto;
import ru.mai.pvk.robot.model.dto.SettingDto;
import ru.mai.pvk.robot.service.ProjectService;
import ru.mai.pvk.robot.service.SettingService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private List<ProjectDto> projectsList = new ArrayList<>();

    @Override
    public List<ProjectDto> getProjects() {
        return projectsList;
    }

    @Override
    public ProjectDto getProjectbyId(Integer id) {
        return projectsList.get(id);
    }

    @Override
    public void updateProject(ProjectDto projectDto) {
        projectsList.set(projectDto.getId(), projectDto);
    }

    @Override
    public void addProject(ProjectDto projectDto) {
        projectsList.add(projectDto);
    }
}
