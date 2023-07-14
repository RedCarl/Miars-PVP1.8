package cn.mcarl.miars.core.command;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.manager.CitizensManager;
import cn.mcarl.miars.core.ui.OpenInvGUI;
import cn.mcarl.miars.core.ui.ServerMenuGUI;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.storage.data.practice.PracticeGameDataStorage;
import cn.mcarl.miars.storage.storage.data.serverInfo.ServerInfoDataStorage;
import cn.mcarl.miars.storage.storage.data.serverMenu.ServerMenuDataStorage;
import cn.mcarl.miars.storage.storage.data.serverNpc.ServerNpcDataStorage;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GamemodeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {

        if (sender instanceof Player player){
            if (player.hasPermission("miars.admin")) {
                switch (args[0].toLowerCase()) {
                    case "0" -> {
                        player.setGameMode(GameMode.SURVIVAL);
                    }
                    case "1" -> {
                        player.setGameMode(GameMode.CREATIVE);
                    }
                    case "2" -> {
                        player.setGameMode(GameMode.ADVENTURE);
                    }
                    case "3" -> {
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                }
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
        if (sender instanceof Player player) {
            // 玩家指令部分
            if (player.hasPermission("miars.admin")) {
                allCompletes.add("0");
                allCompletes.add("1");
                allCompletes.add("2");
                allCompletes.add("3");
            }
        }

        return allCompletes.stream()
                .filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
                .limit(10).collect(Collectors.toList());
    }
}