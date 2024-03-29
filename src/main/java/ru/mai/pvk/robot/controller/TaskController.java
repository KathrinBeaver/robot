package ru.mai.pvk.robot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mai.pvk.robot.error.exception.TaskProccessException;
import ru.mai.pvk.robot.model.dto.*;
import ru.mai.pvk.robot.service.TaskService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping(value = "/getAllTopics")
    public ThemeListDto getAllTopics() {
        ThemeListDto themes;
        try {
            themes = taskService.getThemes();
        } catch (Exception e) {
            throw new TaskProccessException();
        }

        return themes;
    }

    @GetMapping(value = "/getTasks/{themeId}")
    public ThemeTasksDto getTasks(@PathVariable String themeId) {
        ThemeTasksDto themeTasks;
        try {
            themeTasks = taskService.getThemeTasks(themeId);
        } catch (Exception e) {
            throw new TaskProccessException();
        }

        return themeTasks;
    }

    @GetMapping(value = "/getTests/{taskId}")
    public TaskTestsDto getTests(@PathVariable String taskId) {
        TaskTestsDto taskTests;
        try {
            taskTests = taskService.getTaskTests(taskId);
        } catch (Exception e) {
            throw new TaskProccessException();
        }

        return taskTests;
    }

    @PutMapping(consumes = "application/json", produces = "application/json", value = "/addOrUpdateTask")
    public Map<String, String> addTask(@RequestBody TaskDto task) {
        String response;
        try {
            response = taskService.addOrUpdateTask(task);
        } catch (Exception e) {
            throw new TaskProccessException();
        }

        return Collections.singletonMap("taskId", response);
    }

    @PutMapping(consumes = "application/json", produces = "application/json", value = "/addOrUpdateTest")
    public TestDto addTest(@RequestBody TestDto test) {
        TestDto response;
        try {
            response = taskService.addOrUpdateTest(test);
        } catch (Exception e) {
            throw new TaskProccessException();
        }

        return response;
    }

    @PostMapping(consumes = "application/json", produces = "application/json", value = "/addIssue")
    public String addIssue(@RequestBody AddIssueDto data) {
        TestDto response;
        try {
            taskService.addIssue(data);
        } catch (Exception e) {
            throw new TaskProccessException();
        }

        return "200 - задача добавлена";
    }
}
