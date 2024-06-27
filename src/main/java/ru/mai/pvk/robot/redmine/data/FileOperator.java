package ru.mai.pvk.robot.redmine.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class FileOperator {
    private String outputFile = "контрольная_работа_2.txt";

    public FileOperator(String outputFile) {
        this.outputFile = outputFile;
    }

    public void saveDataToFile(HashMap<String, ArrayList<String>> issuesOfTheStudent) {
        int max = 0;
        StringBuilder sb = new StringBuilder();
        for (String key : issuesOfTheStudent.keySet()) {
            if (issuesOfTheStudent.get(key).size() > max) {
                max = issuesOfTheStudent.get(key).size();
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            sb.append("Total tasks ").append(max).append("\n");
            for (String key : issuesOfTheStudent.keySet().stream()
                    .sorted((x, y) -> x.split(" ")[1].compareTo(y.split(" ")[1]))
                    .toList()) {
                ArrayList<String> stIssues = issuesOfTheStudent.get(key);
                sb.append(key).append(" - ")
                        .append(stIssues.size())
                        .append(" (Mark  ")
                        .append(Math.round((stIssues.size() * 1000.) / max) / 100.).append(" )\n");
            }

            sb.append("===========================\n");
            for (String key : issuesOfTheStudent.keySet()) {
                ArrayList<String> stIssues = issuesOfTheStudent.get(key);
                sb.append(key + "(Total " + stIssues.size() + "): \n");
                stIssues.forEach(issue -> sb.append("\t").append(issue).append("\n"));
            }
            
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }
}
