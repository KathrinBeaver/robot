package ru.mai.pvk.robot.service;

import ru.mai.pvk.robot.model.dto.StudentListDto;
import ru.mai.pvk.robot.model.dto.TaskAndStudentListDto;
import ru.mai.pvk.robot.model.dto.IssueCheckerDto;
import ru.mai.pvk.robot.model.dto.IssueListDto;

public interface IssueCheckerService {
    void startFullCheck(IssueCheckerDto settings);
    void startSingleIssueCheck(IssueCheckerDto settings);
    StudentListDto getStudentsList(String projectId);

    IssueListDto getTasksList(String projectId, String iterationId);

    void assignTasksToStudents(TaskAndStudentListDto tasksAndStudents);
}
