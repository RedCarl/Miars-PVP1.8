package io.github.Leonardo0013YT.UltraSkyWarsSetup;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.cmds.SetupCMD;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.listeners.SetupListener;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.managers.SetupManager;
import io.github.Leonardo0013YT.UltraSkyWarsSetup.menus.SetupMenu;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class UltraSkyWarsSetup extends JavaPlugin {

    private static UltraSkyWarsSetup instance;
    private UltraSkyWars ultraSW;
    private SetupManager sm;
    private SetupMenu sem;

    public static UltraSkyWarsSetup get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        if (!Bukkit.getPluginManager().isPluginEnabled("UltraSkyWars")) {
            Bukkit.getConsoleSender().sendMessage("§4You must have UltraSkyWars.jar before you can use Setup mode.");
            Bukkit.getScheduler().cancelTasks(this);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        ultraSW = UltraSkyWars.get();
        this.sm = new SetupManager();
        this.sem = new SetupMenu(ultraSW);
        getServer().getPluginManager().registerEvents(new SetupListener(ultraSW, this), this);
        getCommand("sws").setExecutor(new SetupCMD(ultraSW, this));
    }

    @Override
    public void onDisable() {

    }

    public UltraSkyWars getUltraSW() {
        return ultraSW;
    }

    public SetupManager getSm() {
        return sm;
    }

    public SetupMenu getSem() {
        return sem;
    }
}