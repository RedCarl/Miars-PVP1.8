// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.manager;

import gg.noob.plunder.util.MongoManager;
import gg.noob.plunder.Plunder;
import java.util.UUID;

public class Manager
{
    private UUID uuid;
    private String lastUserName;
    private String caseLastUserName;
    private boolean alerts;
    private boolean verbose;
    private boolean debug;
    private long firstJoin;
    private int trust;
    
    public Manager(final UUID uuid, final String lastUserName) {
        this.alerts = true;
        this.verbose = false;
        this.debug = false;
        this.trust = 0;
        this.uuid = uuid;
        this.lastUserName = lastUserName;
        this.caseLastUserName = lastUserName.toUpperCase();
        this.firstJoin = System.currentTimeMillis();
    }
    
    public void save() {
        final MongoManager mongoManager = Plunder.getInstance().getMongoManager();
        mongoManager.saveManager(this);
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
    
    public boolean isAlerts() {
        return this.alerts;
    }
    
    public boolean isVerbose() {
        return this.verbose;
    }
    
    public boolean isDebug() {
        return this.debug;
    }
    
    public long getFirstJoin() {
        return this.firstJoin;
    }
    
    public int getTrust() {
        return this.trust;
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
    
    public void setAlerts(final boolean alerts) {
        this.alerts = alerts;
    }
    
    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }
    
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }
    
    public void setFirstJoin(final long firstJoin) {
        this.firstJoin = firstJoin;
    }
    
    public void setTrust(final int trust) {
        this.trust = trust;
    }
}
