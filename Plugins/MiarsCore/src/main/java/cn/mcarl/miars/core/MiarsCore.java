package cn.mcarl.miars.core;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.easyplugin.utils.MessageUtils;
import cn.mcarl.miars.core.hooker.PAPIExpansion;
import cn.mcarl.miars.core.listener.PlayerListener;
import cn.mcarl.miars.core.manager.ConfigManager;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.core.utils.BungeeApi;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lombok.SneakyThrows;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @Author: carl0
 * @DATE: 2022/11/6 17:42
 */
public class MiarsCore extends JavaPlugin {
    private static MiarsCore instance;
    public static MiarsCore getInstance() {
        return instance;
    }
    private static BungeeApi bungeeApi;
    public static BungeeApi getBungeeApi() {
        return bungeeApi;
    }
    private static LuckPerms luckPerms;
    public static LuckPerms getLuckPerms() {
        return luckPerms;
    }
    private static HologramsAPI hologramsAPI;
    public static HologramsAPI getHologramsAPI() {
        return hologramsAPI;
    }
    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }
    private static ProtocolManager protocolManager;
    protected ConfigManager configManager;
    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;

        log(getName() + " " + getDescription().getVersion() + " &7开始加载...");

        long startTime = System.currentTimeMillis();

        log("正在初始化配置文件...");
        this.configManager = new ConfigManager(getDataFolder());

        log("正在注册服务器管理系统...");
        bungeeApi = BungeeApi.of(this);

        //HolographicDisplays
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            log("未找到 HolographicDisplays 依赖...");
            this.setEnabled(false);
            return;
        }
        log("正在加载 HolographicDisplays 依赖...");

        //LuckPerms
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            log("正在注册 LuckPerms 权限管理...");
            luckPerms = provider.getProvider();
        }else {
            log("未安装 LuckPerms 不进行数据包注册...");
            log("若您想使用全部功能,请安装LuckPerms！");
        }

        //PlaceholderAPI
        if (MessageUtils.hasPlaceholderAPI()) {
            log("注册变量...");
            new PAPIExpansion(this).register();
        } else {
            log("检测到未安装PlaceholderAPI，跳过变量注册。");
        }

        //ProtocolLib
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            log("正在注册数据包...");
            protocolManager = ProtocolLibrary.getProtocolManager();
        }else {
            log("未安装 ProtocolLib 不进行数据包注册...");
            log("若您想使用全部功能，请安装ProtocolLib！");
        }

        log("正在注册监听器...");
        regListener(new PlayerListener());

        log("正在初始化 Bungee 代理...");
        ServerManager.getInstance().onStartServer();

        log("正在初始化 GUI 模块...");
        GUI.initialize(this);

        log("当前服务端版本 "+Bukkit.getServer().getVersion());

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

        log("正在关闭 Bungee 代理...");
        ServerManager.getInstance().onStopServer();

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
        log("&7本插件由 &c&lMiars Studios &7提供长期支持与维护。");
    }

}
