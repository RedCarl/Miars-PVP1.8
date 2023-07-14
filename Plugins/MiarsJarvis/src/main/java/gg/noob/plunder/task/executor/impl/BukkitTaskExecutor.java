// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.task.executor.impl;

import gg.noob.plunder.task.Task;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import gg.noob.plunder.task.executor.TaskExecutor;

public class BukkitTaskExecutor extends TaskExecutor
{
    @Override
    public String execute(final String actions) {
        try {
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), actions);
            return "Success";
        }
        catch (Exception exception) {
            return "Failed: " + exception.toString();
        }
    }
    
    @Override
    public String execute(final Task task) {
        final String actions = task.getAction();
        try {
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), actions);
            return "Success";
        }
        catch (Exception exception) {
            return "Failed: " + exception.toString();
        }
    }
}
