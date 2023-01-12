package cn.mcarl.bungee.auth;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.bungee.auth.listener.PlayerListener;
import cn.mcarl.bungee.auth.manager.ConfigManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class MiarsAuthBungee extends Plugin {

    private static MiarsAuthBungee instance;
    public static MiarsAuthBungee getInstance() {
        return instance;
    }

    protected ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;

        log(getDescription().getName() + " " + getDescription().getVersion() + " &7开始加载...");

        long startTime = System.currentTimeMillis();

        log("正在初始化配置文件...");
        this.configManager = new ConfigManager(getDataFolder());

        log("正在注册监听器...");
        regListener(new PlayerListener(getInstance()));
//
//        log("正在注册指令...");
//        regCommand(new HubCommands("hub"));

//        log("正在开启 HttpServer 服务端...");
//        ServerManager.getInstance().run();

        log("加载完成 ,共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

        showAD();
    }

    /**
     * 注册监听器
     *
     * @param listener 监听器
     */
    public static void regListener(Listener listener) {
        ProxyServer.getInstance().getPluginManager().registerListener(getInstance(), listener);
    }

    /**
     * 注册指令
     *
     * @param command 指令
     */
    public static void regCommand(Command command) {
        ProxyServer.getInstance().getPluginManager().registerCommand(getInstance(), command);
    }

    /**
     * 日志
     * @param message 日志消息
     */
    public static void log(String message) {
        ProxyServer.getInstance().getConsole().sendMessage(ColorParser.parse("[" + getInstance().getDescription().getName() + "] " + message));
    }

    /**
     * 作者信息
     */
    private void showAD() {
        log("&7感谢您使用 &c&l"+getDescription().getName()+" v" + getDescription().getVersion());
        log("&7本插件由 &c&lMiars Studios &7提供长期支持与维护。");
    }
}
