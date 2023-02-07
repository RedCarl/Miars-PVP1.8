package cn.mcarl.miars.practiceffa.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.utils.CustomizeColor;
import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    
    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        if (FFAUtil.isItemRange(e.getBlock().getLocation(),PluginConfig.FFA_SITE.LOCATION.get(),(PluginConfig.FFA_SITE.RADIUS.get()*6))){
            if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ColorParser.parse("&7很抱歉，这里不适合您这样做，换个地方试试吧。"));

                Location location = e.getBlockPlaced().getLocation().add(0.5, 1.5, 0.5);

                PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), CustomizeColor.RED, CustomizeColor.GREEN, CustomizeColor.BLUE, (float) 255, 0, 10);
                ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(particles);
            }
        }

    }
    
    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        if (FFAUtil.isItemRange(e.getBlock().getLocation(), PluginConfig.FFA_SITE.LOCATION.get(), (PluginConfig.FFA_SITE.RADIUS.get()*6))){
            if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ColorParser.parse("&7很抱歉,这里不适合您这样做,换个地方试试吧。"));

                Location location = e.getBlock().getLocation().add(0.5, 1.5, 0.5);

                PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), CustomizeColor.RED, CustomizeColor.GREEN, CustomizeColor.BLUE, (float) 255, 0, 10);
                ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(particles);
            }
        }
    }
}
