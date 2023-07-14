package cn.mcarl.miars.practiceffa.command;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.practiceffa.MiarsPracticeFFA;
import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.practiceffa.manager.LeaderboardsHologram;
import gg.noob.lib.hologram.Hologram;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderboardsHologramCommand implements CommandExecutor, TabCompleter {

    @SneakyThrows
    @Override
    public boolean onCommand(
            @NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("miars.admin")){
                switch (args.length){
                    case 1 -> {
                        String name = args[0];
                        switch (name){
                            case "DailyWinstreak" -> {
                                PluginConfig.LEADERBOARDS_HOLOGRAM.DAILY_WINSTREAK.set(player.getLocation());
                                MiarsPracticeFFA.getInstance().getConfigManager().getConfigProvider().save();
                                MiarsCore.getInstance().getHologramManager().getHolograms().forEach(Hologram::updateViewers);
                                player.sendMessage(ColorParser.parse("&7update hologram success."));
                            }
                        }
                    }
                    default -> {
                        player.sendMessage(ColorParser.parse("&7User: &b/leaderboard <type> &7Set location."));
                    }
                }
            }else {
                return false;
            }

        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        List<String> allCompletes = new ArrayList<>();
        if (sender instanceof Player) {
            // 玩家指令部分
            switch (args.length) {
                case 1: {
                    allCompletes.add("DailyWinstreak");
                    break;
                }
            }
        }

        return allCompletes.stream()
                .filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
                .limit(10).collect(Collectors.toList());
    }
}