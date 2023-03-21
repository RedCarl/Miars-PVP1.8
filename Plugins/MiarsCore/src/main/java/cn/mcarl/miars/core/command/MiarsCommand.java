package cn.mcarl.miars.core.command;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.manager.CitizensManager;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.core.ui.OpenInvGUI;
import cn.mcarl.miars.core.ui.ServerMenuGUI;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeGameDataStorage;
import cn.mcarl.miars.storage.storage.data.serverInfo.ServerInfoDataStorage;
import cn.mcarl.miars.storage.storage.data.serverMenu.ServerMenuDataStorage;
import cn.mcarl.miars.storage.storage.data.serverNpc.ServerNpcDataStorage;
import cn.mcarl.miars.storage.utils.BukkitUtils;
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

public class MiarsCommand implements CommandExecutor, TabCompleter {

    private boolean helpConsole(CommandSender sender) {
        return true;
    }

    private boolean helpPlayer(Player player) {
        return true;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        if (args.length < 1) {
            return helpConsole(sender);
        }
        switch (args[0].toLowerCase()) {
            case "practice" -> {
                if (args.length < 2) {
                    return helpConsole(sender);
                }
                switch (args[1].toLowerCase()) {
                    case "openinv" -> {
                        if (args.length < 4) {
                            return helpConsole(sender);
                        }
                        String name = args[2];
                        Integer id = Integer.valueOf(args[3]);

                        ArenaState state = PracticeGameDataStorage.getInstance().getArenaDataById(id);

                        Player player = (Player) sender;
                        OpenInvGUI.open(player, state,name);

                        return true;
                    }
                    default -> {
                        return helpConsole(sender);
                    }
                }
            }
            case "menu" -> {
                if (sender instanceof Player player){
                    if (!player.hasPermission("group.admin")){
                        return false;
                    }
                    switch (args[1].toLowerCase()){
                        case "reload" -> {
                            ServerMenuDataStorage.getInstance().clear();
                        }
                        case "open" -> {
                            if (args.length>=3){
                                ServerMenuGUI.open(player, ColorParser.parse(args[2]));
                            }
                        }
                    }
                }
            }
            case "serverinfo" -> {
                if (sender instanceof Player player){

                    if (!player.hasPermission("group.admin")){
                        return false;
                    }
                    switch (args[1].toLowerCase()){
                        case "reload" -> {
                            ServerInfoDataStorage.getInstance().clear();
                        }
                    }
                }
            }
            case "npc" -> {
                if (sender instanceof Player player){

                    if (!player.hasPermission("group.admin")){
                        return false;
                    }
                    switch (args[1].toLowerCase()){
                        case "reload" -> {
                            ServerNpcDataStorage.getInstance().clear();
                            CitizensManager.getInstance().clear();
                            CitizensManager.getInstance().init();
                        }
                    }
                }
            }

            default -> {
                return helpConsole(sender);
            }
        }

        return false;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String alias,
            @NotNull String[] args
    ) {
        List<String> allCompletes = new ArrayList<>();
//        if (sender instanceof Player) {
//            // 玩家指令部分
//            Player player = (Player) sender;
//            if (player.hasPermission("UltraDepository.use")) {
//                allCompletes.add("reload");
//            }
//        } else {
//            //后台指令部分
//            allCompletes.add("reload");
//        }

        return allCompletes.stream()
                .filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
                .limit(10).collect(Collectors.toList());
    }
}