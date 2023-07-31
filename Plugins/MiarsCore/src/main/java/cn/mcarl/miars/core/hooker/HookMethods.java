package cn.mcarl.miars.core.hooker;

import cn.mcarl.miars.core.MiarsCore;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import java.util.List;

public class HookMethods {
  public void protocolLibHook(final List<String> list) {
    ProtocolLibrary.getProtocolManager()
      .addPacketListener(new PacketAdapter(MiarsCore.getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Client.TAB_COMPLETE) {
          @Override
          public void onPacketReceiving(PacketEvent e) {
            if (e.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
              PacketContainer packet = e.getPacket();
              String message = packet.getSpecificModifier(String.class).read(0).toLowerCase();
              if (!e.getPlayer().hasPermission("antitab.bypass")) {
                  for (String command : list) {
                    if (message.startsWith(command) || (message.startsWith("/") && !message.contains(" "))) {
                        e.setCancelled(true);
                    }
                  }
              }
            } 
          }
        });
  }
}
