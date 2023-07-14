/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  gg.noob.lib.util.Callback
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package gg.noob.plunder.command;

import gg.noob.lib.util.Callback;
import gg.noob.plunder.checks.combat.aurabot.AuraBotA;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BotCommand
        implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("plunder.bot")) {
            commandSender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }
        if (strings.length == 1) {
            Player player = Bukkit.getPlayer((String)strings[0]);
            if (player != null) {
                AuraBotA.sendBot(player, commandSender instanceof Player ? (Player)commandSender : null, (Callback<Integer>)((Callback)callback -> {
                    commandSender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat((String)"-", (int)25));
                    commandSender.sendMessage(ChatColor.WHITE + "Click Count: " + ChatColor.AQUA + callback);
                    commandSender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat((String)"-", (int)25));
                }));
                commandSender.sendMessage(ChatColor.GREEN + "Successfully send bot to " + player.getName() + "!");
            } else {
                commandSender.sendMessage(ChatColor.RED + strings[0] + " doesn't exist!");
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "Usage: /bot <player>");
        }
        return false;
    }
}

