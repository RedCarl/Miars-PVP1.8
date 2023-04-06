package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.objects.Level;
import io.github.Leonardo0013YT.UltraSkyWars.objects.PrestigeIcon;
import io.github.Leonardo0013YT.UltraSkyWars.objects.Reward;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class LevelManager {

    private final Map<String, PrestigeIcon> prestige = new HashMap<>();
    private final Map<Integer, Level> levels = new HashMap<>();
    private final Map<UUID, Integer> playerLevel = new HashMap<>();
    private final Map<Integer, Reward> soulWellRewards = new HashMap<>();

    public LevelManager() {
        reload();
    }

    public void reload() {
        levels.clear();
        soulWellRewards.clear();
        prestige.clear();
        UltraSkyWars plugin = UltraSkyWars.get();
        if (plugin.getLevels().isSet("prestige")) {
            ConfigurationSection conf = plugin.getLevels().getConfig().getConfigurationSection("prestige");
            for (String c : conf.getKeys(false)) {
                PrestigeIcon pi = new PrestigeIcon(plugin, "prestige." + c);
                prestige.put(pi.getId(), pi);
            }
        }
        if (plugin.getLevels().isSet("levels")) {
            ConfigurationSection conf = plugin.getLevels().getConfig().getConfigurationSection("levels");
            for (String c : conf.getKeys(false)) {
                levels.put(levels.size(), new Level(plugin, "levels." + c, levels.size()));
            }
        }
        if (plugin.getRewards().getConfig().isSet("rewards")) {
            ConfigurationSection conf = plugin.getRewards().getConfig().getConfigurationSection("rewards");
            for (String c : conf.getKeys(false)) {
                int id = soulWellRewards.size();
                soulWellRewards.put(id, new Reward(plugin, "rewards." + c, id));
            }
        }
    }

    public void checkUpgrade(Player p) {
        UltraSkyWars plugin = UltraSkyWars.get();
        if (plugin.getCm().isDisableLevels()) return;
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        if (sw == null) return;
        int level = sw.getLevel();
        Level lvl = getLevel(p);
        Level act = getLevelByLevel(level);
        if (lvl == null || act == null || lvl.getLevel() == act.getLevel()) {
            return;
        }
        if (levels.get(lvl.getId()).getLevel() > levels.get(act.getId()).getLevel()) {
            sw.setLevel(lvl.getLevel());
            lvl.execute(p);
            CustomSound.UPGRADE.reproduce(p);
            p.sendMessage(plugin.getLang().get(p, "messages.levelUp.msg").replaceAll("<level>", lvl.getPrefix()));
            Utils.updateSB(p);
            if (!plugin.getCm().isBroadcastLevelUp()) return;
            for (Player on : Bukkit.getOnlinePlayers()) {
                on.sendMessage(plugin.getLang().get(p, "messages.levelUp.broadcast").replaceAll("<player>", p.getName()).replaceAll("<level>", lvl.getPrefix()));
            }
        }
    }

    public Level getLevel(Player p) {
        if (p == null || !p.isOnline()) return levels.get(0);
        UltraSkyWars plugin = UltraSkyWars.get();
        int elo = plugin.getDb().getSWPlayer(p).getXp();
        for (Level lvl : levels.values()) {
            if (elo >= lvl.getXp() && elo < lvl.getLevelUp()) {
                playerLevel.put(p.getUniqueId(), lvl.getId());
                return lvl;
            }
        }
        return levels.get(0);
    }

    public Level getLevelByLevel(int level) {
        for (Level l : levels.values()) {
            if (l.getLevel() == level) {
                return l;
            }
        }
        return null;
    }

    public String getLevelPrefix(Player p) {
        UltraSkyWars plugin = UltraSkyWars.get();
        if (plugin.getCm().isDisableLevels()) return "";
        if (playerLevel.get(p.getUniqueId()) == null) {
            return plugin.getLang().get("progressBar.max");
        }
        return levels.get(playerLevel.get(p.getUniqueId())).getPrefix();
    }

    public Reward getRewardByID(int id) {
        return soulWellRewards.get(id);
    }

    public Reward getSoulWellRewardByIcon(ItemStack icon) {
        return soulWellRewards.get(NBTEditor.getInt(icon, "REWARD", "ID"));
    }

    public Reward getRandomReward() {
        return soulWellRewards.get(ThreadLocalRandom.current().nextInt(0, soulWellRewards.size()));
    }

    public Map<String, PrestigeIcon> getPrestige() {
        return prestige;
    }

    public Map<UUID, Integer> getPlayerLevel() {
        return playerLevel;
    }

    public Map<Integer, Level> getLevels() {
        return levels;
    }

    public int getNextID() {
        return soulWellRewards.size();
    }

    public boolean isEmpty() {
        return soulWellRewards.isEmpty();
    }

    public void remove(Player p) {
        playerLevel.remove(p.getUniqueId());
    }

}