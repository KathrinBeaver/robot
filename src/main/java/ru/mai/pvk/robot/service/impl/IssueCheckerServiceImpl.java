package ru.mai.pvk.robot.service.impl;

import com.taskadapter.redmineapi.RedmineException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.model.dto.*;
import ru.mai.pvk.robot.property.RobotProperties;
import ru.mai.pvk.robot.redmine.ConnectionWithRedmine;
import ru.mai.pvk.robot.securingweb.security.domain.model.User;
import ru.mai.pvk.robot.securingweb.security.service.UserService;
import ru.mai.pvk.robot.service.IssueCheckerService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class IssueCheckerServiceImpl implements IssueCheckerService {
    private final RobotProperties robotProperties;
    private final ConnectionWithRedmine connection;
    private final UserService userService;
    private String apikey;

    @Override
    public void startSingleIssueCheck(IssueCheckerDto settings) {

    }

    @Override
    public void startFullCheck(IssueCheckerDto aSettings) {

    }

    @Override
    public StudentListDto getStudentsList(String projectId) {
        StudentListDto students = StudentListDto.of(new ArrayList<>());
        apikey = userService.getCurrentUser().getRedmineApiKey();
        initConnection(projectId, apikey);
        students.getStudentList().addAll(connection.getProjectUsers());

        return students;
    }


    @Override
    public IssueListDto getTasksList(String projectId, String iterationId) {
        apikey = userService.getCurrentUser().getRedmineApiKey();
        initConnection(projectId, apikey);
        List<IssueDto> tasksToAdd = connection.getIterationFreeTasks(iterationId);
        IssueListDto allTasks = IssueListDto.of(new ArrayList<>());

        allTasks.getIssueList().addAll(tasksToAdd);

        return allTasks;
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

    private void initConnection(String projectId, String apikey) {
        if (!connection.isInitialized()) {
            try {
                connection.init(robotProperties.getUrl(), apikey, projectId);
            } catch (RedmineException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
