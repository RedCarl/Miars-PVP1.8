// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.text.SimpleDateFormat;

public class TimeUtils
{
    private static final ThreadLocal<StringBuilder> mmssBuilder;
    private static final SimpleDateFormat dateFormat;
    
    public static String formatIntoHHMMSS(final int secs) {
        return formatIntoMMSS(secs);
    }
    
    public static String formatLongIntoHHMMSS(final long secs) {
        final int unconvertedSeconds = (int)secs;
        return formatIntoMMSS(unconvertedSeconds);
    }
    
    public static String formatIntoMMSS(int secs) {
        final int seconds = secs % 60;
        secs -= seconds;
        long minutesCount = secs / 60;
        final long minutes = minutesCount % 60L;
        minutesCount -= minutes;
        final long hours = minutesCount / 60L;
        final StringBuilder result = TimeUtils.mmssBuilder.get();
        result.setLength(0);
        if (hours > 0L) {
            if (hours < 10L) {
                result.append("0");
            }
            result.append(hours);
            result.append(":");
        }
        if (minutes < 10L) {
            result.append("0");
        }
        result.append(minutes);
        result.append(":");
        if (seconds < 10) {
            result.append("0");
        }
        result.append(seconds);
        return result.toString();
    }
    
    public static String formatLongIntoMMSS(final long secs) {
        final int unconvertedSeconds = (int)secs;
        return formatIntoMMSS(unconvertedSeconds);
    }
    
    public static String formatIntoDetailedString(final int secs) {
        if (secs == 0) {
            return "0s";
        }
        final int remainder = secs % 86400;
        final int days = secs / 86400;
        final int hours = remainder / 3600;
        final int minutes = remainder / 60 - hours * 60;
        final int seconds = remainder % 3600 - minutes * 60;
        final String fDays = (days > 0) ? (days + "d") : "";
        final String fHours = (hours > 0) ? (hours + "h") : "";
        final String fMinutes = (minutes > 0) ? (minutes + "m") : "";
        final String fSeconds = (seconds > 0) ? (seconds + "s") : "";
        return (fDays + fHours + fMinutes + fSeconds).trim();
    }
    
    public static String formatLongIntoDetailedString(final long secs) {
        final int unconvertedSeconds = (int)secs;
        return formatIntoDetailedString(unconvertedSeconds);
    }
    
    public static String formatMillIntoDetailedString(final long secs) {
        final int unconvertedSeconds = (int)secs / 50;
        return formatLongIntoDetailedString(unconvertedSeconds);
    }
    
    public static String formatIntoCalendarString(final Date date) {
        return TimeUtils.dateFormat.format(date);
    }
    
    public static int parseTime(final String time) {
        if (time.equals("0") || time.equals("")) {
            return 0;
        }
        final String[] lifeMatch = { "w", "d", "h", "m", "s" };
        final int[] lifeInterval = { 604800, 86400, 3600, 60, 1 };
        int seconds = -1;
        for (int i = 0; i < lifeMatch.length; ++i) {
            final Matcher matcher = Pattern.compile("([0-9]+)" + lifeMatch[i]).matcher(time);
            while (matcher.find()) {
                if (seconds == -1) {
                    seconds = 0;
                }
                seconds += Integer.parseInt(matcher.group(1)) * lifeInterval[i];
            }
        }
        if (seconds == -1) {
            throw new IllegalArgumentException("Invalid time provided.");
        }
        return seconds;
    }
    
    public static long parseTimeToLong(final String time) {
        return parseTime(time);
    }
    
    public static int getSecondsBetween(final Date a, final Date b) {
        return (int)getSecondsBetweenLong(a, b);
    }
    
    public static long getSecondsBetweenLong(final Date a, final Date b) {
        final long diff = a.getTime() - b.getTime();
        final long absDiff = Math.abs(diff);
        return absDiff / 1000L;
    }
    
    static {
        mmssBuilder = ThreadLocal.withInitial((Supplier<? extends StringBuilder>)StringBuilder::new);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    }
}
