package cn.mcarl.miars.practiceffa.manager;

import cn.mcarl.miars.practiceffa.MiarsPracticeFFA;
import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @Author: carl0
 * @DATE: 2023/1/3 22:52
 */
public class FFABorderManager {

    private static final FFABorderManager instance = new FFABorderManager();
    public static FFABorderManager getInstance() {
        return instance;
    }

    public void init(){
//        FFAUtil.setGlassBorder();
        FFAUtil.setFFAGameBorder();
        FFAUtil.setBorder();
        PluginConfig.FFA_SITE.LOCATION.get().getWorld().setDifficulty(Difficulty.HARD);
        PluginConfig.FFA_SITE.LOCATION.get().getWorld().setGameRuleValue("naturalRegeneration", "false");
        PluginConfig.FFA_SITE.LOCATION.get().getWorld().setGameRuleValue("doDaylightCycle", "false");
        PluginConfig.FFA_SITE.LOCATION.get().getWorld().setGameRuleValue("doMobSpawning", "false");
        PluginConfig.FFA_SITE.LOCATION.get().getWorld().setTime(1000);
//        tick();
    }

    private void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {
                CombatManager.getInstance().getCombatPlayers().forEach((uuid)->{
                    Player player = Bukkit.getPlayer(uuid);
                    if (CombatManager.getInstance().isCombat(player)){
                        FFAUtil.setVirtualBorder(player);
                    }
                });
            }
        }.runTaskTimerAsynchronously(MiarsPracticeFFA.getInstance(),PluginConfig.FFA_SITE.BORDER_TICK.get(),PluginConfig.FFA_SITE.BORDER_TICK.get());
    }

}
