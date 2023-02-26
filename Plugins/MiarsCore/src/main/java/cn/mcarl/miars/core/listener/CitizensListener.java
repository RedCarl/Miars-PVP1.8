package cn.mcarl.miars.core.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.manager.ServerManager;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
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
//        String npcType = CitizensManager.getInstance().getNpcType(npc);
//        switch (npcType) {
//            case "practice" -> {
//                if (ServerManager.getInstance().getServerInfo(npcType) == null) {
//                    player.sendMessage(ColorParser.parse("&7很抱歉,该服务器暂时无法进入,请耐心等待..."));
//                    return;
//                }
//                MiarsCore.getBungeeApi().connect(player, "practice");
//            }
//        }
    }
}