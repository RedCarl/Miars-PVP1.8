package cn.mcarl.miars.practice.command;

import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.storage.data.ArenaDataStorage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArenaCreateCommand implements CommandExecutor, TabCompleter {

    Map<String,Arena> data = new HashMap<>();

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("group.admin")){
                Arena arena = new Arena();

                arena.setName(args[0]);
                arena.setMode(FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()));

                if (data.containsKey(arena.getName())){
                    arena = data.get(arena.getName());
                }

                String operate = args[1]; // 设置内容

                switch (operate){
                    case "setName" -> {
                        arena.setName(args[2]);
                        data.put(arena.getName(),arena);
                    }
                    case "setDisplayName" -> {
                        arena.setDisplayName(args[2]);
                        data.put(arena.getName(),arena);
                    }
                    case "build" -> {
                        arena.setBuild(Boolean.valueOf(args[2]));
                        data.put(arena.getName(),arena);
                    }
                    case "setLoc1" -> {
                        arena.setLoc1(player.getLocation());
                        data.put(arena.getName(),arena);
                    }
                    case "setLoc2" -> {
                        arena.setLoc2(player.getLocation());
                        data.put(arena.getName(),arena);
                    }
                    case "save" -> {
                        ArenaDataStorage.getInstance().putArenaData(data.get(arena.getName()));
                    }
                }
                System.out.println(data);

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
        if (sender instanceof Player player) {
            // 玩家指令部分
            if (player.hasPermission("group.admin")) {
                switch (args.length) {
                    case 1: {
                        allCompletes.addAll(data.keySet());
                    }
                    case 2: {
                        allCompletes.add("setName");
                        allCompletes.add("setDisplayName");
                        allCompletes.add("build");
                        allCompletes.add("setLoc1");
                        allCompletes.add("setLoc2");
                        break;
                    }
                }
            }
        }

        return allCompletes.stream()
                .filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
                .limit(10).collect(Collectors.toList());
    }
}