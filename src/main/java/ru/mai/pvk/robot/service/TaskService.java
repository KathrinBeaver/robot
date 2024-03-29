package ru.mai.pvk.robot.service;

import ru.mai.pvk.robot.model.dto.*;

public interface TaskService {
    ThemeListDto getThemes();
    ThemeTasksDto getThemeTasks(String themeId);
    TaskTestsDto getTaskTests(String testId);

    String addOrUpdateTask(TaskDto task);

    TestDto addOrUpdateTest(TestDto test);

    void addIssue(AddIssueDto data);
}
