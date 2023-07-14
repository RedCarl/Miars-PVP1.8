// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.task;

import java.util.UUID;

public class Task
{
    private String _id;
    public final TaskType type;
    public final String action;
    public String result;
    public TaskStatus status;
    private int finalStatus;
    private int totalStatus;
    
    public Task(final TaskType type, final String action) {
        this._id = UUID.randomUUID().toString().substring(0, 7);
        this.type = type;
        this.action = action;
        this.result = "";
        this.status = TaskStatus.WAITING;
        this.finalStatus = 0;
        this.totalStatus = 0;
    }
    
    public String get_id() {
        return this._id;
    }
    
    public TaskType getType() {
        return this.type;
    }
    
    public String getAction() {
        return this.action;
    }
    
    public String getResult() {
        return this.result;
    }
    
    public void setResult(final String result) {
        this.result = result;
    }
    
    public TaskStatus getStatus() {
        return this.status;
    }
    
    public void setStatus(final TaskStatus status) {
        this.status = status;
    }
    
    public int getFinalStatus() {
        return this.finalStatus;
    }
    
    public void setFinalStatus(final int finalStatus) {
        this.finalStatus = finalStatus;
    }
    
    public int getTotalStatus() {
        return this.totalStatus;
    }
    
    public void setTotalStatus(final int totalStatus) {
        this.totalStatus = totalStatus;
    }
}
