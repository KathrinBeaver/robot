package ru.mai.pvk.robot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.model.dto.*;
import ru.mai.pvk.robot.property.RobotProperties;
import ru.mai.pvk.robot.service.IssueCheckerService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class IssueCheckerServiceImpl implements IssueCheckerService {
    private final RobotProperties robotProperties;

    @Override
    public void startSingleIssueCheck(IssueCheckerDto settings) {

    }

    @Override
    public void startFullCheck(IssueCheckerDto aSettings) {

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
    public IssueListDto getTasksList(String projectId, String iterationId) {
        IssueListDto tasks = IssueListDto.of(new ArrayList<>());
        tasks.getIssueList().add(
                IssueDto.of("12345", "Шашки"));
        tasks.getIssueList().add(
                IssueDto.of("12346", "МКАД"));
        tasks.getIssueList().add(
                IssueDto.of("12347", "Последнее слово"));

        return tasks;
    }

    @Override
    public void assignTasksToStudents(TaskAndStudentListDto tasksAndStudents) {

    }

    @Override
    public List<LogDto> getLogs(int idStart) {
        List<LogDto> result = new ArrayList<>();
        String address = robotProperties.getUrl();
        int startLog = (new Random()).nextInt(300);

        result.add(LogDto.of(idStart + 1, "LogId = " + idStart + ". Started at " + LocalDateTime.now().toLocalTime()));

        for (int i = idStart + 1; i < idStart + 20; i++) {
            if (i % 2 == 0) {
                result.add(LogDto.of(i, wrapToHref(String.valueOf(900000 + i), address) + wrapToBold(" passed")));
            } else {
                result.add(LogDto.of(i, wrapToHref(String.valueOf(800000 + i), address) + wrapToRed(" failed")));
            }

        }
        result.add(LogDto.of(idStart + 20 + 1, "LogId = " + (idStart + 21) + ". Finished at " + LocalDateTime.now().toLocalTime()));

        return result;
    }

    private String wrapToHref(String issueId, String address) {
        return "<a href='" + address + "/issues/" + issueId + "'>" + issueId + "</a>";
    }

    private String wrapToBold(String str) {
        return "<b>" + str + "</b>";
    }

    private String wrapToRed(String str) {
        return "<span><font color=\"red\">" + str + "</font></span>";
    }
}
