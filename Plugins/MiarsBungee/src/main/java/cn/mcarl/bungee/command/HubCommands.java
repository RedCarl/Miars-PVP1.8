package cn.mcarl.bungee.command;

import cn.mcarl.bungee.util.bungee.ServerHelper;
import cc.carm.lib.easyplugin.utils.ColorParser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommands extends Command {
    public HubCommands(String name) {
        super(name);
    }

    public void execute(CommandSender cs, String[] args) {
        if (cs instanceof ProxiedPlayer player){
            player.sendMessage(ColorParser.parse("&7正在连接到 &alobby &7服务器..."));
            player.connect(ServerHelper.getServerInfo("lobby"));
        }
    }
}
