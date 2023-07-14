// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.velocity;

import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;
import org.bukkit.potion.PotionEffectType;
import gg.noob.plunder.util.FastTrig;
import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.MathHelper;
import gg.noob.plunder.util.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import java.util.HashMap;
import org.bukkit.util.Vector;
import java.util.Map;
import gg.noob.plunder.checks.Check;

public class VelocityB extends Check
{
    private double pvX;
    private double pvZ;
    private double lastDeltaX;
    private double lastDeltaZ;
    private boolean useEntity;
    private boolean cvc;
    private boolean jumped;
    private boolean useEntityOnMotion;
    private boolean lastUseEntity;
    private boolean failed;
    private Map<Integer, VelocityData> velocityDataMap;
    private double buffer;
    private double moreBuffer;
    private Vector velocity;
    private int ticks;
    private double aiMoveSpeed;
    private static final double[] moveValues;
    
    public VelocityB() {
        super("Velocity (B)");
        this.velocityDataMap = new HashMap<Integer, VelocityData>();
        this.setMaxViolations(11);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
            final double lastPosX = data.lastPosX;
            final double lastPosY = data.lastPosY;
            final double lastPosZ = data.lastPosZ;
            final float lastYaw = data.lastYaw;
            final float lastPitch = data.lastPitch;
            final boolean lastGround = data.lastGroundBoolean;
            final Location to = new Location(player.getWorld(), packetPlayInFlying.a(), packetPlayInFlying.b(), packetPlayInFlying.c(), packetPlayInFlying.d(), packetPlayInFlying.e());
            final Location from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch);
            if (!packetPlayInFlying.g() || packetPlayInFlying.b() == -999.0) {
                to.setX(from.getX());
                to.setY(from.getY());
                to.setZ(from.getZ());
            }
            if (!packetPlayInFlying.h()) {
                to.setYaw(from.getYaw());
                to.setPitch(from.getPitch());
            }
            final double deltaX = to.getX() - from.getX();
            final double deltaY = to.getY() - from.getY();
            final double deltaZ = to.getZ() - from.getZ();
            if (packetPlayInFlying.g() && deltaX != 0.0 && deltaZ != 0.0) {
                final double ldx = this.lastDeltaX;
                final double ldz = this.lastDeltaZ;
                this.calc(deltaX, deltaZ, this.lastDeltaX, this.lastDeltaZ, to.getYaw());
                this.lastDeltaX = ldx;
                this.lastDeltaZ = ldz;
            }
            else {
                data.key = "";
                final PlayerData playerData = data;
                final PlayerData playerData2 = data;
                final double n = 0.0;
                playerData2.forward = n;
                playerData.strafe = n;
            }
            if (this.cvc) {
                this.pvX = this.velocity.getX();
                this.pvZ = this.velocity.getZ();
                this.cvc = false;
                this.ticks = 0;
                this.failed = false;
            }
            if (deltaX != 0.0 || deltaY != 0.0 || deltaZ != 0.0) {
                if (this.pvX != 0.0 || this.pvZ != 0.0) {
                    double drag = 0.91;
                    if (data.getUnderBlockTicks() > 0 || data.getLiquidTicks() > 0 || player.getGameMode() == GameMode.CREATIVE || PlayerUtils.hasInvalidJumpBoost(player) || data.isDoingBlockUpdate()) {
                        final double n2 = 0.0;
                        this.pvZ = n2;
                        this.pvX = n2;
                        this.buffer -= ((this.buffer > 0.0) ? 1.0 : 0.0);
                        return;
                    }
                    if (lastGround) {
                        drag *= data.getFriction();
                    }
                    if (this.ticks == 1) {
                        final double highestY = this.velocity.getY() - 0.05;
                        if (deltaY > highestY) {
                            this.jumped = true;
                        }
                    }
                    final VelocityData velocityData = this.getVelocityData(deltaX, deltaZ, to, lastGround, drag, this.useEntity, data.getSprintTicks());
                    this.velocityDataMap.put(this.ticks, velocityData);
                    if (++this.ticks > 6) {
                        for (int i = 0; i < this.ticks; ++i) {
                            final VelocityData calcVlcData = this.velocityDataMap.get(i);
                            final VelocityData vlcData = this.getVelocityData(calcVlcData.deltaX, calcVlcData.deltaZ, calcVlcData.to, calcVlcData.lastGround, calcVlcData.drag, calcVlcData.useEntity, calcVlcData.sprintTicks);
                            this.pvX = vlcData.getPvx();
                            this.pvZ = vlcData.getPvz();
                            if (Math.abs(this.pvX) < 0.005) {
                                this.pvX = 0.0;
                            }
                            if (Math.abs(this.pvZ) < 0.005) {
                                this.pvZ = 0.0;
                            }
                            if (i == 0 && this.jumped) {
                                this.pvX -= MathHelper.sin(vlcData.to.getYaw() * 0.017453292f) * 0.2f;
                                this.pvZ += MathHelper.cos(vlcData.to.getYaw() * 0.017453292f) * 0.2f;
                                vlcData.jumped = true;
                            }
                            double ratioX = vlcData.getDeltaX() / this.pvX;
                            double ratioZ = vlcData.getDeltaZ() / this.pvZ;
                            ratioX = ((Double.isInfinite(ratioX) || Double.isNaN(ratioX) || ratioX == 0.0) ? 1.0 : ratioX);
                            ratioZ = ((Double.isInfinite(ratioZ) || Double.isNaN(ratioZ) || ratioZ == 0.0) ? 1.0 : ratioZ);
                            final double ratio = (Math.abs(ratioX) + Math.abs(ratioZ)) / 2.0;
                            if ((ratio < 0.9 || ratioX < 0.0 || ratioZ < 0.0 || ratio > 2.15 || Double.isInfinite(ratio) || Double.isNaN(ratio)) && this.pvX != 0.0 && this.pvZ != 0.0 && !data.isTeleporting(5) && !player.getItemInHand().getType().isEdible() && System.currentTimeMillis() - data.getLastBypass() > 1000L && !PlayerUtils.hasInvalidJumpBoost(player)) {
                                if (data.getSolidBlockBehindTicks() < 20) {
                                    if (!this.failed) {
                                        final double buffer = this.buffer + 1.0;
                                        this.buffer = buffer;
                                        if (buffer > 20.0) {
                                            this.logCheat("pct=%.2f ticks=%s buffer=%.1f forward=%.2f strafe=%.2f", ratio * 100.0, i, this.buffer, vlcData.getMoveStrafe(), vlcData.getMoveForward());
                                            this.buffer = 19.0;
                                        }
                                    }
                                    if (ratio > 2.15 || ratioX < -2.15 || ratioZ < -2.15) {
                                        ++this.moreBuffer;
                                    }
                                    if (ratio > 3.0 || ratioX < -3.0 || ratioZ < -3.0) {
                                        ++this.moreBuffer;
                                    }
                                    if (ratio > 10.0 || ratioX < -10.0 || ratioZ < -10.0 || Double.isInfinite(ratio)) {
                                        this.moreBuffer += 3.0;
                                    }
                                    if (this.moreBuffer > 4.0) {
                                        this.logCheat("pct=%.2f ticks=%s moreBuffer=%.1f forward=%.2f strafe=%.2f jumped=%s", ratio * 100.0, i, this.moreBuffer, vlcData.getMoveStrafe(), vlcData.getMoveForward(), this.jumped);
                                        this.moreBuffer = 3.0;
                                        this.back();
                                    }
                                }
                            }
                            else {
                                this.buffer -= ((this.buffer > 0.0) ? (data.hasLag() ? 0.5 : 0.125) : 0.0);
                                this.moreBuffer -= ((this.moreBuffer > 0.0) ? 1.0 : 0.0);
                            }
                            if (player.getName().equalsIgnoreCase("RareMen")) {
                                this.dumpLogs(String.format("ratio=%.3f ratioX=%.3f ratioZ=%.3f dx=%.4f dz=%.4f pvx=%.4f pvz=%.4f buffer=%.1f moreBuffer=%.1f ticks=%s strafe=%.2f forward=%.2f jumped=%s key=%s sprint=%s sprintTicks=%s lastGround=%s useEntityOnMotion=%s useEntity=%s slowdown=%s ai=%.4f", ratio, ratioX, ratioZ, deltaX, deltaZ, this.pvX, this.pvZ, this.buffer, this.moreBuffer, i, vlcData.getMoveStrafe(), vlcData.getMoveForward(), this.jumped, data.getKey(), vlcData.isSprint(), vlcData.getSprintTicks(), lastGround, this.useEntityOnMotion, this.useEntity, vlcData.isSlowdown(), this.aiMoveSpeed));
                            }
                            this.pvX *= vlcData.drag;
                            this.pvZ *= vlcData.drag;
                        }
                        this.ticks = 0;
                        final double n3 = 0.0;
                        this.pvZ = n3;
                        this.pvX = n3;
                        this.jumped = false;
                        this.velocity = null;
                    }
                    this.lastDeltaX = deltaX;
                    this.lastDeltaZ = deltaZ;
                    this.lastUseEntity = this.useEntity;
                }
                this.useEntity = false;
                this.useEntityOnMotion = false;
            }
        }
        else if (packet instanceof PacketPlayInUseEntity && data.isSprint() && !this.useEntity && ((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            this.useEntity = true;
        }
    }
    
    public void calc(final double deltaX, final double deltaZ, double lastDeltaX, double lastDeltaZ, final float yaw) {
        final PlayerData data = this.getPlayerData();
        lastDeltaX *= 0.91 * (data.lastGroundBoolean ? data.getFriction() : 1.0f);
        lastDeltaZ *= 0.91 * (data.lastGroundBoolean ? data.getFriction() : 1.0f);
        if (this.cvc) {
            lastDeltaX = this.velocity.getX();
            lastDeltaZ = this.velocity.getZ();
        }
        if (this.useEntity) {
            lastDeltaX *= 0.6;
            lastDeltaZ *= 0.6;
            this.useEntityOnMotion = true;
        }
        this.aiMoveSpeed = this.getAttributeSpeed(true);
        this.calcKey(deltaX - lastDeltaX, deltaZ - lastDeltaZ, yaw);
    }
    
    private void calcKey(final double mx, final double mz, final float yaw) {
        final PlayerData data = this.getPlayerData();
        float motionYaw = this.getMotionYaw(mx, mz, yaw);
        int direction = 6;
        motionYaw /= 45.0f;
        float moveS = 0.0f;
        float moveF = 0.0f;
        String key = "";
        final double preD = 1.2 * Math.pow(10.0, -3.0);
        if (Math.abs(Math.abs(mx) + Math.abs(mz)) > preD) {
            direction = Math.round(motionYaw);
            if (direction == 1) {
                moveF = 1.0f;
                moveS = -1.0f;
                key = "W + D";
            }
            else if (direction == 2) {
                moveS = -1.0f;
                key = "D";
            }
            else if (direction == 3) {
                moveF = -1.0f;
                moveS = -1.0f;
                key = "S + D";
            }
            else if (direction == 4) {
                moveF = -1.0f;
                key = "S";
            }
            else if (direction == 5) {
                moveF = -1.0f;
                moveS = 1.0f;
                key = "S + A";
            }
            else if (direction == 6) {
                moveS = 1.0f;
                key = "A";
            }
            else if (direction == 7) {
                moveF = 1.0f;
                moveS = 1.0f;
                key = "W + A";
            }
            else if (direction == 8) {
                moveF = 1.0f;
                key = "W";
            }
            else if (direction == 0) {
                moveF = 1.0f;
                key = "W";
            }
        }
        moveF *= 0.98f;
        moveS *= 0.98f;
        data.strafe = moveS;
        data.forward = moveF;
        data.key = key;
    }
    
    private float getMotionYaw(final double mx, final double mz, final float yaw) {
        float motionYaw;
        for (motionYaw = (float)(FastTrig.fast_atan2(mz, mx) * 180.0 / 3.141592653589793) - 90.0f, motionYaw -= yaw; motionYaw > 360.0f; motionYaw -= 360.0f) {}
        while (motionYaw < 0.0f) {
            motionYaw += 360.0f;
        }
        return motionYaw;
    }
    
    private double[] moveFlying(double strafe, double forward, final double friction, final Location to) {
        double f = strafe * strafe + forward * forward;
        if (f >= 9.999999747378752E-5) {
            f = Math.sqrt(f);
            if (f < 1.0) {
                f = 1.0;
            }
            f = friction / f;
            strafe *= f;
            forward *= f;
            final double f2 = Math.sin(to.getYaw() * 3.141592653589793 / 180.0);
            final double f3 = Math.cos(to.getYaw() * 3.141592653589793 / 180.0);
            return new double[] { strafe * f3 - forward * f2, forward * f3 + strafe * f2 };
        }
        return new double[] { 0.0, 0.0 };
    }
    
    public float getAttributeSpeed(final boolean sprint) {
        float attributeSpeed = this.getPlayer().getWalkSpeed() / 2.0f;
        if (sprint) {
            attributeSpeed += attributeSpeed * 0.3f;
        }
        final int speedAmplifier = PlayerUtils.getPotionEffectLevel(this.getPlayer(), PotionEffectType.SPEED);
        if (speedAmplifier > 0) {
            attributeSpeed *= 1.0f + speedAmplifier * 0.2f;
        }
        return attributeSpeed;
    }
    
    @Override
    public void handleTransaction(final Player player, final Vector velocity) {
        this.velocity = velocity;
        this.ticks = 0;
        this.jumped = false;
        this.cvc = true;
        this.velocityDataMap.clear();
    }

    public VelocityData getVelocityData(double deltaX, double deltaZ, Location to, boolean lastGround, double drag, boolean useEntity, int sprintTicks) {
        ArrayList<VelocityData> velocityDataList = new ArrayList<>();
        for (boolean sprint : new boolean[]{true, false}) {
            for (double forward : moveValues) {
                for (double strafe : moveValues) {
                    velocityDataList.add(this.calcVelocityData(this.pvX, this.pvZ, lastGround, deltaX, deltaZ, to, sprint, strafe, forward, useEntity, drag, sprintTicks));
                }
            }
        }
        VelocityData minVelocityData = velocityDataList.stream().min(Comparator.comparing(velocityData -> {
            double tupleDeltaX = Math.abs(velocityData.getPvx() - deltaX);
            double tupleDeltaZ = Math.abs(velocityData.getPvz() - deltaZ);
            return tupleDeltaX * tupleDeltaX + tupleDeltaZ * tupleDeltaZ;
        })).get();
        return this.calcVelocityData(this.pvX, this.pvZ, lastGround, deltaX, deltaZ, to, minVelocityData.isSprint(), minVelocityData.getMoveStrafe(), minVelocityData.getMoveForward(), useEntity, drag, sprintTicks);
    }

    
    public VelocityData calcVelocityData(double pvX, double pvZ, final boolean lastGround, final double deltaX, final double deltaZ, final Location to, final boolean sprint, final double moveStrafe, final double moveForward, final boolean useEntity, final double drag, final int sprintTicks) {
        boolean slowdown = false;
        if (useEntity) {
            pvX *= 0.6;
            pvZ *= 0.6;
            slowdown = true;
        }
        final double f = 0.16277136 / (drag * drag * drag);
        double f2;
        if (lastGround) {
            f2 = this.getAttributeSpeed(sprint) * f;
        }
        else {
            f2 = (sprint ? 0.026000000536441803 : 0.019999999552965164);
        }
        final double[] pvXZ = this.moveFlying(moveStrafe, moveForward, f2, to);
        pvX += pvXZ[0];
        pvZ += pvXZ[1];
        return new VelocityData(pvX, pvZ, deltaX, deltaZ, moveStrafe, moveForward, sprint, slowdown, lastGround, useEntity, to, drag, sprintTicks);
    }
    
    static {
        moveValues = new double[] { -0.98, 0.0, 0.98 };
    }
    
    private static class VelocityData
    {
        private double pvx;
        private double pvz;
        private double moveStrafe;
        private double moveForward;
        private double deltaX;
        private double deltaZ;
        private double drag;
        private boolean sprint;
        private boolean slowdown;
        private boolean lastGround;
        private boolean jumped;
        private boolean useEntity;
        private Location to;
        private int sprintTicks;
        
        public VelocityData(final double pvx, final double pvz, final double deltaX, final double deltaZ, final double moveStrafe, final double moveForward, final boolean sprint, final boolean slowdown, final boolean lastGround, final boolean useEntity, final Location to, final double drag, final int sprintTicks) {
            this.jumped = false;
            this.pvx = pvx;
            this.pvz = pvz;
            this.deltaX = deltaX;
            this.deltaZ = deltaZ;
            this.moveStrafe = moveStrafe;
            this.moveForward = moveForward;
            this.sprint = sprint;
            this.slowdown = slowdown;
            this.lastGround = lastGround;
            this.useEntity = useEntity;
            this.to = to;
            this.drag = drag;
            this.sprintTicks = sprintTicks;
        }
        
        public double getPvx() {
            return this.pvx;
        }
        
        public double getPvz() {
            return this.pvz;
        }
        
        public double getMoveStrafe() {
            return this.moveStrafe;
        }
        
        public double getMoveForward() {
            return this.moveForward;
        }
        
        public double getDeltaX() {
            return this.deltaX;
        }
        
        public double getDeltaZ() {
            return this.deltaZ;
        }
        
        public double getDrag() {
            return this.drag;
        }
        
        public boolean isSprint() {
            return this.sprint;
        }
        
        public boolean isSlowdown() {
            return this.slowdown;
        }
        
        public boolean isLastGround() {
            return this.lastGround;
        }
        
        public boolean isJumped() {
            return this.jumped;
        }
        
        public boolean isUseEntity() {
            return this.useEntity;
        }
        
        public Location getTo() {
            return this.to;
        }
        
        public int getSprintTicks() {
            return this.sprintTicks;
        }
    }
}
