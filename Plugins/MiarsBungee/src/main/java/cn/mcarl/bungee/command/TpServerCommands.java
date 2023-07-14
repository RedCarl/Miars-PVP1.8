package cn.mcarl.bungee.command;

import cn.mcarl.bungee.manager.ShoutAllManager;
import cn.mcarl.bungee.util.bungee.ServerHelper;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TpServerCommands extends Command {
    public TpServerCommands(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (cs instanceof ProxiedPlayer player){
            String token = args[0];
            ShoutAllManager.getInstance().click(player,token);
        }
    }
}
