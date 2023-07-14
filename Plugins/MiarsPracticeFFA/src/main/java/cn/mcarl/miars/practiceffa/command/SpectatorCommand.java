package cn.mcarl.miars.practiceffa.command;

import cc.carm.lib.easyplugin.utils.ColorParser;
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

public class SpectatorCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player player) {
            switch (args.length){
                case 1 -> {
                    String name = args[0];
                    //PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer();
//                    if (p!=null){
//                    }else {
//                        player.sendMessage(ColorParser.parse("&7No player matching &b"+args[0]+" &7is connected to this server."));
//                    }
                }
                default -> {
                    player.sendMessage(ColorParser.parse("&7请输入 &b/spec &7玩家名称 来观看战斗。"));
                }
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
                    for (Player p:Bukkit.getOnlinePlayers()){
                        allCompletes.add(p.getName());
                    }
                    break;
                }
            }
        }

        return allCompletes.stream()
                .filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
                .limit(10).collect(Collectors.toList());
    }
}