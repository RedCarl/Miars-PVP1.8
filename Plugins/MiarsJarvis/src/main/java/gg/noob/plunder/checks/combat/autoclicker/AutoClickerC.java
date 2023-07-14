// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.combat.autoclicker;

import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class AutoClickerC extends Check
{
    private int count;
    private long lastAttackDistance;
    private long lastAttack;
    
    public AutoClickerC() {
        super("AutoClicker (C)");
        this.count = 0;
        this.lastAttackDistance = -1L;
        this.lastAttack = -1L;
        this.setMaxViolations(3);
    }
    
    @Override
    public void handleReceivedPacket(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInUseEntity) {
            if (this.lastAttack != -1L) {
                if (this.lastAttackDistance != -1L) {
                    if (System.currentTimeMillis() - this.lastAttack == this.lastAttackDistance) {
                        ++this.count;
                    }
                    else if (this.count > 0) {
                        --this.count;
                    }
                    if (this.count > 10) {
                        this.logCheat(player);
                        this.count = 0;
                    }
                }
                this.lastAttackDistance = System.currentTimeMillis() - this.lastAttack;
            }
            this.lastAttack = System.currentTimeMillis();
        }
    }
}
