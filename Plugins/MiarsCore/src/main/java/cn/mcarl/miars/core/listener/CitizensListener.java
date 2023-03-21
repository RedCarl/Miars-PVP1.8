package cn.mcarl.miars.core.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.conf.PluginConfig;
import cn.mcarl.miars.core.manager.CitizensManager;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.core.ui.ServerMenuGUI;
import cn.mcarl.miars.storage.entity.serverNpc.ServerNPC;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CitizensListener implements Listener {

    @EventHandler
    public void NPCLeftClickEvent(NPCLeftClickEvent e){
        ClickNPCEvent(e.getNPC(),e.getClicker(),0);
    }

    @EventHandler
    public void NPCRightClickEvent(NPCRightClickEvent e){
        ClickNPCEvent(e.getNPC(),e.getClicker(),1);
    }

    public void ClickNPCEvent(NPC npc, Player player, int type){
        ServerNPC serverNPC = CitizensManager.getInstance().getServerNPC(npc.data().get(PluginConfig.SERVER_INFO.NAME.get()));
        switch (serverNPC.getType()) {
            case "server" -> {
                if (ServerManager.getInstance().getServerInfo(serverNPC.getValue()) == null) {
                    player.sendMessage(ColorParser.parse("&7很抱歉,该服务器暂时无法进入,请耐心等待..."));
                    return;
                }
                MiarsCore.getBungeeApi().connect(player, serverNPC.getValue());
            }
            case "menu" -> {
                ServerMenuGUI.open(player,serverNPC.getValue());
            }
            case "command" -> {
                Bukkit.dispatchCommand(player,serverNPC.getValue());
            }
        }
    }
}