package ru.mai.pvk.robot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.error.exception.TaskProccessException;
import ru.mai.pvk.robot.model.dto.*;
import ru.mai.pvk.robot.service.IssueCheckerService;
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
                "МКАД", "12345", "Василий ехал по МКАД со скоростью V. L - Длина МКАДА.", themeId, "mkad"));
        result.getTasksInTheme().add(TaskDto.of(
                "Складирование ноутбуков", "54321", "Ноутбуки лежат на складе. Лежат себе и лежат.", themeId, "notebooks"));

        return result;
    }
}
