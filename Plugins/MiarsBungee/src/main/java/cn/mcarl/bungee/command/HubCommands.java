package cn.mcarl.bungee.command;

import cn.mcarl.bungee.util.bungee.ServerHelper;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommands extends Command {
    public HubCommands(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (cs instanceof ProxiedPlayer player){
            ServerHelper.sendLobbyServer(player);
        }
    }
}
