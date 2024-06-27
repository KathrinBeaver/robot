package ru.mai.pvk.robot.redmine.data;

import com.taskadapter.redmineapi.bean.Issue;
import lombok.Getter;
import lombok.Setter;

public class ConfiguredTask {
    @Setter
    @Getter
    private Issue issue;
    @Setter
    @Getter
    private String taskCompleter;
    private boolean isNeededForceCheck;
    private boolean isLintRequired;
    @Setter
    @Getter
    private double requiredPythonRating;
    @Getter
    @Setter
    private int maxJavaLintErrors;
    private boolean isInEasyMode;

    @Setter
    @Getter
    private LintReportMode lintReportMode;

    public ConfiguredTask(Issue issue,
                          String taskCompleter,
                          boolean isNeededForceCheck,
                          boolean isLintRequired,
                          double requiredPythonRating,
                          int javaErrors,
                          boolean easyMode, LintReportMode lintReportMode) {
        this.isLintRequired = isLintRequired;
        this.isNeededForceCheck = isNeededForceCheck;
        this.issue = issue;
        this.requiredPythonRating = requiredPythonRating;
        this.taskCompleter = taskCompleter;
        this.maxJavaLintErrors = javaErrors;
        this.isInEasyMode = easyMode;
        this.lintReportMode = lintReportMode;
    }

    @Override
    public String toString() {
        return issue.toString() + " " + "Need Force: " + isNeededForceCheck +
                " Need Lint: " + isLintRequired + " PyLint Rating: " + requiredPythonRating +
                " Java Error Limit: " + maxJavaLintErrors +
                " Student:" + taskCompleter +
                " LintMode: " + lintReportMode;
    }

    public boolean isNeededForceCheck() {
        return isNeededForceCheck;
    }

    public void setNeededForceCheck(boolean neededForceCheck) {
        isNeededForceCheck = neededForceCheck;
    }

    public boolean isLintRequired() {
        return isLintRequired;
    }

    public void setLintRequired(boolean lintRequired) {
        isLintRequired = lintRequired;
    }

    public boolean isEasyMode() {
        return isInEasyMode;
    }
}
