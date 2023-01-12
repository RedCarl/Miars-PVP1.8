
package cn.mcarl.miars.core.utils.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;

public class WrapperPlayClientBlockDig extends AbstractPacket {
    public static final PacketType TYPE;

    public WrapperPlayClientBlockDig() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayClientBlockDig(PacketContainer packet) {
        super(packet, TYPE);
    }

    public EnumWrappers.PlayerDigType getStatus() {
        return (EnumWrappers.PlayerDigType)this.handle.getPlayerDigTypes().read(0);
    }

    public void setStatus(EnumWrappers.PlayerDigType value) {
        this.handle.getPlayerDigTypes().write(0, value);
    }

    public BlockPosition getLocation() {
        return (BlockPosition)this.handle.getBlockPositionModifier().read(0);
    }

    public void setLocation(BlockPosition value) {
        this.handle.getBlockPositionModifier().write(0, value);
    }

    static {
        TYPE = Client.BLOCK_DIG;
    }
}
