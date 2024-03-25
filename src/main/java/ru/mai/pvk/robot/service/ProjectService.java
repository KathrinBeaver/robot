package ru.mai.pvk.robot.service;

import ru.mai.pvk.robot.model.dto.ProjectDto;
import ru.mai.pvk.robot.model.dto.ProjectIterationsDto;

import java.util.List;

public interface ProjectService {

    public List<ProjectDto> getProjects();
    public ProjectDto getProjectbyId(Integer id);
    public void updateProject(ProjectDto projectDto);
    public void addProject(ProjectDto projectDto);

    ProjectIterationsDto getProjectIterations(String projectId);
}
