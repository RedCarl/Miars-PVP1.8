package cn.mcarl.miars.lobby.command;

import cn.mcarl.miars.lobby.MiarsLobby;
import cn.mcarl.miars.lobby.conf.PluginConfig;
import cn.mcarl.miars.lobby.manager.ConfigManager;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
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

public class LobbyCommand implements CommandExecutor, TabCompleter {

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
            case "reload": {
                if (sender instanceof Player player){
                    if (player.hasPermission("miars.admin")){
                        MiarsLobby.getInstance().getConfigManager().reload();
                    }
                }else {
                    MiarsLobby.getInstance().getConfigManager().reload();
                }
            }
            default:
                return helpConsole(sender);
        }

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

        return allCompletes.stream()
                .filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
                .limit(10).collect(Collectors.toList());
    }
}