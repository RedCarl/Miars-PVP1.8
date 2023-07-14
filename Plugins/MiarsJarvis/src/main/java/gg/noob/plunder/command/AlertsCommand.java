// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.command;

import gg.noob.plunder.manager.Manager;
import gg.noob.plunder.manager.ManagerManager;
import gg.noob.plunder.Plunder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class AlertsCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (commandSender instanceof Player) {
            final Player player = (Player)commandSender;
            if (!player.hasPermission("plunder.alerts")) {
                player.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            final ManagerManager managerManager = Plunder.getInstance().getManagerManager();
            final Manager manager = managerManager.getManagerByUUID(player.getUniqueId());
            if (manager.isAlerts()) {
                manager.setAlerts(false);
                player.sendMessage(ChatColor.RED + "You are no longer view alerts!");
            }
            else {
                manager.setAlerts(true);
                player.sendMessage(ChatColor.GREEN + "You are now receive alerts!");
            }
            manager.save();
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "This command can only be execute by the player!");
        }
        return false;
    }
}
