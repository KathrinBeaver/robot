package ru.mai.pvk.robot.redmine.data.utils;

import com.taskadapter.redmineapi.bean.Issue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.*;

public class PvkLogger {

    private StreamHandler streamHandler;
    private final Logger logger;
    private static PvkLogger instance;
    private LoggerController loggerController;

    protected PvkLogger(String name, boolean needConsole) {
        logger = Logger.getLogger(name);
        if (!needConsole) {
            Handler[] handlers = logger.getParent().getHandlers();
            if ((handlers.length > 0) && (handlers[0] instanceof ConsoleHandler)) {
                logger.getParent().removeHandler(handlers[0]);
            }
            ByteArrayOutputStream logStream = new ByteArrayOutputStream();
            PrintStream prStr = new PrintStream(logStream);
            streamHandler = new StreamHandler(prStr, new SimpleFormatter());
            logger.addHandler(streamHandler);
        }
    }

    public void setLoggerControllerWindow(LoggerController window) {
        this.loggerController = window;
    }

    public static PvkLogger getLogger(String name, boolean needConsole) {
        if (instance == null) {
            instance = new PvkLogger(name, needConsole);
        }

        return instance;
    }

    public static PvkLogger getLogger(String name) {
        if (instance == null) {
            return new PvkLogger(name, false);
        }
        return instance;
    }

    public void info(String data) {
        logger.info(data);
        if (loggerController != null) {
            loggerController.appendLogs(data);
        }
        flushAll();
    }

    public void info(List<? extends Object> aList) {
        for (Object o : aList) {
            logger.info(o.toString());
            if (loggerController != null) {
                loggerController.appendLogs(o.toString());
            }
        }
        flushAll();
    }

    public void warning(String data) {
        logger.info(data);
        if (loggerController != null) {
            loggerController.appendLogsBold(data);
            loggerController.appendLogsBoldLn("");
        }
        flushAll();
    }

    public void error(String data) {
        logger.severe(data);
        if (loggerController != null) {
            loggerController.appendLogsError(data);
        }
        flushAll();
    }

    public void logHtmlIssueLink(Issue issue, String url) {
        logger.info(issue.toString());
        if (loggerController != null) {
            loggerController.appendHyperLink(issue.getId().toString(), url);
        }
    }

    private void flushAll() {
        streamHandler.flush();
    }
}
