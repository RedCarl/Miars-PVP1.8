// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;

public class Global
{
    public void sendLog(final String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[Plunder] " + message));
    }
}
