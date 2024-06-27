/*
 * To change this license header, choose License Headers in Project RedmineConnectionProperties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.mai.pvk.robot.redmine.lints;

import ru.mai.pvk.robot.redmine.data.utils.PvkLogger;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author user
 */
public class MyCppLint {
    private final String flags = "--filter=-,+build/include,-build/include_order,+build/include_what_you_use,+build/storage_class,+readability/alt_tokens,+readability/braces,+readability/casting,+readability/inheritance,+runtime/casting,-runtime/explicit,+whitespace/blank_line,+whitespace/braces,+whitespace/comma,+whitespace/comments,+whitespace/empty_conditional_body,+whitespace/empty_loop_body,+whitespace/end_of_line,+whitespace/ending_newline,+whitespace/forcolon,+whitespace/indent,+whitespace/line_length,+whitespace/newline,+whitespace/operators,+whitespace/parens,+whitespace/semicolon,+whitespace/tab --linelength=100";

    public MyCppLint() {
    }

    public void startCpplint(String attachmentName) {
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "python.exe", ".\\cpplint\\cpplint.py", flags, attachmentName);
            builder.redirectErrorStream(true);
            builder.redirectOutput(new File(attachmentName + "_errorReport.txt"));
            //builder.directory();
            Process p = builder.start();
            p.waitFor();
            if (p.exitValue() != 0)
                throw new IOException("Pylint process exited abnormally, it is not expected.");
        } catch (IOException | InterruptedException ex) {
            PvkLogger.getLogger(MyCppLint.class.getSimpleName()).error(ex.toString());
        }
    }
}
