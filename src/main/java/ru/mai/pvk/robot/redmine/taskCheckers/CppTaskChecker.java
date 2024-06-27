package ru.mai.pvk.robot.redmine.taskCheckers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CppTaskChecker extends TaskChecker {

    public CppTaskChecker(String subject, String fileToManage, boolean easyMode) {
        this.setSubject(subject);
        this.setFileToManage(fileToManage);
        this.isEasyMode = easyMode;
        this.workingDir = ".\\cpplint\\";
    }

    public static void main(String[] args) {
        CppTaskChecker checker = new CppTaskChecker("Шашки", "myFiles/1.cpp", true);
        FileAndItsTest fileAndItsTest = checker.copyFileToTempFolder(checker.getFileToManage());
        fileAndItsTest.testName = "checkers";
        StringBuilder sb = checker.cppCompileSingleCppFile(fileAndItsTest);
        System.out.println(sb.toString());
    }

    public String startCppCheck() {
        FileAndItsTest fileAndItsTest = copyFileToTempFolder(getFileToManage());
        fileAndItsTest.testName = getNameForKnownTest();
        StringBuilder sb = cppCompileSingleCppFile(fileAndItsTest);
        return sb.toString();
    }

    private StringBuilder cppCompileSingleCppFile(FileAndItsTest data) {
        StringBuilder sb = new StringBuilder();
        String cppFileToCompile = data.fileName;
        if (!Files.exists(Paths.get(cppFileToCompile))) {
            cppFileToCompile = workingDir + data.fileName;
        }
        if (!Files.exists(Paths.get(cppFileToCompile))) {
            sb.append("Failed: File not found!");
            return sb;
        }
        try {
            Path pathFrom = new File(cppFileToCompile).toPath();
            data.fileName = pathFrom.toAbsolutePath().toString();

            String cLangExec = new File(workingDir.substring(2) + "clang++.exe").getAbsolutePath();

            ProcessBuilder builder = new ProcessBuilder(cLangExec,
                    data.fileName,
                    "-o",
                    data.fileName + ".exe");
            builder.redirectErrorStream(true);
            Process p = builder.start();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line + "\n");
            }
            if (p.exitValue() == 0) {
                runTestsForBinaryFile(sb, data);
            }
        } catch (IOException ex) {
            Logger.getLogger(CppTaskChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        //removeTempFiles(data);
        return sb;
    }

    private void runTestsForBinaryFile(StringBuilder sb, TaskChecker.FileAndItsTest data) {
        try {
            String relative = new File(workingDir).toURI().relativize(
                    new File(data.fileName).toURI()).getPath() + ".exe";

            ProcessBuilder builder = new ProcessBuilder("python.exe",
                    "cppTestRunner.py", relative,
                    data.testName,
                    isInEasyMode() ? "True" : "");
            builder.redirectErrorStream(true);
            builder.directory(new File(workingDir));
            Process p = builder.start();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream(), "cp1251"));
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(CppTaskChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
