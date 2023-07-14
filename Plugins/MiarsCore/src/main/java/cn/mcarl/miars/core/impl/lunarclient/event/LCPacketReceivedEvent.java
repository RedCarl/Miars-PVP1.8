package cn.mcarl.miars.core.impl.lunarclient.event;

import lombok.Getter;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.LCPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public final class LCPacketReceivedEvent extends PlayerEvent {

    @Getter
    private static HandlerList handlerList = new HandlerList();

    @Getter
    private final LCPacket packet;

    /**
     * Called when the player sends a {@link LCPacket}
     * to the server from the client.
     *
     * @param player The LunarClient player sending the packet.
     * @param packet The incoming packet from the client.
     */
    public LCPacketReceivedEvent(Player player, LCPacket packet) {
        super(player);
        this.packet = packet;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}