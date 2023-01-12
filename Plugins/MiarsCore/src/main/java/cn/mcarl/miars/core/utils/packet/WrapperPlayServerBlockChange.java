//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.mcarl.miars.core.utils.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.Location;
import org.bukkit.World;

public class WrapperPlayServerBlockChange extends AbstractPacket {
    public static final PacketType TYPE;

    public WrapperPlayServerBlockChange() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerBlockChange(PacketContainer packet) {
        super(packet, TYPE);
    }

    public BlockPosition getLocation() {
        return (BlockPosition)this.handle.getBlockPositionModifier().read(0);
    }

    public void setLocation(BlockPosition value) {
        this.handle.getBlockPositionModifier().write(0, value);
    }

    public Location getBukkitLocation(World world) {
        return this.getLocation().toVector().toLocation(world);
    }

    public WrappedBlockData getBlockData() {
        return (WrappedBlockData)this.handle.getBlockData().read(0);
    }

    public void setBlockData(WrappedBlockData value) {
        this.handle.getBlockData().write(0, value);
    }

    static {
        TYPE = Server.BLOCK_CHANGE;
    }
}
