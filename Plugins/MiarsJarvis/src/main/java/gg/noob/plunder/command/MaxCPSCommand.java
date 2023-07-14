// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.command;

import gg.noob.plunder.data.PlayerData;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.combat.autoclicker.AutoClickerA;
import gg.noob.plunder.checks.Check;
import org.bukkit.ChatColor;
import gg.noob.plunder.Plunder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class MaxCPSCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (strings.length == 1) {
            final Player player = Bukkit.getPlayer(strings[0]);
            if (player != null) {
                final PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
                if (data == null) {
                    player.sendMessage(ChatColor.RED + "Your data is loading...");
                    return true;
                }
                final Check autoClickerCheck = data.getChecks().stream().filter(check -> check instanceof AutoClickerA).findFirst().orElse(null);
                if (autoClickerCheck == null) {
                    player.sendMessage(ChatColor.RED + "This command is blocked.");
                    return true;
                }
                commandSender.sendMessage(ChatColor.AQUA + player.getName() + ChatColor.WHITE + "'s max CPS: " + ChatColor.YELLOW + ((AutoClickerA)autoClickerCheck).maxCPS);
            }
            else {
                commandSender.sendMessage(ChatColor.RED + strings[0] + " doesn't exist!");
            }
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "/maxCPS <player>");
        }
        return false;
    }
}
