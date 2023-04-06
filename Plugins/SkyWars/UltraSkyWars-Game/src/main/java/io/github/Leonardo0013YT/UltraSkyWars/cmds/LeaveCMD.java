package io.github.Leonardo0013YT.UltraSkyWars.cmds;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCMD implements CommandExecutor {

    private UltraSkyWars plugin;

    public LeaveCMD(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!plugin.getGm().isPlayerInGame(p)) {
                p.sendMessage(plugin.getLang().get(p, "messages.noInGame"));
                return true;
            }
            plugin.getGm().removePlayerAllGame(p);
            p.sendMessage(plugin.getLang().get(p, "messages.leaveGame"));
        }
        return false;
    }
}