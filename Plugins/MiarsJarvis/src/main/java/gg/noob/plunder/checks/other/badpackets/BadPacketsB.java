// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.badpackets;

import gg.noob.plunder.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInHeldItemSlot;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.GameMode;
import net.minecraft.server.v1_8_R3.PacketPlayInSpectate;
import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class BadPacketsB extends Check
{
    private long lastFlying;
    private long lastBlockPlace;
    private boolean sentHeldItem;
    private boolean sentBlockPlace;
    private boolean sentBlockDig;
    private boolean sentArmAnimation;
    private boolean sentHeldWhilePlacing;
    private double bufferA;
    private double bufferB;
    private double bufferC;
    private double bufferD;
    private double bufferE;
    
    public BadPacketsB() {
        super("Bad Packets (B)");
        this.bufferA = 0.0;
        this.bufferB = 0.0;
        this.bufferC = 0.0;
        this.bufferD = 0.0;
        this.bufferE = 0.0;
        this.setMaxViolations(6);
        this.setViolationsRemoveOneTime(500L);
    }
    
    @Override
    public boolean handleReceivedPacketCanceled(final Player player, final Packet packet) {
        final PlayerData data = this.getPlayerData();
        boolean cancel = false;
        final long delay = System.currentTimeMillis() - this.lastFlying;
        final long placeDelay = System.currentTimeMillis() - this.lastBlockPlace;
        if (packet instanceof PacketPlayInFlying) {
            if (this.bufferA > 0.0) {
                this.bufferA -= 0.005;
            }
            if (data.isTeleporting(5)) {
                this.bufferA = 0.0;
            }
            if (this.bufferB > 0.0) {
                this.bufferB -= 0.005;
            }
            if (data.isTeleporting(5)) {
                this.bufferB = 0.0;
            }
            if (this.bufferC > 0.0) {
                this.bufferC -= 0.005;
            }
            if (data.isTeleporting(5)) {
                this.bufferC = 0.0;
            }
            if (this.bufferD > 0.0) {
                this.bufferD -= 0.005;
            }
            if (data.isTeleporting(5)) {
                this.bufferD = 0.0;
            }
            if (this.bufferE > 0.0) {
                this.bufferE -= 0.005;
            }
            if (data.isTeleporting(5)) {
                this.bufferE = 0.0;
            }
            if (delay > 40L && delay < 100L) {
                if (this.sentHeldWhilePlacing) {
                    final double bufferA = this.bufferA + 1.0;
                    this.bufferA = bufferA;
                    if (bufferA > 3.0) {
                        final String reason = "Sent held item while placing";
                        this.logCheat(player, "Reason: " + reason + " Delay: " + placeDelay);
                        this.bufferA = 3.0;
                    }
                }
                if (this.sentHeldItem) {
                    final double bufferB = this.bufferB + 1.0;
                    this.bufferB = bufferB;
                    if (bufferB > 3.0) {
                        final String reason = "Post sent held item packet";
                        this.logCheat(player, "Reason: " + reason + " Delay: " + delay);
                        this.bufferB = 3.0;
                    }
                }
                if (this.sentBlockPlace) {
                    final double bufferC = this.bufferC + 1.0;
                    this.bufferC = bufferC;
                    if (bufferC > 3.0) {
                        final String reason = "Post sent block place packet";
                        this.logCheat(player, "Reason: " + reason + " Delay: " + delay);
                        this.bufferC = 3.0;
                    }
                }
                if (this.sentBlockDig) {
                    final double bufferD = this.bufferD + 1.0;
                    this.bufferD = bufferD;
                    if (bufferD > 3.0) {
                        final String reason = "Post sent block dig packet";
                        this.logCheat(player, "Reason: " + reason + " Delay: " + delay);
                        this.bufferD = 3.0;
                    }
                }
                if (this.sentArmAnimation) {
                    final double bufferE = this.bufferE + 1.0;
                    this.bufferE = bufferE;
                    if (bufferE > 3.0) {
                        final String reason = "Post sent arm animation packet";
                        this.logCheat(player, "Reason: " + reason + " Delay: " + delay);
                        this.bufferE = 3.0;
                    }
                }
            }
            this.sentHeldWhilePlacing = false;
            this.sentHeldItem = false;
            this.sentBlockPlace = false;
            this.sentBlockDig = false;
            this.sentArmAnimation = false;
            this.lastFlying = System.currentTimeMillis();
        }
        else if (packet instanceof PacketPlayInBlockPlace) {
            this.lastBlockPlace = System.currentTimeMillis();
        }
        else if (packet instanceof PacketPlayInBlockDig) {
            final PacketPlayInBlockDig blockDig = (PacketPlayInBlockDig)packet;
            if (blockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM && (blockDig.a().getX() != 0 || blockDig.a().getY() != 0 || blockDig.a().getZ() != 0)) {
                final String reason2 = "Invalid release use item to breaking block";
                this.logCheat(player, "Reason: " + reason2);
                cancel = true;
            }
        }
        else if (packet instanceof PacketPlayInSteerVehicle) {
            final PacketPlayInSteerVehicle packetPlayInSteerVehicle = (PacketPlayInSteerVehicle)packet;
            final float forwards = Math.abs(packetPlayInSteerVehicle.a());
            final float sideways = Math.abs(packetPlayInSteerVehicle.b());
            if (forwards > 0.98f || sideways > 0.98f) {
                final String reason3 = "Too large forwards and sideways";
                this.logCheat(player, "Reason: " + reason3);
                cancel = true;
            }
        }
        else if (packet instanceof PacketPlayInSpectate) {
            if (player.getGameMode() != GameMode.SPECTATOR) {
                final String reason = "Spoofed spectate packets";
                this.logCheat(player, "Reason: " + reason);
                cancel = true;
            }
        }
        else if (packet instanceof PacketPlayInClientCommand) {
            final PacketPlayInClientCommand packetPlayInClientCommand = (PacketPlayInClientCommand)packet;
            if (packetPlayInClientCommand.a() == PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN && data.getDeadTicks() <= 0) {
                final String reason2 = "Spoofed respawn packets";
                this.logCheat(player, "Reason: " + reason2);
                cancel = true;
            }
        }
        if (delay < 10L) {
            if (packet instanceof PacketPlayInHeldItemSlot) {
                this.sentHeldItem = true;
            }
            else if (packet instanceof PacketPlayInBlockPlace) {
                this.sentBlockPlace = true;
            }
            else if (packet instanceof PacketPlayInBlockDig) {
                this.sentBlockDig = true;
            }
            else if (packet instanceof PacketPlayInArmAnimation) {
                this.sentArmAnimation = true;
            }
        }
        if (placeDelay < 10L && packet instanceof PacketPlayInHeldItemSlot) {
            this.sentHeldWhilePlacing = true;
        }
        return cancel;
    }
}
