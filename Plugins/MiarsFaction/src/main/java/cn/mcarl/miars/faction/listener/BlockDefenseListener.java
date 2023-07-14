package cn.mcarl.miars.faction.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.faction.entity.BlockDefenseEntity;
import cn.mcarl.miars.faction.manager.BlockDefenseManager;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class BlockDefenseListener implements Listener {

    @EventHandler
    public void EntityExplodeEvent(EntityExplodeEvent e) {
        List<Block> result = BlockDefenseManager.getInstance().onBoom(e.getLocation(), e.blockList(), 1, 3);
        e.blockList().removeIf(b -> !result.contains(b));
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e){
        Player player = e.getPlayer();
        //马铃薯查看方块耐久
        if (player.getItemInHand().getType().equals(Material.POTATO_ITEM)) {
            if (e.getClickedBlock() != null) {
                BlockDefenseEntity bde = BlockDefenseManager.getInstance().getBlockValue(e.getClickedBlock());
                if (bde!=null){
                    double value = bde.getHealthy();
                    if (value != 0) {
                        int percent = (int) (((value * 100) / BlockDefenseManager.getInstance().getBlockDefaultValue(e.getClickedBlock().getType())));
                        player.sendMessage(ColorParser.parse("&e&l耐久! &7该方块剩余耐久值 "+getBlockInfo(percent)+" &7长时间不攻击将会恢复。"));
                    }
                }
            }
        }
    }

    public String getBlockInfo(Integer i){
        if (i<=10){
            return "&4"+i+"%";
        }else if (i<=30){
            return "&c"+i+"%";
        }else if (i<=60){
            return "&e"+i+"%";
        }else if (i<=100){
            return "&a"+i+"%";
        }
        return "&7ERROR";
    }

}
