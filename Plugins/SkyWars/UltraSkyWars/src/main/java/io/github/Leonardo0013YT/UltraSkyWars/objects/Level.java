package io.github.Leonardo0013YT.UltraSkyWars.objects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Level {

    @Getter
    private int id, xp, level, levelUp;
    @Getter
    private String prefix;
    private List<String> rewards;

    public Level(UltraSkyWars plugin, String path, int id) {
        this.id = id;
        this.level = plugin.getLevels().getInt(path + ".level");
        this.xp = plugin.getLevels().getInt(path + ".xp");
        this.levelUp = plugin.getLevels().getInt(path + ".levelUp");
        this.prefix = plugin.getLevels().get(path + ".prefix");
        this.rewards = plugin.getLevels().getList(path + ".rewards");
    }

    public void execute(Player p) {
        for (String r : rewards) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), r.replaceFirst("/", "").replaceAll("<player>", p.getName()));
        }
    }

}