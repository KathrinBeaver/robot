package ru.mai.pvk.robot.service;

import ru.mai.pvk.robot.model.dto.*;

import java.util.List;

public interface TaskService {
    ThemeListDto getAllThemes();
    List<TaskDto> getAllTasks();
    ThemeTasksDto getThemeTasks(String themeId);
    TaskTestsDto getTaskTests(String testId);

    String addOrUpdateTask(TaskDto task);

    TestDto addOrUpdateTest(TestDto test);

    void addIssue(AddIssueDto data);

    TaskDto getTaskDto(String taskId);
}
