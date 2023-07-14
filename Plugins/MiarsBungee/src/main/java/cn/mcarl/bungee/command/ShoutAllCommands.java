package cn.mcarl.bungee.command;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.bungee.MiarsBungee;
import cn.mcarl.bungee.entity.ShouAllData;
import cn.mcarl.bungee.manager.ShoutAllManager;
import cn.mcarl.bungee.util.bungee.ServerHelper;
import me.clip.placeholderapi.libs.JSONMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class ShoutAllCommands extends Command {
    public ShoutAllCommands(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (cs instanceof ProxiedPlayer player){
            ServerInfo serverInfo = player.getServer().getInfo();

            String token = UUID.randomUUID().toString();
            String serverName = ServerHelper.getServerDisplayName(serverInfo.getName());

            TextComponent tc = new TextComponent(
                    ColorParser.parse("\n&b["+serverName+"] &a"+ player.getName() +" &fwants you to play! &a(Join)\n")
            );
            tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpserver "+token));
            tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorParser.parse("&7Join Server")).create()));

            ShoutAllManager.getInstance().putShoutAll(token,new ShouAllData(
                    player.getName(),
                    System.currentTimeMillis(),
                    serverInfo
            ));

            for (ProxiedPlayer p : MiarsBungee.getInstance().getProxy().getPlayers()) {
                p.sendMessage(tc);
            }
        }
    }
}
