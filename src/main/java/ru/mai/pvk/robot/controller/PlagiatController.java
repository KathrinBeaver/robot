package ru.mai.pvk.robot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.pvk.robot.error.exception.TaskProccessException;
import ru.mai.pvk.robot.model.dto.*;
import ru.mai.pvk.robot.service.PlagiatService;
import ru.mai.pvk.robot.service.TaskService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plagiat")
public class PlagiatController {

    private final PlagiatService plagiatService;

    @GetMapping(value = "/getPlagiat/{taskId}")
    public PlagiatDto getTasks(@PathVariable String taskId) {
        PlagiatDto plagiat;
        try {
            plagiat = plagiatService.getPlagiat(taskId);
        } catch (Exception e) {
            throw new TaskProccessException();
        }

        return plagiat;
    }

    @GetMapping(value = "/getPlagiatOfTwoIssues/{taskId}/{firstStudent}/{secondStudent}")
    public PlagiatTwoIssuesComparisonDto getComparison(@PathVariable String taskId,
                                    @PathVariable String firstStudent,
                                    @PathVariable String secondStudent) {
        PlagiatTwoIssuesComparisonDto plagiat;
        try {
            plagiat = plagiatService.getComparison(taskId,firstStudent,secondStudent);
        } catch (Exception e) {
            throw new TaskProccessException();
        }

        return plagiat;
    }
}
