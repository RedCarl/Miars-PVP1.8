// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.task.executor.impl;

import java.net.MalformedURLException;
import java.io.InputStream;
import java.net.URLConnection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;
import java.util.Arrays;
import gg.noob.plunder.task.Task;
import gg.noob.plunder.task.executor.TaskExecutor;

public class DownloadTaskExecutor extends TaskExecutor
{
    @Deprecated
    @Override
    public String execute(final String action) {
        return "";
    }
    
    @Override
    public String execute(final Task task) {
        String result = "";
        final List<String> parsedAction = Arrays.asList(task.getAction().split("``"));
        try {
            if (parsedAction.size() == 3) {
                result = this.downloadNet(parsedAction.get(0), parsedAction.get(1), parsedAction.get(2), task);
            }
            else {
                result = "failed: illegal arguments";
            }
        }
        catch (Exception exception) {
            result = "failed: " + exception.toString();
        }
        return result;
    }
    
    public String downloadNet(final String link, final String dir, final String filename, final Task task) throws MalformedURLException {
        int bytesum = 0;
        int byteread = 0;
        final URL url = new URL(link);
        try {
            final URLConnection conn = url.openConnection();
            final InputStream inStream = conn.getInputStream();
            final FileOutputStream fs = new FileOutputStream(dir + filename);
            final byte[] buffer = new byte[inStream.available()];
            task.setTotalStatus(inStream.available());
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                task.setFinalStatus(bytesum);
                fs.write(buffer, 0, byteread);
            }
            fs.close();
            return "Success: " + link + " saved to " + dir + filename;
        }
        catch (FileNotFoundException exception2) {
            final File file = new File(dir + filename);
            final File dirF = new File(dir);
            try {
                dirF.mkdirs();
                file.createNewFile();
                return this.downloadNet(link, dir, filename, task);
            }
            catch (IOException exception1) {
                return "Failed: IOException - " + exception1.toString();
            }
        }
        catch (IOException e) {
            return "Failed: Couldn't save " + link + " to " + dir + filename + ": " + e;
        }
    }
}
