package ru.mai.pvk.robot.controlller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.pvk.robot.error.exception.ProjectProccessException;
import ru.mai.pvk.robot.model.dto.ProjectDto;
import ru.mai.pvk.robot.model.dto.SettingDto;
import ru.mai.pvk.robot.service.ProjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;
    @GetMapping(value = "")
    public List<ProjectDto> getAllProjects () {
        return projectService.getProjects();
    }

    @GetMapping(value = "/{id}")
    public ProjectDto getProject (@PathVariable("id") Integer id) {
        return projectService.getProjectbyId(id);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public String updateProject (@RequestBody ProjectDto projectDto) {
        try {
            projectService.updateProject(projectDto);
        }
        catch (Exception e) {
            throw new ProjectProccessException();
        }

        return "Проект успешно обновлен";
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public String addProject (@RequestBody ProjectDto projectDto) {
        try {
            projectService.addProject(projectDto);
        }
        catch (Exception e) {
            throw new ProjectProccessException();
        }

        return "Проект успешно добавлен";
    }

}
