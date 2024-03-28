package ru.mai.pvk.robot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.pvk.robot.error.exception.ProjectProccessException;
import ru.mai.pvk.robot.error.exception.TaskProccessException;
import ru.mai.pvk.robot.model.dto.StudentListDto;
import ru.mai.pvk.robot.model.dto.TaskAndStudentListDto;
import ru.mai.pvk.robot.model.dto.IssueCheckerDto;
import ru.mai.pvk.robot.model.dto.IssueListDto;
import ru.mai.pvk.robot.service.IssueCheckerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/issueChecker")
public class IssueCheckerController {

    private final IssueCheckerService taskCheckerService;

    @PostMapping(consumes = "application/json",
            produces = "application/json",
            value = "/startFullCheck")
    public String startFullCheck(@RequestBody IssueCheckerDto taskCheckerSettings) {
        try {
            taskCheckerService.startFullCheck(taskCheckerSettings);
        } catch (Exception e) {
            throw new TaskProccessException();
        }

        return "200 - проверка всех стартовала, детали в логе";
    }

    @PostMapping(consumes = "application/json",
            produces = "application/json",
            value = "/startCheckSingleIssue")
    public String startCheckSingleIssue(@RequestBody IssueCheckerDto taskCheckerSettings) {
        try {
            taskCheckerService.startSingleIssueCheck(taskCheckerSettings);
        } catch (Exception e) {
            throw new TaskProccessException();
        }

        return "200 - проверка одной задачи стартовала, детали в логе";
    }

    @PostMapping(consumes = "application/json",
            produces = "application/json",
            value = "/assignTasksToStudents")
    public String assignTasksToStudents(@RequestBody TaskAndStudentListDto tasksAndStudents) {
        try {
            taskCheckerService.assignTasksToStudents(tasksAndStudents);
        } catch (Exception e) {
            throw new TaskProccessException();
        }

        return "200 - стартовала раздача задач студентам";
    }

    @GetMapping(value = "/getStudentsList/{projectId}")
    public StudentListDto getStudentsList(@PathVariable("projectId") String projectId) {
        StudentListDto studentsList;
        try {
            studentsList = taskCheckerService.getStudentsList(projectId);
        } catch (Exception e) {
            throw new ProjectProccessException();
        }

        return studentsList;
    }


    @GetMapping(value = "/getTasksList/{projectId}/{iterationId}")
    public IssueListDto getTasksList(
            @PathVariable("projectId") String projectId,
            @PathVariable("iterationId") String iterationId) {
        IssueListDto tasksList;
        try {
            tasksList = taskCheckerService.getTasksList(projectId, iterationId);
        } catch (Exception e) {
            throw new ProjectProccessException();
        }

        return tasksList;
    }
}
