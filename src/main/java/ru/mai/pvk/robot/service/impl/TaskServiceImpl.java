package ru.mai.pvk.robot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.error.exception.TaskProccessException;
import ru.mai.pvk.robot.model.dto.*;
import ru.mai.pvk.robot.service.TaskService;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    @Override
    public ThemeListDto getThemes() {
        ThemeListDto result = ThemeListDto.of(new ArrayList<>());
        result.getThemeListList().add(ThemeDto.of("Простая арифметика", "1"));
        result.getThemeListList().add(ThemeDto.of("Рекурсия", "2"));
        result.getThemeListList().add(ThemeDto.of("Функциональное программирование", "3"));

        return result;
    }

    @Override
    public ThemeTasksDto getThemeTasks(String themeId) {
        if (!themeId.equals("1")) {
            throw new TaskProccessException();
        }

        ThemeTasksDto result = ThemeTasksDto.of(ThemeDto.of(
                "Простая арифметика", themeId), new ArrayList<>()
        );
        result.getTasksInTheme().add(TaskDto.of(
                "МКАД", "12345", "Василий ехал по МКАД со скоростью V. L - Длина МКАДА.", themeId, "mkad", "func=contains"));
        result.getTasksInTheme().add(TaskDto.of(
                "Складирование ноутбуков", "54321", "Ноутбуки лежат на складе. Лежат себе и лежат.", themeId, "notebooks", ""));

        return result;
    }

    @Override
    public TaskTestsDto getTaskTests(String taskId) {
        TaskTestsDto result = TaskTestsDto.of(taskId, new ArrayList<>());
        result.getTaskTests().add(TestDto.of(1, taskId, "1 1 1", "3"));
        result.getTaskTests().add(TestDto.of(2, taskId, "1 \n 1", "2"));

        return result;
    }

    @Override
    public String addOrUpdateTask(TaskDto task) {

        return (task.getTaskId() != null) ? task.getTaskId() : "000000";
    }

    @Override
    public TestDto addOrUpdateTest(TestDto test) {
        int id = test.getTestId() == 0 ? 12345 : test.getTestId();
        TestDto result = TestDto.of(id, test.getTaskId(), test.getInputData(), test.getInputData());
        return result;
    }

    @Override
    public void addIssue(AddIssueDto data) {

    }

    @Override
    public TaskDto getTaskDto(String taskId) {
        TaskDto result = TaskDto.of(
                "Шашки",
                taskId,
                null,
                null,
                null,
                null
        );

        return result;
    }
}
