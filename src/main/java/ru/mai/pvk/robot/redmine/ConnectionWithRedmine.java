/*
 * To change this license header, choose License Headers in Project RedmineConnectionProperties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.mai.pvk.robot.redmine;

import com.taskadapter.redmineapi.*;
import com.taskadapter.redmineapi.bean.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.Charsets;
import org.apache.http.entity.ContentType;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.mai.pvk.robot.model.dto.StudentDto;
import ru.mai.pvk.robot.redmine.data.ConfiguredTask;
import ru.mai.pvk.robot.redmine.data.LintReportMode;
import ru.mai.pvk.robot.redmine.data.TaskInfo;
import ru.mai.pvk.robot.redmine.data.utils.PvkLogger;
import ru.mai.pvk.robot.redmine.data.utils.TextUtils;
import ru.mai.pvk.robot.redmine.data.utils.Translit;
import ru.mai.pvk.robot.redmine.lints.MyCppLint;
import ru.mai.pvk.robot.redmine.lints.MyPylint;
import ru.mai.pvk.robot.redmine.lints.PhrasesGenerator;
import ru.mai.pvk.robot.redmine.taskCheckers.CppTaskChecker;
import ru.mai.pvk.robot.redmine.taskCheckers.JavaTaskChecker;
import ru.mai.pvk.robot.redmine.taskCheckers.PyTaskChecker;
import ru.mai.pvk.robot.redmine.lints.MyCheckStyle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static ru.mai.pvk.robot.redmine.data.constants.TaskStatus.STATUS_ID_APPROVED;
import static ru.mai.pvk.robot.redmine.data.constants.TaskStatus.STATUS_ID_CLOSED;

/**
 * @author Zerg0s
 */
@Component
public class ConnectionWithRedmine {

    static final String myFilesDir = ".\\myFiles\\";

    boolean returnBackIfAllOk = true;
    @Setter
    boolean lint = true;
    @Setter
    @Getter
    private String url;
    @Setter
    @Getter
    private String apiAccessKey;
    @Getter
    private String projectKey;
    private Integer queryId = null;
    @Setter
    private Integer issueStatus;
    @Setter
    @Getter
    private String studentName = "";
    @Setter
    @Getter
    private String professorName = "";
    @Setter
    private String assigneeName = "";

    @Getter
    private RedmineManager redmineManager;

    @Getter
    private IssueManager issueManager;
    private AttachmentManager attachmentManager;
    private List<Issue> issues;
    @Getter
    private ProjectManager projectManager;
    private List<Project> projects;
    private UserManager userManager;
    private List<User> users;
    private List<Membership> projectsUsers;
    private List<Version> versions;

    public ConnectionWithRedmine() {

    }

    public void init(String url, String apikey, String projectId) throws RedmineException {
        this.url = url;
        this.projectKey = projectId;
        this.apiAccessKey = apikey;

        this.redmineManager = RedmineManagerFactory.createWithApiKey(url, apiAccessKey);
        this.issueManager = redmineManager.getIssueManager();
        this.attachmentManager = redmineManager.getAttachmentManager();
        this.projectManager = redmineManager.getProjectManager();
        this.userManager = redmineManager.getUserManager();

        getProjectDetails(projectKey);
    }

    public void setReturnBackIfAllOk(Boolean value) {
        this.returnBackIfAllOk = value;
    }

    public void setIssueAssigneeNameForIssue(Issue issue, String userName) {
        int id = getUserId(userName);
        if (id == 0) {
            logger.warning("Can't find user " + userName);
        } else {
            issue.setAssigneeId(id);
            issue.setAssigneeName(userName);
        }
    }

    public void checkIssueAttachments(ConfiguredTask task) {
        Issue issue = task.getIssue();
        Collection<Attachment> issueAttachment = issue.getAttachments();
        List<Attachment> issueAttachments = new ArrayList<Attachment>(issueAttachment);
        new File(myFilesDir).mkdirs();

        //Check only latest attach, the rest are already history
        Optional<Attachment> nullableAttach = getLatestCheckableAttach(issueAttachments);
        Attachment attach;
        if (nullableAttach.isEmpty()) {
            logger.warning("Can't find suitable attaches for ");
            //logger.logHtmlIssueLink(task.getIssue(), url + "/issues/" + task.getIssue().getId());
            logger.info("");
            return;
        }

        attach = nullableAttach.get();
        if (!TextUtils.isNullOrEmpty(attach.getAuthor().getFullName())) {
            task.setTaskCompleter(attach.getAuthor().getFullName());
        }
        checkSingleFileAttachment(attach, task);
    }

    private void checkSingleFileAttachment(Attachment attach, ConfiguredTask task) {
        String attachFileName = attach.getFileName();
        Issue issue = task.getIssue();

        if (isKnownAttachExtention(attachFileName)) {
            int wasCheckedEarlier = -1;
            //Если не надо перепроверять ранее проверенные, смотрим, не нашлось ли ранее проверенных
            if (!task.isNeededForceCheck()) {
                try {
                    wasCheckedEarlier = isAttachmentIdWasEarlierChecked(attach.getId());
                } catch (IOException ex) {
                    logger.error(ex.toString());
                }
            }

            if (!task.isNeededForceCheck() && wasCheckedEarlier == 1) {
                logger.warning("Skipping the issue.");
                logger.info(" This version was already checked.");
            }

            //Проверяем файл аттача: 1)если не проверяли ранее 0) надо обязательно проверять
            if (task.isNeededForceCheck() || wasCheckedEarlier == 0) {
                String fileToManage = myFilesDir + makeUsableFileName(
                        attachFileName,
                        attach.getAuthor().getFullName(),
                        issue.getSubject());
                try {
                    downloadAttachments(attach.getContentURL(), apiAccessKey, fileToManage);
                } catch (IOException ex) {
                    logger.warning(ex.toString());
                }

                if (attachFileName.endsWith(".py")) {
                    processPythonFile(task, issue, fileToManage);
                }

                if (attachFileName.endsWith(".zip") || attachFileName.endsWith(".java")) {
                    processJavaFile(task, issue, fileToManage);
                }

                if (attachFileName.endsWith(".cpp")) {
                    processCppFile(task, fileToManage);
                }
            }
        }
    }

    public Optional<Attachment> getLatestCheckableAttach(List<Attachment> issueAttachments) {
        if (issueAttachments == null && issueAttachments.isEmpty()) {
            return Optional.empty();
        }
        long idMax = 0;
        int maxIndex = 0;
        for (int i = 0; i < issueAttachments.size(); i++) {
            if (isKnownAttachExtention(issueAttachments.get(i).getFileName()) &&
                    issueAttachments.get(i).getId() > idMax) {
                idMax = issueAttachments.get(i).getId();
                maxIndex = i;
            }
        }
        if (idMax == 0) {
            return Optional.empty();
        }
        return Optional.of(issueAttachments.get(maxIndex));
    }

    private void processJavaFile(ConfiguredTask task, Issue issue, String fileToManage) {
        int processResult = -1;
        boolean javaLintResult = true;
        if (task.isLintRequired()) {
            javaLintResult = doJavaLint(task, fileToManage);
        }
        //if javaLint was OK or not required
        if (javaLintResult) {
            JavaTaskChecker javaChecker = new JavaTaskChecker(issue.getSubject(), fileToManage, task.isEasyMode());
            String testFolder = javaChecker.getNameForKnownTest();
            if (!testFolder.isBlank()) {
                processResult = doJavaTaskCheck(javaChecker, issue);
            }
        } else {
            String student = task.getTaskCompleter();
            this.setIssueAssigneeNameForIssue(issue, student);
        }

        processResult(task, processResult);
    }


    private void processCppFile(ConfiguredTask task, String fileToManage) {
        int processResult = -1;
        boolean cppLintResult = true;
        Issue issue = task.getIssue();

        if (task.isLintRequired()) {
            cppLintResult = doCppLint(fileToManage, task);
        }

        //if javaLint was OK or not required
        if (cppLintResult) {
            CppTaskChecker cppTaskChecker = new CppTaskChecker(issue.getSubject(), fileToManage, task.isEasyMode());
            String testFolder = cppTaskChecker.getNameForKnownTest();
            if (!testFolder.isBlank()) {
                processResult = doCppTaskCheck(cppTaskChecker, issue);
            }
        } else {
            String student = task.getTaskCompleter();
            this.setIssueAssigneeNameForIssue(issue, student);
        }

        processResult(task, processResult);
    }

    private void processResult(ConfiguredTask task, int processResult) {
        Issue issue = task.getIssue();
        if (processResult != -1) {
            logger.warning(TextUtils.getStringResult(processResult));
            logger.info(" (after tests - back to Student) - " + task.getTaskCompleter());
            this.setIssueAssigneeNameForIssue(issue, task.getTaskCompleter());
            if (processResult == 1 && this.returnBackIfAllOk) {
                issue.setStatusId(STATUS_ID_CLOSED);
            }

            if (processResult == 1 && !this.returnBackIfAllOk) {
                issue.setStatusId(STATUS_ID_APPROVED);
            }
        }

        this.updateIssue(issue);
    }

    private void processPythonFile(ConfiguredTask task, Issue issue, String fileToManage) {
        int processResult = -1;
        boolean pyLintResult = true;
        if (task.isLintRequired()) {
            pyLintResult = doPyLint(task, fileToManage);
        }
        if (pyLintResult) {
            PyTaskChecker pyChecker = new PyTaskChecker(issue.getSubject(), fileToManage, task.isEasyMode());
            String testFolder = pyChecker.getNameForKnownTest();
            if (!testFolder.isEmpty()) {
                processResult = doPyTaskCheck(pyChecker, issue);
            }
        }

        processResult(task, processResult);
    }

    private boolean isKnownAttachExtention(String attachName) {
        return attachName.endsWith(".py") || attachName.endsWith(".java")
                || attachName.endsWith(".zip") || attachName.endsWith(".cpp");
    }

    private void doCppLint(Attachment attach, ConfiguredTask task) {
        String fileToManage = myFilesDir + makeUsableFileName(
                attach.getFileName(),
                attach.getAuthor().getFullName(),
                task.getIssue().getSubject());
        doCppLint(fileToManage, task);
    }

    private boolean doCppLint(String fullFileName, ConfiguredTask task) {
        new MyCppLint().startCpplint(fullFileName);
        String[] allLines = TextUtils.readReportFile(fullFileName + "_errorReport.txt");
        String lastLine = allLines[allLines.length - 1];
        String notesForIssue = "";

        int studentCppErrorAmount = TextUtils.cppErrorAmountDetectionInFile(lastLine);
        if (studentCppErrorAmount > task.getMaxJavaLintErrors()) {
            if (task.getLintReportMode().getModeNumber() != LintReportMode.NIGHTMARE_MODE) {
                if (task.getLintReportMode().getModeNumber() == LintReportMode.DEFAULT_MODE) {
                    notesForIssue = TextUtils.getPrettyErrorsCpp(allLines);
                    this.uploadAttachment(task.getIssue(), fullFileName + "_errorReport.txt");
                } else {
                    notesForIssue = lastLine;
                }
            }
            task.getIssue().setNotes(notesForIssue + "\nSome corrections are required.");
            this.updateIssue(task.getIssue());
            return false;
        } else {
            task.getIssue().setNotes(generateSuccessMsg());
            this.updateIssue(task.getIssue());
            return true;
        }
    }

    private void doJavaLint(String fullFileName) {
        doJavaLint(null, fullFileName);
    }

    private boolean doJavaLint(ConfiguredTask task, String fullFileName) {
        Boolean lintResult = false;
        new MyCheckStyle().startCheckStyle(fullFileName);
        String[] allLines = TextUtils.readReportFile(fullFileName + "_errorReport.txt");
        String lastLine = getStringWithErrorAmmountJava(allLines);
        String notesForIssue = "";
        int studentJavaErrorAmount = TextUtils.javaErrorAmountDetectionInFile(lastLine);

        if (studentJavaErrorAmount > task.getMaxJavaLintErrors()) {
            if (task != null) {
                if (task.getLintReportMode().getModeNumber() != LintReportMode.NIGHTMARE_MODE) {
                    if (task.getLintReportMode().getModeNumber() == LintReportMode.DEFAULT_MODE) {
                        notesForIssue = TextUtils.getPrettyErrorsJava(allLines);
                        this.uploadAttachment(task.getIssue(), fullFileName + "_errorReport.txt");
                    } else {
                        notesForIssue = lastLine;
                    }
                }
                task.getIssue().setNotes(notesForIssue + "\nSome corrections are required.");
            }

            lintResult = false;
        } else {
            task.getIssue().setNotes(generateSuccessMsg());
            lintResult = true;
        }

        return lintResult;
    }

    private String getStringWithErrorAmmountJava(String[] allLines) {
        String checkStyle = "Checkstyle ends with";
        int countErrors = 0;
        for (String line : allLines) {
            if (line.contains(checkStyle)) {
                countErrors += TextUtils.javaErrorAmountDetectionInFile(line);
            }
        }
        return String.format(checkStyle + " %d errors.", countErrors);
    }

    private boolean doPyLint(ConfiguredTask task, String fileToManage) {
        new MyPylint().startPylint(fileToManage);
        String attachName = fileToManage + "_errorReport.txt";
        String[] reportLines = TextUtils.readReportFile(attachName);
        String lastLineInReport = reportLines[reportLines.length - 1];
        String notesForIssue = "";

        double studentPythonRating = 0;
        try {
            studentPythonRating = TextUtils.pythonRatingCheck(lastLineInReport);
        } catch (Exception ex) {
            studentPythonRating = -200;
        }
        boolean isPLintSuccessful = false;

        if (task.getRequiredPythonRating() > studentPythonRating) {
            String studentsName = getStudentsName(task.getIssue().getId(), this.professorName);
            if (studentsName.isBlank()) {
                studentsName = professorName;
            }
            logger.warning("Failed on Lint. ");
            logger.info("(PyLint - assigning back to Student) - " + studentsName + " " + lastLineInReport);
            this.setIssueAssigneeNameForIssue(task.getIssue(), studentsName);
            lastLineInReport = TextUtils.generateErrorMsg(task, lastLineInReport);

            if (task.getLintReportMode().getModeNumber() == LintReportMode.DEFAULT_MODE) {
                notesForIssue = TextUtils.getPrettyErrorsPython(reportLines);
                lastLineInReport = String.format("Error(s): %d", notesForIssue.split("\n").length - 2);
                this.uploadAttachment(task.getIssue(), attachName);
            }
        } else {
            lastLineInReport = "\n" + generateSuccessMsg();
            isPLintSuccessful = true;
        }
        notesForIssue += lastLineInReport;
        task.getIssue().setNotes(notesForIssue);
        this.updateIssue(task.getIssue());

        return isPLintSuccessful;
    }

    public void downloadAttachments(String url, String apikey, String fileName) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-redmine-api-key", apikey)
                .addHeader("cache-control", "no-cache") //не обязательно
                .build();

        Response response = client.newCall(request).execute();

        try (InputStream in = response.body().byteStream()) {
            Path to = Paths.get(fileName); //convert from String to Path
            Files.copy(in, to, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public List<Issue> getIssues() throws RedmineException {
        issues = issueManager.getIssues(projectKey, queryId, Include.journals, Include.attachments, Include.changesets);
        return issues;
    }

    private List<Issue> getIssues(String iterationName, boolean openOrClosed) throws RedmineException {
        String iterationid = getIterationIdByName(iterationName);
        boolean hasMoreIssues = true;
        int offset = 0;
        List<Issue> tmpList = new ArrayList<>();
        while (hasMoreIssues) {
            final Map<String, String> params = new HashMap<>();
            params.put("project_id", projectKey);
            params.put("fixed_version_id", iterationid);
            params.put("limit", "100");
            params.put("offset", String.valueOf(offset));
            params.put("include", "attachments, journals");
            if (!openOrClosed) {
                params.put("status_id", "5");
            }
            List<Issue> current = issueManager.getIssues(params).getResults();
            tmpList.addAll(current);
            if (current.size() == 0) {
                hasMoreIssues = false;
            }
            offset += 100;
        }

        issues = tmpList;
        return issues;
    }

    public List<Issue> getOpenedIssues(String iterationName) throws RedmineException {
        return getIssues(iterationName, true);
    }

    public List<Issue> getClosedIssues(String iterationName) {
        try {
            return getIssues(iterationName, false);
        } catch (RedmineException e) {
            logger.info(e.getMessage());
        }
        return new ArrayList<Issue>();
    }

    public List<Issue> getUniqueIssues(String iterationName) {
        List<Issue> uniqueIssues = new ArrayList<>();
        Set<String> set = new HashSet<>();
        try {
            uniqueIssues.addAll(getClosedIssues(iterationName));
            uniqueIssues.addAll(getOpenedIssues(iterationName));
            uniqueIssues.removeIf(issue -> !set.add(issue.getSubject()));
        } catch (RedmineException e) {
            logger.info(e.getMessage());
        }

        return uniqueIssues;
    }

    public List<Issue> getMyIssues(String iterationName) throws RedmineException {
        final Map<String, String> params = new HashMap<>();

        params.put("project_id", projectKey);
        if (!Objects.equals(iterationName, "")) {
            params.put("fixed_version_id", getIterationIdByName(iterationName));
        }

        params.put("limit", "100");
        params.put("assigned_to_id", "me");
        params.put("include", "attachments, journals");
        issues = issueManager.getIssues(params).getResults();
        return issues;
    }

    private String getIterationIdByName(String iterationName) {
        String retVal = "";
        int projectKeyInt = 0;
        List<Version> allVersions = null;
        try {
            projectKeyInt = redmineManager.getProjectManager().getProjectByKey(projectKey).getId();
            allVersions = this.redmineManager.getProjectManager().getVersions(projectKeyInt);
            for (Version version : allVersions) {
                if (version.getName().equals(iterationName)) {
                    retVal = version.getId().toString();
                    break;
                }
            }
        } catch (RedmineException ex) {
            logger.error(ex.toString());
        }

        return retVal;
    }

    public Collection<String> getVersions(String projectKey) {
        List<Version> allVersions = new ArrayList<>();
        try {
            allVersions = projectManager.getVersions(projectManager.getProjectByKey(projectKey).getId());
        } catch (RedmineException e) {
            logger.info(e.getMessage());
        }
        return allVersions.stream().map(v -> v.getName()).collect(Collectors.toList());
    }

    public Collection<String> getVersions() {
        return getVersions(projectKey);
    }

    public Issue getIssueById(Integer issueID) throws RedmineException {
        return issueManager.getIssueById(issueID, Include.journals, Include.attachments);
    }

    public void uploadAttachment(Issue issue, String path) {
        try {
            File file = new File(path);
            attachmentManager.addAttachmentToIssue(issue.getId(), file, ContentType.TEXT_PLAIN.getMimeType());
        } catch (RedmineException | IOException | IllegalArgumentException ex) {
            logger.info(ex.toString());
        }
    }

    public int isAttachmentIdWasEarlierChecked(Integer id) throws IOException {
        List<String> attachmentIDs = new ArrayList<String>();
        Path path = Paths.get("AttachmentID.txt");
        attachmentIDs = Files.readAllLines(path, StandardCharsets.UTF_8);
        int response = 0;
        int attachWasCheckedBefore = 1;
        int attachIsNew = 0;

        for (String attach : attachmentIDs) {
            if (attach.trim().equals("")) {
                continue;
            }
            long idFromFile = Long.parseLong(attach);
            if (idFromFile == id) {
                response = attachWasCheckedBefore;
                break;
            } else {
                response = attachIsNew;
            }
        }
        if (response == attachIsNew) {
            String fromIntToString = id + "\r\n";
            Files.write(path, fromIntToString.getBytes(), StandardOpenOption.APPEND);
        }
        return response;
    }

    private void removeDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
        }

        dir.delete();
    }

    private void cleanDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
        }
    }

    public void updateIssue(Issue issue) {
        try {
            redmineManager.getIssueManager().update(issue);
        } catch (RedmineException ex) {
            logger.info(ex.toString());
        } catch (IllegalArgumentException ex) {
        }
    }

    public ArrayList<StudentDto> getProjectUsers() {
        MembershipManager membershipManager = redmineManager.getMembershipManager();
        List<Role> roles;
        List<Membership> memberships = new ArrayList<>();
        try {
            memberships = membershipManager.getMemberships(projectKey);
        } catch (RedmineException ex) {
            logger.info(ex.toString());
        }

        ArrayList<StudentDto> retArray = new ArrayList<>();
        for (Membership m : memberships) {
            retArray.add(StudentDto.of(m.getUserId().toString(), m.getUserName()));
        }

        return retArray;
    }

    public ArrayList<String> getIterationFreeTasks(String iteration) {
        List<Issue> issues = new ArrayList<>();
        try {
            issues = getOpenedIssues(iteration);
        } catch (RedmineException ex) {
            logger.info(ex.toString());
        }
        ArrayList<String> retVal = new ArrayList<>();
        for (Issue issue : issues) {
            String assigneeName = issue.getAssigneeName();
            if (assigneeName == null || assigneeName.equalsIgnoreCase("")) {
                retVal.add(issue.getId() + ": " + issue.getSubject());
            }
        }
        return retVal;
    }

    public void copyAndAssignIssue(int issueId, String nameTo) {
        Issue currIssue = new Issue();
        try {
            currIssue = getIssueById(issueId);
        } catch (RedmineException ex) {
            logger.error(ex.toString());
        }
        Issue newIssue = new Issue();
        newIssue.setSubject(currIssue.getSubject());
        newIssue.setTargetVersion(currIssue.getTargetVersion());
        newIssue.setCategory(currIssue.getCategory());
        newIssue.setProjectId(currIssue.getProjectId());
        newIssue.setPrivateIssue(true);
        newIssue.setAssigneeName(nameTo);
        newIssue.setDescription(currIssue.getDescription());
        setIssueAssigneeNameForIssue(newIssue, nameTo);

        try {
            redmineManager.getIssueManager().createIssue(newIssue);
        } catch (RedmineException ex) {
            logger.info(ex.toString());
        }
    }

    public void createNewIssueToRedmine(TaskInfo task) {
        try {
            Issue newIssue = new Issue();
            newIssue.setSubject(task.getTaskName());
            newIssue.setTargetVersion(versions.stream()
                    .filter(v -> v.getName().equals(task.getIterationPath()))
                    .findFirst().get());
            //newIssue.setCategory(issueManager.getCategories().stream().findFirst());
            newIssue.setProjectId(projectManager.getProjectByKey(projectKey).getId());
            newIssue.setPrivateIssue(true);
            newIssue.setAssigneeName("");
            newIssue.setDescription(task.getTaskBody());
            redmineManager.getIssueManager().createIssue(newIssue);
        } catch (RedmineException ex) {
            logger.info(ex.getMessage());
        }
    }

    private String getStudentsName(int id, String managerName) {
        String name = new RedmineAlternativeReader(url, this.apiAccessKey).getStudentsName(id, managerName);
        return name;
    }

    private String getStudentsName(int id) {
        return getStudentsName(id, professorName);
    }

    private String generateSuccessMsg() {
        String succ = "First step. Checking style...\n";
        return succ + new PhrasesGenerator().getSuccessRandomPhrase() + " Waiting for further checks.";
    }

    //тут надо что-то сделать с этим файлом: убить кириллицу и пробелы,
    // оставив только латиницу
    public String makeUsableFileName(String fileName, String author, String taskTitle) {
        fileName = (Translit.toTranslit(author.toLowerCase())
                + "_" + Translit.toTranslit(taskTitle.toLowerCase())
                + "_" + fileName.toLowerCase())
                .replaceAll(" ", "");
        StringBuilder retStr = new StringBuilder();

        for (int i = 0; i < fileName.length(); i++) {
            if (fileName.charAt(i) >= 'a' && fileName.charAt(i) <= 'z'
                    || fileName.charAt(i) == '.'
                    || fileName.charAt(i) == '_') {
                retStr.append(fileName.charAt(i));
            }
        }

        //Защита от совсем левака
        if (retStr.toString().equalsIgnoreCase(".py")) {
            retStr = new StringBuilder("task.py");
        }
        return retStr.toString();
    }

    private int doPyTaskCheck(PyTaskChecker checker, Issue issue) {
        String result = "";
        try {
            result = checker.startPyCheck();
        } catch (UnsupportedOperationException ex) {
            return -1;
        }
        int success = 0;
        String[] splittedResult = result.split("\n");
        if (splittedResult[splittedResult.length - 1].toLowerCase().equals("passed")) {
            issue.setNotes("All tests passed");
            success = 1;
        } else {
            issue.setNotes("<pre>" + result + "</pre>");
        }
        return success;
    }

    private int doJavaTaskCheck(JavaTaskChecker checker, Issue issue) {
        String result = "";
        try {
            result = checker.startJavaCheck();
        } catch (UnsupportedOperationException ex) {
            return -1;
        }
        int success = 0;
        String[] splittedResult = result.split("\n");
        if (splittedResult[splittedResult.length - 1].equalsIgnoreCase("passed")) {
            issue.setNotes("All tests passed");
            success = 1;
        } else {
            issue.setNotes("<pre>" + result + "</pre>");
        }
        return success;
    }

    private int doCppTaskCheck(CppTaskChecker checker, Issue issue) {
        String result = "";
        try {
            result = checker.startCppCheck();
        } catch (UnsupportedOperationException ex) {
            return -1;
        }
        int success = 0;
        String[] splittedResult = result.split("\n");
        if (splittedResult[splittedResult.length - 1].equalsIgnoreCase("passed")) {
            issue.setNotes("All tests passed");
            success = 1;
        } else {
            issue.setNotes("<pre>" + result + "</pre>");
        }
        return success;
    }

    public Issue getIssueWithJournals(Integer issueId) {
        Issue issue = null;
        try {
            issue = issueManager.getIssueById(issueId, Include.journals);
        } catch (RedmineException e) {
            e.printStackTrace();
        }
        return issue;
    }

    public boolean getLint() {
        return lint;
    }

    private static final PvkLogger logger = PvkLogger.getLogger(ConnectionWithRedmine.class.getSimpleName(), false);

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
        try {
            getProjectDetails(projectKey);
        } catch (RedmineException e) {
            logger.error(e.getMessage());
        }
    }

    private int getUserId(String userName) {
        for (Membership user : projectsUsers) {
            if (user.getUserName().equalsIgnoreCase(userName)) {
                return user.getUserId();
            }
        }
        return 0;
    }


    private void getProjectDetails(String projectKey) throws RedmineException {
        try {
            redmineManager.getTransport().setObjectsPerPage(100);
            redmineManager.setObjectsPerPage(100);
            projectsUsers = redmineManager.getProjectManager().getProjectMembers(this.projectKey);
            versions = projectManager.getVersions(projectManager.getProjectByKey(projectKey).getId());
        } catch (RedmineException ex) {
            logger.info(ex.toString());
            throw ex;
        }
    }
}
