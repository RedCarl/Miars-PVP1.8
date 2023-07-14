/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package gg.noob.plunder.command;

import gg.noob.plunder.Plunder;
import gg.noob.plunder.checks.Check;
import gg.noob.plunder.logs.Log;
import gg.noob.plunder.manager.Manager;
import gg.noob.plunder.manager.ManagerManager;
import gg.noob.plunder.util.MongoManager;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlunderCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender commandSender, Command command, String s, final String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat((String)"-", (int)25));
            commandSender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Plunder " + ChatColor.WHITE + "version" + ChatColor.AQUA + " 3.0.8-SNAPSHOT");
            commandSender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat((String)"-", (int)25));
            return true;
        }
        if (commandSender.isOp()) {
            if (strings[0].equalsIgnoreCase("toggle")) {
                Check check1 = Plunder.getInstance().getChecks().stream().filter(check -> check.getName().equalsIgnoreCase(strings[1])).findFirst().orElse(null);
                if (check1 != null) {
                    check1.setEnable(!check1.isEnable());
                    Plunder.getInstance().getDataManager().getPlayerDataSet().forEach(data -> {
                        Check check2 = data.getChecks().stream().filter(check -> check.getName().equalsIgnoreCase(strings[1])).findFirst().orElse(null);
                        if (check2 != null) {
                            check2.setEnable(!check2.isEnable());
                        }
                    });
                } else {
                    commandSender.sendMessage(ChatColor.RED + strings[0] + " doesn't exist!");
                }
            } else if (strings.length == 2) {
                if (strings[0].equalsIgnoreCase("ban")) {
                    Player player = Bukkit.getPlayer((String)strings[1]);
                    if (player != null) {
                        commandSender.sendMessage(ChatColor.GREEN + "Executing...");
                        this.ban(player);
                    } else {
                        commandSender.sendMessage(ChatColor.RED + strings[1] + " doesn't exist!");
                    }
                } else if (strings[0].equalsIgnoreCase("clearLogs")) {
                    final MongoManager mongoManager = Plunder.getInstance().getMongoManager();
                    commandSender.sendMessage(ChatColor.GREEN + "Executing...");
                    new BukkitRunnable(){

                        public void run() {
                            Set<Log> logs = mongoManager.getLogs(strings[1]);
                            if (logs == null) {
                                commandSender.sendMessage(ChatColor.RED + strings[1] + " doesn't exist!");
                                return;
                            }
                            for (Log log : logs) {
                                mongoManager.removeLog(log);
                            }
                            commandSender.sendMessage(ChatColor.GREEN + "Successfully clear " + strings[1] + "'s logs!");
                        }
                    }.runTaskAsynchronously((Plugin)Plunder.getInstance());
                }
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "No permission.");
        }
        return false;
    }

    public void ban(Player player) {
        Bukkit.broadcastMessage((String)"");
        Bukkit.broadcastMessage((String)(ChatColor.RED + ChatColor.BOLD.toString() + "\u2718 " + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " was banned by " + ChatColor.AQUA + "Plunder " + ChatColor.WHITE + "for " + ChatColor.AQUA + "Unfair Advantage"));
        Bukkit.broadcastMessage((String)"");
        ManagerManager managerManager = Plunder.getInstance().getManagerManager();
        Manager playerManager = managerManager.getManagerByUUID(player.getUniqueId());
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("ban -s " + player.getName() + " 30d Unfair Advantage"));
    }
}

