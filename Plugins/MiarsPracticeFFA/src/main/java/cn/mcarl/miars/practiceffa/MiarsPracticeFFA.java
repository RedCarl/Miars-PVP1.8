package cn.mcarl.miars.practiceffa;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.listener.EnderPearlListener;
import cn.mcarl.miars.practiceffa.command.DuelCommand;
import cn.mcarl.miars.practiceffa.hooker.PAPIExpansion;
import cn.mcarl.miars.practiceffa.listener.BlockListener;
import cn.mcarl.miars.practiceffa.listener.EntityListener;
import cn.mcarl.miars.practiceffa.listener.PlayerListener;
import cn.mcarl.miars.practiceffa.manager.*;
import cn.mcarl.miars.storage.storage.data.practice.PracticeDailyStreakDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import gg.noob.lib.hologram.BaseHologram;
import gg.noob.lib.hologram.Hologram;
import gg.noob.lib.hologram.HologramManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @Author: carl0
 * @DATE: 2023/1/3 19:55
 */
public class MiarsPracticeFFA extends JavaPlugin {

    @Getter
    private static MiarsPracticeFFA instance;
    @Getter
    protected ConfigManager configManager;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;

        log(getName() + " " + getDescription().getVersion() + " &7开始加载...");

        long startTime = System.currentTimeMillis();

        log("正在初始化配置文件...");
        this.configManager = new ConfigManager(getDataFolder());

        log("正在注册监听器...");
        regListener(new EntityListener());
        regListener(new PlayerListener());
        regListener(new BlockListener());
        regListener(new EnderPearlListener());

        log("正在注册指令...");
        regCommand("Duel",new DuelCommand());
//        regCommand("Leaderboard",new LeaderboardsHologramCommand());

        /* PlaceholderAPI Support */
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPIExpansion(this).register();
        }

        log("正在初始化FFA竞技场...");
        FFABorderManager.getInstance().init();

        log("正在初始化 Board 模块...");
        ScoreBoardManager.getInstance().init();
        MiarsCore.getInstance().getTabHeaderAndFooter().getHeader().setLines(
                "&bKazer Network",
                "&r");
        MiarsCore.getInstance().getTabHeaderAndFooter().getFooter().setLines(
                "&r",
                "&fYou are playing &bPractice &fon &bkazer.gg");

        log("正在初始化 DeathChest 模块...");
        DeathChestManager.getInstance().init();

        log("正在初始化 LeaderboardsHologram 组件...");
        new BukkitRunnable() {
            @Override
            public void run() {
                HologramManager hologramManager = MiarsCore.getInstance().getHologramManager();
                Hologram leaderboardHologram = new Hologram(new LeaderboardsHologram());
                leaderboardHologram.run();
                hologramManager.registerHologram(leaderboardHologram);
            }
        }.runTaskLater(this,1L);

//        log("初始化数据包...");
//        MiarsCore.getProtocolManager().addPacketListener(new ProtocolListener());

        log("加载完成 ,共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

        showAD();
    }

    @SneakyThrows
    @Override
    public void onDisable() {
        log(getName() + " " + getDescription().getVersion() + " 开始卸载...");
        long startTime = System.currentTimeMillis();

        log("卸载监听器...");
        Bukkit.getServicesManager().unregisterAll(this);
        PracticeQueueDataStorage.getInstance().clear();
        PracticeDailyStreakDataStorage.getInstance().clear();

        log("卸载完成 ,共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

        showAD();
    }

    /**
     * 注册监听器
     *
     * @param listener 监听器
     */
    public static void regListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, getInstance());
    }

    /**
     * 注册指令
     *
     * @param name 指令名字
     * @param command 指令
     */
    public static void regCommand(String name, CommandExecutor command) {
        Bukkit.getPluginCommand(name).setExecutor(command);
    }

    /**
     * 日志
     * @param message 日志消息
     */
    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(ColorParser.parse("[" + getInstance().getName() + "] " + message));
    }

    /**
     * 作者信息
     */
    private void showAD() {
        log("&7感谢您使用 &c&l"+getDescription().getName()+" v" + getDescription().getVersion());
        log("&7本插件由 &c&lMCarl Studios &7提供长期支持与维护。");
    }

}
