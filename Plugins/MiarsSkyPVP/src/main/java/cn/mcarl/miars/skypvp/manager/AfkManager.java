package cn.mcarl.miars.skypvp.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.skypvp.MiarsSkyPVP;
import cn.mcarl.miars.skypvp.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class AfkManager {
    private static final AfkManager instance = new AfkManager();
    public static AfkManager getInstance() {
        return instance;
    }

    private final Long time = 0L;

    public void init(){
        tick();
    }

    public void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player:Bukkit.getOnlinePlayers()) {
                    if (PlayerUtils.isAfkRegion(player)){
                        int exp = new Random().nextInt(50);
                        float coin = new Random().nextLong(10);
                        player.giveExp(exp);
                        MiarsCore.getEcon().depositPlayer(player,coin);

                        player.setHealth(player.getMaxHealth());
                        player.setFoodLevel(20);
                        player.sendMessage(ColorParser.parse("&9&l挂机池! &7您在挂机池获得了 &e"+exp+" &7经验与 &e"+coin+" &7硬币。"));
                    }
                }
            }
        }.runTaskTimer(MiarsSkyPVP.getInstance(),0,20*30);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player:Bukkit.getOnlinePlayers()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"cc give physical 勇者宝箱 1 "+player.getName());
                    player.sendMessage(ColorParser.parse("&a&l在线奖励! &7在线时间较长，给予 &a勇者宝箱 &7钥匙一把。"));
                }
            }
        }.runTaskTimer(MiarsSkyPVP.getInstance(),0,20*60*15);
    }
}
