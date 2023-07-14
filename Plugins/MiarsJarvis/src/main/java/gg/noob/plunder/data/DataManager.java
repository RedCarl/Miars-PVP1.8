/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package gg.noob.plunder.data;

import gg.noob.plunder.Plunder;
import gg.noob.plunder.checks.Check;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.ClassUtils;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DataManager {
    private Set<PlayerData> playerDataSet = new HashSet<>();

    public void addPlayerData(final Player player) {
        final PlayerData data = new PlayerData(player);
        this.playerDataSet.add(data);
        new BukkitRunnable(){
            @Override
            public void run() {
                ClassUtils.getClassesInPackage(Plunder.getInstance(), "gg.noob.plunder.checks").forEach(checkClass -> {
                    if (Check.class.isAssignableFrom(checkClass)) {
                        if (checkClass.equals(Check.class)) {
                            return;
                        }
                        try {
                            Check check = (Check)checkClass.newInstance();
                            check.setPlayer(player);
                            String[] split = checkClass.getPackage().getName().split("\\.");
                            check.setType(split[split.length - 2]);
                            data.getChecks().add(check);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                data.setCanCheck(true);
            }
        }.runTaskAsynchronously(Plunder.getInstance());
    }

    public void removePlayerData(Player player) {
        PlayerData data = this.getPlayerData(player);
        if (data != null) {
            this.playerDataSet.remove(data);
        }
    }

    public PlayerData getPlayerData(Player player) {
        return new HashSet<PlayerData>(this.playerDataSet).stream().filter(data -> data.getPlayer() != null && data.getPlayer().getUniqueId() == player.getUniqueId()).findFirst().orElse(null);
    }

    public PlayerData getPlayerData(UUID uuid) {
        return new HashSet<PlayerData>(this.playerDataSet).stream().filter(data -> data.getPlayer() != null && data.getPlayer().getUniqueId() == uuid).findFirst().orElse(null);
    }

    public Set<PlayerData> getPlayerDataSet() {
        return this.playerDataSet;
    }
}

