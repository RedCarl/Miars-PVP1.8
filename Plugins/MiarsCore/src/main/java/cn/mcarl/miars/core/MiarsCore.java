package cn.mcarl.miars.core;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.command.GamemodeCommand;
import cn.mcarl.miars.core.command.MiarsCommand;
import cn.mcarl.miars.core.command.MoneyCommand;
import cn.mcarl.miars.core.hooker.MiarsEconomy;
import cn.mcarl.miars.core.hooker.PAPIExpansion;
import cn.mcarl.miars.core.impl.lunarclient.LunarClientAPI;
import cn.mcarl.miars.core.impl.lunarclient.cooldown.LCCooldown;
import cn.mcarl.miars.core.impl.lunarclient.cooldown.LunarClientAPICooldown;
import cn.mcarl.miars.core.listener.CitizensListener;
import cn.mcarl.miars.core.listener.PlayerListener;
import cn.mcarl.miars.core.listener.WorldListener;
import cn.mcarl.miars.core.manager.CitizensManager;
import cn.mcarl.miars.core.manager.ConfigManager;
import cn.mcarl.miars.core.manager.ItemsManager;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.core.tab.TabProvider_1_7;
import cn.mcarl.miars.core.tab.TabTask;
import cn.mcarl.miars.storage.utils.BungeeApi;
import cn.mcarl.miars.storage.utils.easyitem.ItemManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.nametagedit.plugin.NametagHandler;
import com.nametagedit.plugin.NametagManager;
import com.nametagedit.plugin.api.NametagAPI;
import com.nametagedit.plugin.invisibility.InvisibilityTask;
import dev.fls.tablist.TabHeaderAndFooter;
import gg.noob.lib.hologram.HologramManager;
import gg.noob.lib.hologram.click.listener.HologramClickListener;
import gg.noob.lib.hologram.listener.HologramListener;
import gg.noob.lib.tab.TabHandler;
import gg.noob.lib.tab.TabManager;
import gg.noob.lib.tab.impl.TabAPI;
import lombok.Getter;
import lombok.SneakyThrows;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @Author: carl0
 * @DATE: 2022/11/6 17:42
 */
public class MiarsCore extends JavaPlugin {
    @Getter
    private static MiarsCore instance;
    @Getter
    private static BungeeApi bungeeApi;
    @Getter
    private static LuckPerms luckPerms;
    @Getter
    private static ProtocolManager protocolManager;
    protected ConfigManager configManager;
    @Getter
    private static PlayerPointsAPI ppAPI;
    @Getter
    private static Economy econ;
    private NametagHandler handler;
    private NametagManager manager;
    @Getter
    private static NametagAPI nametagAPI;
    @Getter
    private HologramManager hologramManager;
    @Getter
    private TabHeaderAndFooter tabHeaderAndFooter;

    @Override
    public void onLoad() {
        instance = this;

        // register vault
        Bukkit.getServicesManager().register(
                Economy.class,
                new MiarsEconomy(),
                getInstance(),
                ServicePriority.Normal
        );
    }

    @SneakyThrows
    @Override
    public void onEnable() {

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

        //PlayerPointsAPI
        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
            log("正在加载 PlayerPoints 依赖...");
            ppAPI = PlayerPoints.getInstance().getAPI();
        } else {

            log("未安装 PlayerPoints 不进行货币注册...");
            log("若您想使用全部功能,请安装LuckPerms！");
        }

        //ProtocolLib
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            log("正在注册数据包...");
            protocolManager = ProtocolLibrary.getProtocolManager();
        }else {
            log("未安装 ProtocolLib 不进行数据包注册...");
            log("若您想使用全部功能，请安装ProtocolLib！");
        }

        //Vault
        if (!setupEconomy() ) {
            log("未安装 Vault 不进行经济管理...");
            log("若您想使用全部功能，请安装Vault！");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        /* PlaceholderAPI Support */
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPIExpansion(this).register();
        }

        log("正在注册监听器...");
        regListener(new PlayerListener());
        regListener(new CitizensListener());
        regListener(new ItemManager());
        regListener(new HologramListener());
        regListener(new WorldListener());

        log("正在初始化 Bungee 代理...");
        ServerManager.getInstance().onStartServer();

        log("正在初始化 GUI 模块...");
        GUI.initialize(this);

        log("正在注册指令...");
        regCommand("Miars",new MiarsCommand());
        regCommand("Money",new MoneyCommand());
        regCommand("gamemode",new GamemodeCommand());
//       regCommand("spawner",new SpawnerCommand());

        log("正在初始化 NPC 模块...");
        CitizensManager.getInstance().init();
        new ItemsManager();

        log("正在初始化 NameTag 模块...");
        manager = new NametagManager();
        handler = new NametagHandler(this,manager);
        nametagAPI = new NametagAPI(manager);

        log("彻底隐身系统初始化...");
        new InvisibilityTask().runTaskTimerAsynchronously(this, 100L, 20L);

        log("正在初始化 Tab 模块...");
        tabHeaderAndFooter = new TabHeaderAndFooter(this);
        new TabTask().runTaskTimer(this,0,20);
//        tabManager = new TabManager();
//        getTabManager().setTabHandler(new TabHandler(new TabAPI(), new TabProvider_1_7(), this, 250L));

        log("正在初始化 LunarClientAPi 模块...");
        new LunarClientAPI().onEnable();
        registerLunarClientCoolDownAPI("EnderPearl", 12 * 1000L, Material.ENDER_PEARL);

        log("正在初始化 HologramManager 模块...");
        hologramManager = new HologramManager();

        log("初始化数据包...");
        MiarsCore.getProtocolManager().addPacketListener(new HologramClickListener());

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

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public void registerLunarClientCoolDownAPI(String name, long ms, Material itemId) {
        LunarClientAPICooldown.registerCooldown(new LCCooldown(name, ms, itemId));

       log("Registered lunar client coolDown API: " + name);
    }

}
