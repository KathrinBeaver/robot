package ru.mai.pvk.robot.service;

import ru.mai.pvk.robot.model.dto.ProjectDto;

import java.util.List;

public interface ProjectService {

    public List<ProjectDto> getProjects();
    public ProjectDto getProjectbyId(Integer id);
    public void updateProject(ProjectDto projectDto);
    public void addProject(ProjectDto projectDto);
}
