// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.logs.command;

import org.bukkit.plugin.Plugin;
import java.util.Set;
import java.util.List;
import gg.noob.plunder.logs.Log;
import gg.noob.plunder.checks.Check;
import java.util.ArrayList;
import gg.noob.plunder.util.MongoManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;
import gg.noob.plunder.Plunder;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class LogsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (commandSender instanceof Player) {
            final Player player = (Player)commandSender;
            if (strings.length == 1) {
                if (player.hasPermission("plunder.alerts")) {
                    final MongoManager mongoManager = Plunder.getInstance().getMongoManager();
                    player.sendMessage(ChatColor.YELLOW + "Catching...");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            final Set<Log> logs = mongoManager.getLogs(strings[0]);
                            final List<Check> checks = new ArrayList<Check>();
                            for (final Check check : Plunder.getInstance().getChecks()) {
                                final List<Log> checkLogs = new ArrayList<Log>();
                                for (final Log log : logs) {
                                    if (!log.getCheck().equalsIgnoreCase(check.getName())) {
                                        continue;
                                    }
                                    checkLogs.add(log);
                                }
                                if (checkLogs.isEmpty()) {
                                    continue;
                                }
                                checks.add(check);
                            }
                            // Todo LogMenu
//                            new ChecksLogMenu(player, strings[0], 1, new ArrayList<Log>(logs), checks.size()).open();
                        }
                    }.runTaskAsynchronously(Plunder.getInstance());
                }
                else {
                    player.sendMessage(ChatColor.RED + "No permission.");
                }
            }
            else {
                player.sendMessage(ChatColor.RED + "Usage: /logs <player>");
            }
        }
        else {
            commandSender.sendMessage(ChatColor.RED + "This command can only be executed by the player!");
        }
        return false;
    }
}
