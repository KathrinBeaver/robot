package ru.mai.pvk.robot.service;

import ru.mai.pvk.robot.model.dto.StudentListDto;
import ru.mai.pvk.robot.model.dto.TaskAndStudentListDto;
import ru.mai.pvk.robot.model.dto.TaskCheckerDto;
import ru.mai.pvk.robot.model.dto.TaskListDto;

public interface TaskCheckerService {
    void startFullCheck(TaskCheckerDto settings);
    void startSingleIssueCheck(TaskCheckerDto settings);
    StudentListDto getStudentsList(String projectId);

    TaskListDto getTasksList(String projectId, String iterationId);

    void assignTasksToStudents(TaskAndStudentListDto tasksAndStudents);
}
