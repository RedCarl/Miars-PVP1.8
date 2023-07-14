package cn.mcarl.miars.practiceffa.listener;

import cn.mcarl.miars.storage.utils.packet.WrapperPlayClientBlockDig;
import cn.mcarl.miars.storage.utils.packet.WrapperPlayServerBlockChange;
import cn.mcarl.miars.practiceffa.MiarsPracticeFFA;
import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.practiceffa.manager.CombatManager;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ProtocolListener extends PacketAdapter {
    /**
     * Initialize a packet adapter using a collection of parameters. Use {@link #params()} to get an instance to this builder.
     */
    public ProtocolListener() {
        super(PacketAdapter.params(MiarsPracticeFFA.getInstance(), PacketType.Play.Server.BLOCK_CHANGE, PacketType.Play.Client.BLOCK_DIG, PacketType.Play.Client.BLOCK_PLACE));
    }

    private static final Map<String, Block> lastBedRockBlock = new HashMap<>();
    private static final Map<String,Long> lastBedRockTime = new HashMap<>();
    /**
     * 如果玩家攻击方块就将方块变成基岩，持续 3 秒
     * @param e
     */
    @Override
    public void onPacketReceiving(PacketEvent e) {
        Player player = e.getPlayer();

        //Todo 是否在战斗状态
        if (!CombatManager.getInstance().isCombat(player)){
            return;
        }

        if (e.getPacketType() == PacketType.Play.Client.BLOCK_DIG) {
            WrapperPlayClientBlockDig blockDig = new WrapperPlayClientBlockDig(e.getPacket());
            EnumWrappers.PlayerDigType digType = blockDig.getStatus();
            if (digType.name().contains("DESTROY_BLOCK")) {

                //如果在保护区内就执行
                Location loc = blockDig.getLocation().toLocation(player.getWorld());
                if (FFAUtil.isItemRange(loc,PluginConfig.FFA_SITE.LOCATION.get(),PluginConfig.FFA_SITE.RADIUS.get())){
                    e.setCancelled(true);

                    if (digType == EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK || digType == EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK) {
                        lastBedRockTime.put(player.getUniqueId().toString(), 0L);
                        lastBedRockBlock.put(player.getUniqueId().toString(), null);
                        return;
                    }

                    player.sendBlockChange(loc, Material.BEDROCK, (byte) 0);
                    //Todo 设置基岩的存在时间
                    lastBedRockTime.put(player.getUniqueId().toString(),System.currentTimeMillis());
                    lastBedRockBlock.put(player.getUniqueId().toString(),loc.getBlock());
                }


            }
        }
    }

    /**
     * 防止玩家右键将虚拟方块点消失
     * @param e
     */
    @Override
    public void onPacketSending(PacketEvent e) {
        Player player = e.getPlayer();

        //Todo 是否在战斗状态
        if (!CombatManager.getInstance().isCombat(player)){
            return;
        }

        if (e.getPacketType() == PacketType.Play.Server.BLOCK_CHANGE) {
            if (!e.isAsync()) {
                WrapperPlayServerBlockChange blockChange = new WrapperPlayServerBlockChange(e.getPacket());

                if (blockChange.getBlockData().getType() != Material.AIR) {
                    return;
                }

                //玩家是否真正的在范围内
                int range = PluginConfig.FFA_SITE.BORDER_RADIUS.get()+PluginConfig.FFA_SITE.RADIUS.get();
                if (!FFAUtil.isRange(player, PluginConfig.FFA_SITE.LOCATION.get(),range)){
                    return;
                }

                //是否在保护区内
                Location loc = blockChange.getLocation().toLocation(player.getWorld());
                if (FFAUtil.isItemRange(loc,player.getLocation(),PluginConfig.FFA_SITE.BORDER_RADIUS.get())){
                    e.setCancelled(true);
                }
            }
        }
    }

    public static boolean isDisablePlace(Player player, Block block) {
        if (lastBedRockBlock.get(player.getUniqueId().toString()) == null || lastBedRockTime.get(player.getUniqueId().toString()) <= 0) {
            return false;
        }
        return lastBedRockBlock.get(player.getUniqueId().toString()).equals(block) && lastBedRockTime.get(player.getUniqueId().toString()) + 3000 >= System.currentTimeMillis();
    }
}
