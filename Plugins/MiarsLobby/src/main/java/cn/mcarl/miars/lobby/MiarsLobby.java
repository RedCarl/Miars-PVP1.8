package cn.mcarl.miars.lobby;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.lobby.command.LobbyCommand;
import cn.mcarl.miars.lobby.command.RechargeCommand;
import cn.mcarl.miars.lobby.listener.BlockListener;
import cn.mcarl.miars.lobby.listener.PlayerListener;
import cn.mcarl.miars.lobby.manager.ConfigManager;
import cn.mcarl.miars.lobby.manager.ScoreBoardManager;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MiarsLobby extends EasyPlugin {

    private static MiarsLobby instance;
    public static MiarsLobby getInstance() {
        return instance;
    }
    protected ConfigManager configManager;
    public ConfigManager getConfigManager(){
        return configManager;
    };


    @Override
    protected boolean initialize() {
        instance = this;

        log("正在初始化配置文件...");
        this.configManager = new ConfigManager(getDataFolder());

        log("正在注册监听器...");
        registerListener(new PlayerListener(),new BlockListener());

        log("正在注册指令...");
        registerCommand("Lobby",new LobbyCommand());
        registerCommand("Recharge",new RechargeCommand());

        log("正在计分板...");
        ScoreBoardManager.getInstance().init();

        showAD();
        return true;
    }


    /**
     * 作者信息
     */
    private void showAD() {
        log("&7感谢您使用 &c&l"+getDescription().getName()+" v" + getDescription().getVersion());
        log("&7本插件由 &c&lMCarl Studios &7提供长期支持与维护。");
    }
}
