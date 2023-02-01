package cn.mcarl.miars.storage;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.storage.manager.ConfigManager;
import cn.mcarl.miars.storage.storage.MySQLStorage;
import cn.mcarl.miars.storage.storage.RedisStorage;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MiarsStorage extends JavaPlugin {

    private static MiarsStorage instance;
    public static MiarsStorage getInstance() {
        return instance;
    }
    private static MySQLStorage mySQLStorage;
    public static MySQLStorage getMySQLStorage() {
        return mySQLStorage;
    }
    private static RedisStorage redisStorage;
    public static RedisStorage getRedisStorage() {
        return redisStorage;
    }
    protected ConfigManager configManager;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        mySQLStorage = new MySQLStorage();
        redisStorage = new RedisStorage();

        log(getName() + " " + getDescription().getVersion() + " &7开始加载...");

        long startTime = System.currentTimeMillis();

        log("正在初始化配置文件...");
        this.configManager = new ConfigManager(getDataFolder());

        log("初始化存储方式...");
        getMySQLStorage().initialize();
        getRedisStorage().initialize();

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

        log("释放存储源...");
        getMySQLStorage().shutdown();
        getRedisStorage().shutdown();

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
