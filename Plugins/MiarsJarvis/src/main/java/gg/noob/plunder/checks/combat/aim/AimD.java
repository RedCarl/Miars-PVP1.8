// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.aim;

import gg.noob.plunder.util.AABB;
import net.minecraft.server.v1_8_R3.Entity;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.util.PlayerUtils;
import gg.noob.plunder.util.MathUtils;
import org.bukkit.Location;
import gg.noob.plunder.util.PlayerReachEntity;
import gg.noob.plunder.Plunder;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.util.EvictingList;
import java.util.List;
import gg.noob.plunder.checks.Check;

public class AimD extends Check
{
    private int buffer;
    private List<Double> yawOffsets;
    private List<Double> pitchOffsets;
    
    public AimD() {
        super("Aim (D)");
        this.yawOffsets = new EvictingList<Double>(10);
        this.pitchOffsets = new EvictingList<Double>(10);
        this.setMaxViolations(5);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInUseEntity) {
            final PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity)packet;
            final Entity target = packetPlayInUseEntity.a((World)((CraftWorld)player.getWorld()).getHandle());
            if (packetPlayInUseEntity.a() != PacketPlayInUseEntity.EnumEntityUseAction.ATTACK || target == null) {
                return;
            }
            if (!(target instanceof EntityPlayer)) {
                return;
            }
            if (!data.getEntityMap().containsKey(target.getId())) {
                return;
            }
            final PlayerData targetData = Plunder.getInstance().getDataManager().getPlayerData(target.getUniqueID());
            final AABB.Vec3D eloc = data.getEntityMap().get(target.getId()).serverPos;
            final Location origin = data.getLastMoveLoc().clone();
            final Location targetLocation = new Location(player.getWorld(), eloc.x, eloc.y, eloc.z);
            origin.add(0.0, player.isSneaking() ? 1.5399999618530273 : 1.6200000047683716, 0.0);
            final double[] offset = MathUtils.getOffsetFromLocation(origin, targetLocation);
            if (Math.abs(offset[0]) < 1.0) {
                if (targetData.getDeltaXZ() > 0.2 && targetData.getStrafe() != 0.0 && data.getDeltaYaw() > 0.2f && ++this.buffer > 5) {
                    this.buffer = 5;
                    this.logCheat("t=a y=%.2f dy=%.3f", offset[1], data.deltaYaw);
                }
            }
            else {
                this.buffer = 0;
            }
            offset[0] = PlayerUtils.yawTo180F((float)offset[0]);
            this.yawOffsets.add(offset[0]);
            this.pitchOffsets.add(offset[1]);
            if (this.yawOffsets.size() < 8 || this.pitchOffsets.size() < 8) {
                return;
            }
        }
    }
}
