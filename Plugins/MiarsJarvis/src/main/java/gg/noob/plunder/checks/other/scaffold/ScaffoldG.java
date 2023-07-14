// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.scaffold;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.Event;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class ScaffoldG extends Check
{
    public ScaffoldG() {
        super("Scaffold (G)");
        this.setMaxViolations(3);
    }
    
    @Override
    public boolean handleEvent(final Player player, final Event event) {
        if (event instanceof BlockPlaceEvent) {
            final BlockPlaceEvent e = (BlockPlaceEvent)event;
            final Block ba = e.getBlockAgainst();
            if (!e.getBlockPlaced().getType().isBlock()) {
                return false;
            }
            final Block b = e.getBlock();
            final double ypos = b.getLocation().getY() - player.getLocation().getY();
            final double distance = player.getPlayer().getLocation().distance(b.getLocation());
            final double ab_distance = player.getLocation().distance(ba.getLocation()) + 0.4;
            if (distance >= 1.3 && distance > ab_distance && ypos <= 0.5) {
                this.logCheat(String.format("d:%.4f, ad:%.4f y=%.1f", distance, ab_distance, ypos));
                this.back();
                return true;
            }
        }
        return false;
    }
}
