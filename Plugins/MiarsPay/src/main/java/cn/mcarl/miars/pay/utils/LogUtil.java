package cn.mcarl.miars.pay.utils;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;

import cn.mcarl.miars.pay.MiarsPay;
import java.text.SimpleDateFormat;
import java.io.PrintStream;

public class LogUtil
{
    private static LogUtil instance;
    public static PrintStream out;
    private PrintStream ps;
    private String filePath;
    private String dateFormat;
    private SimpleDateFormat df;
    private String linker;
    
    public static LogUtil getInstance() {
        if (LogUtil.instance != null) {
            return LogUtil.instance;
        }
        return new LogUtil(new File(MiarsPay.getInstance().getDataFolder(), "log.txt"));
    }
    
    public LogUtil(final File file, final String dateFormat, final String csn) {
        this.linker = " -> ";
        LogUtil.instance = this;
        try {
            final File pf = file.getParentFile();
            if (!pf.exists()) {
                pf.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            this.filePath = file.getPath();
            this.setLogDateFormat(dateFormat);
            this.ps = new PrintStream(new FileOutputStream(file, true), true, csn);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public LogUtil(final String fileName, final String dateFormat, final String csn) {
        this(new File(fileName), dateFormat, csn);
    }
    
    public LogUtil(final File file, final String dateFormat) {
        this(file, dateFormat, "UTF-8");
    }
    
    public LogUtil(final String fileName, final String dateFormat) {
        this(new File(fileName), dateFormat);
    }
    
    public LogUtil(final File file) {
        this(file, "yyyy-MM-dd HH:mm:ss");
    }
    
    public LogUtil(final String fileName) {
        this(new File(fileName));
    }
    
    public void write(final String str) {
        this.ps.println(str);
    }
    
    public void write(final Object obj) {
        this.ps.println(obj);
    }
    
    public void log(final String str) {
        this.write(this.df.format(System.currentTimeMillis()) + this.linker + str);
    }
    
    public String getLogFilePath() {
        return this.filePath;
    }
    
    public void setLogDateFormat(final String dateFormat) {
        this.dateFormat = dateFormat;
        this.df = new SimpleDateFormat(dateFormat);
    }
    
    public String getLogDateFormat() {
        return this.dateFormat;
    }
    
    public void setLogLinker(final String linker) {
        this.linker = linker;
    }
    
    public String getLogLinker() {
        return this.linker;
    }
    
    public void close() {
        this.ps.close();
    }
}
