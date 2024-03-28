package ru.mai.pvk.robot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.model.dto.*;
import ru.mai.pvk.robot.service.RobotSettingsService;
import ru.mai.pvk.robot.service.TaskCheckerService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskCheckerServiceImpl implements TaskCheckerService {
    @Override
    public void startSingleIssueCheck(TaskCheckerDto settings) {

    }

    @Override
    public void startFullCheck(TaskCheckerDto aSettings) {

    }

    @Override
    public StudentListDto getStudentsList(String projectId) {
        StudentListDto students = StudentListDto.of(new ArrayList<>());
        students.getStudentList().add(
                StudentDto.of("12345", "Иванов Иван"));
        students.getStudentList().add(
                StudentDto.of("12346", "Петров Петр"));
        students.getStudentList().add(
                StudentDto.of("12347", "Кузнецов Слава"));

        return students;
    }

    @Override
    public TaskListDto getTasksList(String projectId, String iterationId) {
        TaskListDto tasks = TaskListDto.of(new ArrayList<>());
        tasks.getTasksList().add(
                TaskDto.of("12345", "Шашки"));
        tasks.getTasksList().add(
                TaskDto.of("12346", "МКАД"));
        tasks.getTasksList().add(
                TaskDto.of("12347", "Последнее слово"));

        return tasks;
    }
}
