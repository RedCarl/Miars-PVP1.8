// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.breaker;

import org.bukkit.Location;
import org.bukkit.block.Block;
import gg.noob.plunder.util.BlockUtils;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.GameMode;
import gg.noob.plunder.util.MathUtils;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.Event;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class BreakerA extends Check
{
    public BreakerA() {
        super("Breaker (A)");
        this.setBan(false);
    }
    
    @Override
    public boolean handleEvent(final Player player, final Event event) {
        boolean cancel = false;
        if (event instanceof BlockBreakEvent) {
            final BlockBreakEvent e = (BlockBreakEvent)event;
            final Block block = e.getBlock();
            final Location loc = block.getLocation();
            if (MathUtils.getHorizontalDistance(player.getLocation(), loc) > 10.0 || !player.getLocation().getChunk().isLoaded() || !loc.getChunk().isLoaded()) {
                return false;
            }
            final Material type = block.getType();
            if (player.getGameMode() == GameMode.CREATIVE) {
                return false;
            }
            if (type == Material.BED_BLOCK) {
                boolean ok = true;
                boolean bed = false;
                for (int i = 0; i <= 6; ++i) {
                    final Block targetBlock = player.getTargetBlock((Set)null, i);
                    if (targetBlock.getType() == Material.BED_BLOCK) {
                        bed = true;
                    }
                    else if (!BlockUtils.checkPhase(targetBlock.getType())) {
                        ok = false;
                        break;
                    }
                }
                if (!ok || !bed) {
                    this.dumpLogs("Canceled: Breaker");
                    cancel = true;
                }
            }
        }
        return cancel;
    }
}
