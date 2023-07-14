// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.command;

import gg.noob.plunder.data.PlayerData;
import org.bukkit.entity.Player;
import gg.noob.lib.tab.client.ClientVersionUtil;
import java.util.Arrays;
import gg.noob.plunder.util.Version;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import gg.noob.plunder.Plunder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class PlayerInfoCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (commandSender.hasPermission("plunder.alerts")) {
            if (strings.length == 1) {
                final Player player = Bukkit.getPlayer(strings[0]);
                if (player != null) {
                    final PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
                    if (data != null) {
                        commandSender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 25));
                        commandSender.sendMessage(ChatColor.WHITE + "Name: " + ChatColor.AQUA + player.getName());
                        commandSender.sendMessage(ChatColor.WHITE + "Version: " + ChatColor.AQUA + Arrays.stream(Version.values()).filter(version -> version.getProtocol() == ClientVersionUtil.getProtocolVersion(player)).findFirst().orElse(Arrays.stream(Version.values()).filter(version -> version.getProtocol() == -1).findFirst().orElse(null)));
                        commandSender.sendMessage(ChatColor.WHITE + "Client Brand: " + ChatColor.AQUA + data.getClientBrand());
                        commandSender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 25));
                    }
                    else {
                        commandSender.sendMessage(ChatColor.RED + player.getName() + "'s data hasn't loaded yet!");
                    }
                }
                else {
                    commandSender.sendMessage(ChatColor.RED + strings[0] + " doesn't exist!");
                }
            }
            else {
                commandSender.sendMessage(ChatColor.RED + "/playerInfo <player>");
            }
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "No permission.");
        }
        return false;
    }
}
