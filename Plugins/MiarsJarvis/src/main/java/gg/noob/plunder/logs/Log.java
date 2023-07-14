// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.logs;

import java.util.UUID;

public class Log
{
    private UUID uuid;
    private String lastUserName;
    private String caseLastUserName;
    private long time;
    private String check;
    private String moreMessage;
    private int ping;
    private int violations;
    private boolean banned;
    
    public Log(final UUID uuid, final String lastUserName, final String check, final String moreMessage, final int ping, final int violations, final boolean banned) {
        this.uuid = uuid;
        this.lastUserName = lastUserName;
        this.caseLastUserName = lastUserName.toUpperCase();
        this.check = check;
        this.moreMessage = moreMessage;
        this.ping = ping;
        this.violations = violations;
        this.time = System.currentTimeMillis();
        this.banned = banned;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public String getLastUserName() {
        return this.lastUserName;
    }
    
    public String getCaseLastUserName() {
        return this.caseLastUserName;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public String getCheck() {
        return this.check;
    }
    
    public String getMoreMessage() {
        return this.moreMessage;
    }
    
    public int getPing() {
        return this.ping;
    }
    
    public int getViolations() {
        return this.violations;
    }
    
    public boolean isBanned() {
        return this.banned;
    }
    
    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }
    
    public void setLastUserName(final String lastUserName) {
        this.lastUserName = lastUserName;
    }
    
    public void setCaseLastUserName(final String caseLastUserName) {
        this.caseLastUserName = caseLastUserName;
    }
    
    public void setTime(final long time) {
        this.time = time;
    }
    
    public void setCheck(final String check) {
        this.check = check;
    }
    
    public void setMoreMessage(final String moreMessage) {
        this.moreMessage = moreMessage;
    }
    
    public void setPing(final int ping) {
        this.ping = ping;
    }
    
    public void setViolations(final int violations) {
        this.violations = violations;
    }
    
    public void setBanned(final boolean banned) {
        this.banned = banned;
    }
}
