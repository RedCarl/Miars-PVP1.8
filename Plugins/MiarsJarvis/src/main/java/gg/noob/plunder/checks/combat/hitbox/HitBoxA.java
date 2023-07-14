// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.hitbox;

import gg.noob.plunder.util.Pair;
import gg.noob.plunder.util.PlayerUtils;
import org.bukkit.block.Block;
import gg.noob.plunder.util.BlockUtils;
import java.util.Set;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import gg.noob.plunder.util.MathUtils;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class HitBoxA extends Check
{
    public HitBoxA() {
        super("HitBox (A)");
        this.setBan(false);
    }
    
    @Override
    public boolean handleReceivedPacketCanceled(final Player player, final Packet packet) {
        boolean cancel = false;
        if (packet instanceof PacketPlayInUseEntity) {
            final PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity)packet;
            if (packetPlayInUseEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                final Entity entity = packetPlayInUseEntity.a((World)((CraftWorld)player.getWorld()).getHandle());
                if (entity == null || MathUtils.getHorizontalDistance(player.getLocation(), entity.getBukkitEntity().getLocation()) > 8.0 || !player.getLocation().getChunk().isLoaded() || !entity.getBukkitEntity().getLocation().getChunk().isLoaded()) {
                    return false;
                }
                final Location loc = player.getEyeLocation().clone();
                final Location targetLoc = entity.getBukkitEntity().getLocation().clone();
                boolean current = false;
                boolean up = false;
                boolean x1 = false;
                boolean x2 = false;
                boolean z1 = false;
                boolean z2 = false;
                boolean upX1 = false;
                boolean upX2 = false;
                boolean upZ1 = false;
                boolean upZ2 = false;
                final float lastYaw = player.getLocation().getYaw();
                final float lastPitch = player.getLocation().getPitch();
                final EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
                Location clone = targetLoc;
                this.setUp(entityPlayer, loc, clone);
                if (!this.check(player, loc, clone)) {
                    current = true;
                }
                clone = targetLoc.clone().add(0.0, (double)entity.getHeadHeight(), 0.0);
                this.setUp(entityPlayer, loc, clone);
                if (!this.check(player, loc, clone)) {
                    up = true;
                }
                clone = targetLoc.clone().add((targetLoc.getX() > 0.0) ? ((int)targetLoc.getX() + 1 - targetLoc.getX()) : ((int)targetLoc.getX() - 1 - targetLoc.getX()), 0.0, 0.0);
                this.setUp(entityPlayer, loc, clone);
                if (!this.check(player, loc, clone)) {
                    x1 = true;
                }
                clone = targetLoc.clone().add((int)targetLoc.getX() - targetLoc.getX(), 0.0, 0.0);
                this.setUp(entityPlayer, loc, clone);
                if (!this.check(player, loc, clone)) {
                    x2 = true;
                }
                clone = targetLoc.clone().add(0.0, 0.0, (targetLoc.getZ() > 0.0) ? ((int)targetLoc.getZ() + 1 - targetLoc.getZ()) : ((int)targetLoc.getZ() - 1 - targetLoc.getZ()));
                this.setUp(entityPlayer, loc, clone);
                if (!this.check(player, loc, clone)) {
                    z1 = true;
                }
                clone = targetLoc.clone().add(0.0, 0.0, (int)targetLoc.getZ() - targetLoc.getZ());
                this.setUp(entityPlayer, loc, clone);
                if (!this.check(player, loc, clone)) {
                    z2 = true;
                }
                clone = targetLoc.clone().add((targetLoc.getX() > 0.0) ? ((int)targetLoc.getX() + 1 - targetLoc.getX()) : ((int)targetLoc.getX() - 1 - targetLoc.getX()), (double)entity.getHeadHeight(), 0.0);
                this.setUp(entityPlayer, loc, clone);
                if (!this.check(player, loc, clone)) {
                    upX1 = true;
                }
                clone = targetLoc.clone().add((int)targetLoc.getX() - targetLoc.getX(), (double)entity.getHeadHeight(), 0.0);
                this.setUp(entityPlayer, loc, clone);
                if (!this.check(player, loc, clone)) {
                    upX2 = true;
                }
                clone = targetLoc.clone().add(0.0, (double)entity.getHeadHeight(), (targetLoc.getZ() > 0.0) ? ((int)targetLoc.getZ() + 1 - targetLoc.getZ()) : ((int)targetLoc.getZ() - 1 - targetLoc.getZ()));
                this.setUp(entityPlayer, loc, clone);
                if (!this.check(player, loc, clone)) {
                    upZ1 = true;
                }
                clone = targetLoc.clone().add(0.0, (double)entity.getHeadHeight(), (int)targetLoc.getZ() - targetLoc.getZ());
                this.setUp(entityPlayer, loc, clone);
                if (!this.check(player, loc, clone)) {
                    upZ2 = true;
                }
                if (current && up && x1 && x2 && z1 && z2 && upX1 && upX2 && upZ1 && upZ2) {
                    this.dumpLogs("Canceled: HitBox");
                    cancel = true;
                }
                entityPlayer.yaw = lastYaw;
                entityPlayer.pitch = lastPitch;
            }
        }
        return cancel;
    }
    
    private boolean check(final Player player, final Location loc, final Location targetLoc) {
        boolean ok = true;
        for (int i = 0; i <= 6; ++i) {
            final Block block = player.getTargetBlock((Set)null, i);
            if (!BlockUtils.checkPhase(block.getType())) {
                final Location blockLoc = block.getLocation();
                if (loc.getBlockX() <= blockLoc.getBlockX() || targetLoc.getBlockX() <= blockLoc.getBlockX()) {
                    if (loc.getBlockY() <= blockLoc.getBlockY() || targetLoc.getBlockY() <= blockLoc.getBlockY()) {
                        if (loc.getBlockZ() <= blockLoc.getBlockZ() || targetLoc.getBlockZ() <= blockLoc.getBlockZ()) {
                            if (loc.getBlockX() >= blockLoc.getBlockX() || targetLoc.getBlockX() >= blockLoc.getBlockX()) {
                                if (loc.getBlockY() >= blockLoc.getBlockY() || targetLoc.getBlockY() >= blockLoc.getBlockY()) {
                                    if (loc.getBlockZ() >= blockLoc.getBlockZ() || targetLoc.getBlockZ() >= blockLoc.getBlockZ()) {
                                        ok = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ok;
    }
    
    private void setUp(final EntityPlayer entityPlayer, final Location loc, final Location targetLoc) {
        final Pair<Float, Float> lookAtUp = PlayerUtils.lookAt(loc, targetLoc);
        final float yaw180Up = PlayerUtils.yawTo180F(lookAtUp.getX());
        entityPlayer.yaw = ((yaw180Up < 0.0f) ? 180 : -180) + yaw180Up;
        entityPlayer.pitch = -lookAtUp.getY();
    }
}
