package gg.noob.lib.hologram.click.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import cn.mcarl.miars.core.MiarsCore;
import gg.noob.lib.hologram.Hologram;
import gg.noob.lib.hologram.HologramLine;
import gg.noob.lib.hologram.HologramManager;
import gg.noob.lib.hologram.click.ClickType;
import org.bukkit.entity.Player;

public class HologramClickListener extends PacketAdapter {

    public HologramClickListener() {
        super(MiarsCore.getInstance(), PacketType.Play.Client.USE_ENTITY);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packetContainer = event.getPacket();
        Player player = event.getPlayer();

        if (packetContainer.getType() == PacketType.Play.Client.USE_ENTITY) {
            HologramManager hologramManager = MiarsCore.getInstance().getHologramManager();
            int id = packetContainer.getIntegers().read(0);
            EnumWrappers.EntityUseAction action = packetContainer.getEntityUseActions().read(0);

            if (action == EnumWrappers.EntityUseAction.INTERACT_AT) {
                return;
            }

            for (Hologram hologram : hologramManager.getHolograms()) {
                if (!hologram.getBaseHologram().getViewers().contains(player)) {
                    continue;
                }

                for (HologramLine line : hologram.getLines().get(player.getUniqueId())) {
                    if (line.getClickId() == id || line.getEntityId() == id) {
                        ClickType clickType = null;
                        if (action == EnumWrappers.EntityUseAction.ATTACK) {
                            clickType = player.isSneaking() ? ClickType.SHIFT_LEFT : ClickType.LEFT;
                        } else if (action == EnumWrappers.EntityUseAction.INTERACT) {
                            clickType = player.isSneaking() ? ClickType.SHIFT_RIGHT : ClickType.RIGHT;
                        }

                        hologram.getBaseHologram().onClick(player, line, clickType);
                    }
                }
            }
        }
    }
}
