package cn.mcarl.miars.pay.command;

import cn.mcarl.miars.pay.enums.PaywayType;
import cn.mcarl.miars.pay.manager.PayManager;
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

public class PayCommand implements CommandExecutor, TabCompleter {

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
            case "recharge" -> {
                if (sender instanceof Player player){

                    if (!player.hasPermission("group.admin")){
                        return false;
                    }
                    if (args.length==3){
                        PaywayType type = PaywayType.valueOf(args[1].toUpperCase());
                        Double money = Double.valueOf(args[2]);
                        PayManager.initiatePay(player,type,money);
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