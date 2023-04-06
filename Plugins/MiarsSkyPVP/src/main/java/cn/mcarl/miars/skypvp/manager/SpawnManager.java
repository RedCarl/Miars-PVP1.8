package cn.mcarl.miars.skypvp.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.skypvp.MiarsSkyPVP;
import cn.mcarl.miars.skypvp.conf.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpawnManager {
    private static final SpawnManager instance = new SpawnManager();
    public static SpawnManager getInstance() {
        return instance;
    }

    private final Map<UUID, Long> data = new HashMap<>();


    public void init(){
        tick();
    }

    public void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid:data.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);

                    if (player!=null && player.isOnline()){
                        if (System.currentTimeMillis() - data.get(uuid) >= 5000){
                            data.remove(uuid);
                            player.teleport(PluginConfig.PROTECTED_REGION.SPAWN.get());
                            player.sendTitle(new Title(
                                    ColorParser.parse("&a&l已回城!"),
                                    ColorParser.parse("&7您可以在这里稍做调整。"),
                                    0,
                                    20,
                                    5
                            ));
                            break;
                        }else {
                            player.sendTitle(new Title(
                                    ColorParser.parse("&e&l等待回城..."),
                                    ColorParser.parse("&7剩余 &e"+(((5000-(System.currentTimeMillis() - data.get(uuid)))/1000)+1)+" &7秒"),
                                    0,
                                    20,
                                    5
                            ));
                        }
                    }else {
                        data.remove(uuid);
                    }
                }
            }
        }.runTaskTimer(MiarsSkyPVP.getInstance(),0,20);
    }

    public void put(Player player){
        data.put(player.getUniqueId(),System.currentTimeMillis());
    }

    public void remove(Player player){
        if (data.containsKey(player.getUniqueId())){
            data.remove(player.getUniqueId());
            player.sendTitle(new Title(
                    ColorParser.parse("&c&l回城打断"),
                    ColorParser.parse("&7您的移动打断了回城..."),
                    0,
                    20,
                    5
            ));
        }
    }
}
