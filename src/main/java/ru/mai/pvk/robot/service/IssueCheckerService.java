package ru.mai.pvk.robot.service;

import ru.mai.pvk.robot.model.dto.*;

import java.util.List;

public interface IssueCheckerService {
    void startFullCheck(IssueCheckerDto settings);
    void startSingleIssueCheck(IssueCheckerDto settings);
    StudentListDto getStudentsList(String projectId);

    IssueListDto getTasksList(String projectId, String iterationId);

    void assignTasksToStudents(TaskAndStudentListDto tasksAndStudents);

    List<LogDto> getLogs(int idStart);
}
