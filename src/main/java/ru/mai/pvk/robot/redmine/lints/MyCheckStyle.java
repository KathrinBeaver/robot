/*
 * To change this license header, choose License Headers in Project RedmineConnectionProperties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.mai.pvk.robot.redmine.lints;

import tools.ZipFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author user
 */
public class MyCheckStyle {
    private String workingDir = ".\\JavaDir\\";

    public MyCheckStyle() {
    }

    public void startCheckStyle(String attachmentName) {
        if (attachmentName.endsWith(".java")) {
            startCheckStyleSingleJavaFile(attachmentName);
        }

        if (attachmentName.endsWith(".zip")) {
            ZipFile zip = new ZipFile();
            File unzipTo = new File(Paths.get(workingDir.substring(2) + attachmentName.split(".zip")[0]).toUri());
            try {
                Files.createDirectories(unzipTo.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            boolean unzipResult = zip.unzipFileToDir(attachmentName, unzipTo);

            try (Stream<Path> walk = Files.walk(unzipTo.toPath())) {
                List<String> result = walk.map(x -> x.toString())
                        .filter(f -> f.endsWith(".java")).collect(Collectors.toList());
                File mainFile = null;
                boolean wasMainFound = false;
                for (String file : result) {
                    startCheckStyleSingleJavaFile(file);
                }
                collectLintErrors(attachmentName, result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void collectLintErrors(String attachmentName, List<String> resultFiles) {
        String errorReportFull = attachmentName + "_errorReport.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(errorReportFull, false));

            for (String file : resultFiles) {
                writer.write(Files.readString(Path.of(file + "_errorReport.txt")));
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startCheckStyleSingleJavaFile(String attachmentName) {
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    ".\\checkstyle\\checkstyle.bat", attachmentName);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private static Logger logger = Logger.getLogger(MyCheckStyle.class.getName());
}
