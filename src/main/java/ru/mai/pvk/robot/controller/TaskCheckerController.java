package ru.mai.pvk.robot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.pvk.robot.error.exception.ProjectProccessException;
import ru.mai.pvk.robot.model.dto.TaskCheckerDto;
import ru.mai.pvk.robot.service.TaskCheckerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/taskChecker")
public class TaskCheckerController {

    private final TaskCheckerService taskCheckerService;

    @PostMapping(consumes = "application/json",
            produces = "application/json",
            value = "/startFullCheck")
    public String startFullCheck(@RequestBody TaskCheckerDto taskCheckerSettings) {
        try {
            taskCheckerService.startFullCheck(taskCheckerSettings);
        } catch (Exception e) {
            throw new ProjectProccessException();
        }

        return "200 - проверка всех стартовала, детали в логе";
    }

    @PostMapping(consumes = "application/json",
            produces = "application/json",
            value = "/startCheckSingleIssue")
    public String startCheckSingleIssue(@RequestBody TaskCheckerDto taskCheckerSettings) {
        try {
            taskCheckerService.startSingleIssueCheck(taskCheckerSettings);
        } catch (Exception e) {
            throw new ProjectProccessException();
        }

        return "200 - проверка одной задачи стартовала, детали в логе";
    }
}
