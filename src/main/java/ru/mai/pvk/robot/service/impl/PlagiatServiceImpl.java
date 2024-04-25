package ru.mai.pvk.robot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mai.pvk.robot.error.exception.TaskProccessException;
import ru.mai.pvk.robot.model.dto.*;
import ru.mai.pvk.robot.service.PlagiatService;
import ru.mai.pvk.robot.service.TaskService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlagiatServiceImpl implements PlagiatService {
    private final TaskService taskService;

    @Override
    public PlagiatDto getPlagiat(String taskId) {

        PlagiatDto result = PlagiatDto.of(taskService.getTaskDto(taskId),
                new HashMap<>());
        Map<String, List<UserPercentageDto>> temp = result.getStudentPlagiatPercentage();
        String ivanov = "Иванов Иван";
        String petrov = "Петров Петр";
        String kuznetsov = "Кузнецов Слава";


        temp.put(ivanov, new ArrayList<>());

        temp.put(petrov, new ArrayList<>());
        temp.put(kuznetsov, new ArrayList<>());

        temp.get(ivanov).add(UserPercentageDto.of(petrov, 0.2));
        temp.get(ivanov).add(UserPercentageDto.of(kuznetsov, 0.8));
        temp.get(petrov).add(UserPercentageDto.of(kuznetsov, 0.6));
        temp.get(petrov).add(UserPercentageDto.of(ivanov, 0.2));
        temp.get(kuznetsov).add(UserPercentageDto.of(ivanov, 0.8));
        temp.get(kuznetsov).add(UserPercentageDto.of(petrov, 0.6));

        return result;
    }

    @Override
    public PlagiatTwoIssuesComparisonDto getComparison(String taskId, String firstStudent, String secondStudent) {

        Map<String, String> twoIssuesComparison = new HashMap<>();
        //TODO: get Issue soulution for the first Student
        String firstSolution = """
                int k;
                int a = k + 10;
                int b = k + 20;
                System.out.println(b);
                """;
        //TODO: get Issue soulution for the second Student
        String secondSolution = """
                int m;
                int a = m + 10;
                int b = m + 20;
                System.out.println(a);
                """;
        twoIssuesComparison.put(firstStudent, firstSolution);
        twoIssuesComparison.put(secondStudent, secondSolution);

        PlagiatTwoIssuesComparisonDto result = PlagiatTwoIssuesComparisonDto.of(
                taskService.getTaskDto(taskId), twoIssuesComparison
        );
        return result;
    }
}
