package cn.mcarl.miars.faction;

import cc.carm.lib.easyplugin.EasyPlugin;
import cn.mcarl.miars.faction.command.FactionCommand;
import cn.mcarl.miars.faction.listener.*;
import cn.mcarl.miars.faction.manager.ConfigManager;
import cn.mcarl.miars.faction.manager.ScoreBoardManager;

public class MiarsFaction extends EasyPlugin {

    private static MiarsFaction instance;
    public static MiarsFaction getInstance() {
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

        log("正在注册事件...");
        registerListener(
                new MapListener(),
                new FakePlayerListener(),
                new BlockDefenseListener(),
                new ScoreBoardListener(),
                new PlayerListener(),
                new SpawnListener()
        );

        log("正在注册指令...");
        registerCommand("faction",new FactionCommand());

        log("正在注册计分板...");
        ScoreBoardManager.getInstance().init();

        shutdown();
        return true;
    }

    @Override
    protected void shutdown() {

        showAD();
    }


    /**
     * 作者信息
     */
    private void showAD() {
        log("&7感谢您使用 &c&l"+getDescription().getName()+" v" + getDescription().getVersion());
        log("&7本插件由 &c&lMCarl Studios &7提供长期支持与维护。");
    }
}
