package ru.mai.pvk.robot.redmine.data;

import com.taskadapter.redmineapi.bean.Issue;
import lombok.Getter;
import lombok.Setter;
import ru.mai.pvk.robot.redmine.data.utils.Translit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TaskInfo {
    @Setter
    @Getter
    private String taskId;
    @Setter
    @Getter
    private String taskName;
    @Setter
    @Getter
    private String taskBody;
    @Setter
    @Getter
    private String taskPath;
    @Setter
    @Getter
    private String iterationPath;
    private List<String> allAvailableTests = new ArrayList<String>();

    public TaskInfo() {
        taskId = "";
        taskName = "";
        taskBody = "";
        taskPath = "";
        iterationPath = "";
    }

    public TaskInfo(Issue issue) {
        this.taskBody = issue.getDescription();
        this.iterationPath = issue.getTargetVersion().getName();
        // ?? this.taskId
        this.taskName = issue.getSubject();
        this.taskPath = Translit.toTranslit(issue.getSubject());
    }

    public TaskInfo(TaskInfo otherTask) {
        this.taskBody = otherTask.taskBody;
        this.taskPath = otherTask.taskPath;
        this.taskName = otherTask.taskName;
        this.iterationPath = otherTask.iterationPath;
        this.taskId = otherTask.taskId;
        this.allAvailableTests = otherTask.allAvailableTests;
    }

    public void addTest(String testName) {
        allAvailableTests.add(testName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other instanceof TaskInfo) {
            return taskId.equals(((TaskInfo) other).getTaskId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return taskId.hashCode();
    }

//    public String getTestContent(String value, Boolean needAnswer) {
//        String testFileName = allAvailableTests.stream().filter(item -> item.equals(value)).findFirst().get();
//        if (needAnswer) {
//            testFileName = testFileName.replace(".t", ".a");
//        }
//
//        try {
//            return Files.readString(Path.of(TasksKeeper.PathToTests, taskPath, testFileName));
//        } catch (IOException e) {
//            logger.info(e.toString());
//        }
//
//        return ((needAnswer) ? "answer" : "test") + " not found";
//    }

    public void setTests(List<String> tests) {
        allAvailableTests.clear();
        for (String testName : tests) {
            allAvailableTests.add(testName);
        }
    }

//    public void setTestInput(String testFileName, String newValue) {
//        try {
//            Files.writeString(Path.of(TasksKeeper.PathToTests, taskPath, testFileName), newValue);
//        } catch (IOException e) {
//            logger.info(e.toString());
//        }
//    }

//    public void setTestOutput(String testFileName, String newValue) {
//        try {
//            Files.writeString(
//                    Path.of(TasksKeeper.PathToTests, taskPath, testFileName.replace(".t", ".a")),
//                    newValue);
//        } catch (IOException e) {
//            logger.info(e.toString());
//        }
//    }

    public void saveAllData() {

    }

    public String toString() {
        return taskId + " - " + iterationPath + " - " + taskName;
    }

    private static Logger logger = Logger.getLogger(TaskInfo.class.getSimpleName());
}
