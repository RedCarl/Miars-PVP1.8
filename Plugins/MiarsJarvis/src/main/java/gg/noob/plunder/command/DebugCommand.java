// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.command;

import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.manager.Manager;
import gg.noob.plunder.manager.ManagerManager;
import org.bukkit.Bukkit;
import gg.noob.plunder.Plunder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class DebugCommand implements CommandExecutor
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
            if (strings.length == 0) {
                if (manager.isDebug()) {
                    manager.setDebug(false);
                    player.sendMessage(ChatColor.RED + "You are no longer view debug!");
                }
                else {
                    manager.setDebug(true);
                    player.sendMessage(ChatColor.GREEN + "You are now receive debug!");
                }
                manager.save();
            }
            else {
                final Player target = Bukkit.getPlayer(strings[0]);
                if (target != null) {
                    final PlayerData targetData = Plunder.getInstance().getDataManager().getPlayerData(target);
                    if (targetData != null) {
                        targetData.checkingPacket = !targetData.checkingPacket;
                        player.sendMessage("YES");
                    }
                }
            }
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "This command can only be execute by the player!");
        }
        return false;
    }
}
