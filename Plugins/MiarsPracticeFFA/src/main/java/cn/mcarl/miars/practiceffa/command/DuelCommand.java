package cn.mcarl.miars.practiceffa.command;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.practiceffa.ui.SelectPracticeGUI;
import cn.mcarl.miars.storage.entity.practice.Duel;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
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

public class DuelCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player player) {
            switch (args.length){
                case 1 -> {
                    Player p = Bukkit.getPlayerExact(args[0]);
                    if (p!=null){
                        if (p.getName().equals(player.getName())){
                            player.sendMessage(ColorParser.parse("&7You can't duel yourself."));
                            return true;
                        }
                        SelectPracticeGUI.open(player, QueueType.UNRANKED,p);
                    }else {
                        player.sendMessage(ColorParser.parse("&7No player matching &b"+args[0]+" &7is connected to this server."));
                    }
                }
                case 3 -> {
                    String var = args[0];
                    if ("accept".equals(var)){
                        Player p = Bukkit.getPlayerExact(args[1]);
                        if (p!=null){
                            FKitType fKitType = FKitType.valueOf(args[2]);

                            if (PracticeQueueDataStorage.getInstance().acceptDuel(
                                    new Duel(fKitType,p.getName(),player.getName(),System.currentTimeMillis(),1)
                            )){
                                for (int i = 0; i < 20; i++) {
                                    player.sendMessage("");
                                }
                            }else {
                                player.sendMessage(ColorParser.parse("&7You can no longer accept this request."));
                            }

                        }else {
                            player.sendMessage(ColorParser.parse("&7No player matching &b"+args[1]+" &7is connected to this server."));
                        }
                    }
                }
                default -> {
                    player.sendMessage(ColorParser.parse("&7Usage: &b/duel <player>"));
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