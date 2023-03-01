package cn.mcarl.miars.megawalls;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.megawalls.conf.PluginConfig;
import cn.mcarl.miars.megawalls.game.listener.GamePlayerListener;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.manager.ConfigManager;
import cn.mcarl.miars.megawalls.game.manager.ScoreBoardManager;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MiarsMegaWalls extends JavaPlugin {

    private static MiarsMegaWalls instance;
    public static MiarsMegaWalls getInstance() {
        return instance;
    }
    protected ConfigManager configManager;
    public ConfigManager getConfigManager(){
        return configManager;
    };

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;

        log(getName() + " " + getDescription().getVersion() + " &7开始加载...");

        long startTime = System.currentTimeMillis();

        log("正在初始化配置文件...");
        this.configManager = new ConfigManager(getDataFolder());

        if ("GAME".equals(PluginConfig.MODE.get())){
            log("初始化 MegaWalls 游戏数据...");
            GameManager.getInstance().initGameInfo();
            log("正在注册监听器...");
            regListener(new GamePlayerListener());
        }

        log("正在初始化 ScoreBoard 数据...");
        ScoreBoardManager.getInstance().init();


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
