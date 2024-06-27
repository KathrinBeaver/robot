package ru.mai.pvk.robot.redmine.taskCheckers;

import lombok.Getter;
import lombok.Setter;
import ru.mai.pvk.robot.redmine.data.utils.PvkLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskChecker {
    class FileAndItsTest {
        String fileName = "file.py";
        String testName = "test";
    }

    @Setter
    @Getter
    protected String subject;
    protected String fileToManage;
    protected String workingDir = ".\\temp\\";
    protected boolean isEasyMode;

    protected HashMap<String, String> knownTests;

    public void fillData() {
//        TODO:: тут поработать надо над способом хранения тестов
//        TasksXmlReader reader = new TasksXmlReader(".\\TestsInfo_v2.xml");
//        knownTests = reader.getAllTests().getOldStyleTasks();
    }

    public String getNameForKnownTest(String subject) {
        return getTestName(subject);
    }

    public String getNameForKnownTest() {
        return getTestName(getSubject());
    }

    public boolean isInEasyMode(){
        return isEasyMode;
    }

    String getFileToManage() {
        return fileToManage;
    }

    void setFileToManage(String fileToManage) {
        this.fileToManage = fileToManage;
    }

    String getTestName(String subjectFromIssue) {
        String testFolderName;
        testFolderName = knownTests.getOrDefault(subjectFromIssue, "");
        //еще одна попытка найти по неполному совпадению
        if (testFolderName.equals("")){
            for(String test : knownTests.keySet()){
                if (subjectFromIssue.toLowerCase().contains(test.toLowerCase()))
                {
                    testFolderName = knownTests.getOrDefault(test, "");
                }
            }
        }
        return testFolderName;
    }

    void removeTempFiles(FileAndItsTest data) {
        try {
            Files.delete(new File(workingDir + data.fileName).toPath());
            //Files.delete(new File(pyLintDir + data.fileTestName).toPath());
        } catch (IOException ex) {
            PvkLogger.getLogger(TaskChecker.class.getName()).error(ex.getMessage());
        }
    }

    FileAndItsTest copyFileToTempFolder(String fileToManage) {
        FileAndItsTest retVal = new FileAndItsTest();
        retVal.fileName = new File(fileToManage).toPath().getFileName().toString();
        try {
            Files.copy(new File(fileToManage).toPath(), new File(workingDir + retVal.fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            PvkLogger.getLogger(TaskChecker.class.getName()).error(ex.getMessage());
        }
        return retVal;
    }

}
