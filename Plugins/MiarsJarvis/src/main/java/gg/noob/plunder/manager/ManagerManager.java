// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.manager;

import java.util.UUID;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import gg.noob.plunder.Plunder;
import gg.noob.plunder.manager.listener.ManagerGeneralListener;
import org.bukkit.Bukkit;
import java.util.HashSet;
import java.util.Set;

public class ManagerManager
{
    private Set<Manager> managers;
    
    public ManagerManager() {
        this.managers = new HashSet<Manager>();
        Bukkit.getPluginManager().registerEvents((Listener)new ManagerGeneralListener(), (Plugin)Plunder.getInstance());
    }
    
    public Manager getManagerByUUID(final UUID uuid) {
        return this.managers.stream().filter(manager -> manager.getUuid().equals(uuid)).findFirst().orElse(null);
    }
    
    public Manager getManagerByName(final String name) {
        return this.managers.stream().filter(manager -> manager.getLastUserName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    
    public Set<Manager> getManagers() {
        return this.managers;
    }
}
