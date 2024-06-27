/*
 * To change this license header, choose License Headers in Project RedmineConnectionProperties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.mai.pvk.robot.redmine.taskCheckers;

import ru.mai.pvk.robot.redmine.data.utils.PvkLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergeyp
 */
public class PyTaskChecker extends TaskChecker {

    public PyTaskChecker(String subject, String fileToManage, boolean isEasyMode) {
        this.setSubject(subject);
        this.setFileToManage(fileToManage);
        workingDir = ".\\Pylint\\";
        this.isEasyMode = isEasyMode;
    }

    public PyTaskChecker(String subject) {
        this(subject, "file.py", false);
    }

    public static void main(String[] args) {
        PyTaskChecker checker = new PyTaskChecker("Школы с наибольшим числом участников олимпиады", "pyLint/myFile.py", true);
        checker.startPyCheck();
    }

    public String startPyCheck() {
        return startPyCheck(this.getSubject(), this.getFileToManage(), this.isInEasyMode());
    }


    public String startPyCheck(String subject, String fileToManage, boolean isInEasyMode) throws UnsupportedOperationException {

        StringBuilder sb = new StringBuilder();
        FileAndItsTest data = copyFileToTempFolder(fileToManage);
        data.testName = getTestName(subject);

        if (data.testName.isEmpty()) {
            throw new UnsupportedOperationException("Not supported");
        }

        try {
            ProcessBuilder builder = new ProcessBuilder("python.exe",
                    "pyTestRunner.py", data.fileName, data.testName, isEasyMode ? "True" : "");
            builder.redirectErrorStream(true);
            builder.directory(new File(workingDir));
            Process p = builder.start();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException ex) {
            PvkLogger.getLogger(PyTaskChecker.class.getName()).error(ex.getMessage());
        }
        removeTempFiles(data);

        return sb.toString();
    }


    public String getTestName2(String subject) {
        String testFolderName = "";
//        XmlReader reader = new XmlReader(".\\pylint\\TestsInfo.xml");
//        testFolderName = reader.getTestFolderBySubject(subject);
        return testFolderName;
    }
}
