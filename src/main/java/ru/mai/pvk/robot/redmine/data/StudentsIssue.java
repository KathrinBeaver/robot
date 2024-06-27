package ru.mai.pvk.robot.redmine.data;

import lombok.Getter;
import lombok.Setter;

public class StudentsIssue {
    @Setter
    @Getter
    private int id;
    @Setter
    @Getter
    private String studentsName;
    private String issueTitle;

    public String getIssueName() {
        return issueTitle;
    }

    public void setIssueName(String issueName) {
        issueTitle = issueName;
    }

    @Override
    public String toString() {
        return "StudentsIssue{" +
                "id=" + id +
                ", studentsName='" + studentsName + '\'' +
                ", issueTitle='" + issueTitle + '\'' +
                '}';
    }
}
