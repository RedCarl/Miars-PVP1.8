package cn.mcarl.miars.lobby.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.lobby.kit.LobbyItem;
import cn.mcarl.miars.lobby.manager.ItemInteractManager;
import cn.mcarl.miars.lobby.conf.PluginConfig;
import cn.mcarl.miars.lobby.manager.ScoreBoardManager;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();
        Location location = player.getLocation();

        if (location.getBlockY() <= PluginConfig.LOBBY_SITE.HEIGHT.get()){
            player.teleport(PluginConfig.LOBBY_SITE.LOCATION.get());
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.teleport(PluginConfig.LOBBY_SITE.LOCATION.get());

        ToolUtils.autoEquip(player, LobbyItem.get());

        ScoreBoardManager.getInstance().joinPlayer(player);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();
        ScoreBoardManager.getInstance().removePlayer(player);
    }

    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(e.getPlayer());
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
        e.setFormat(ColorParser.parse(mRank.getPrefix()+mRank.getNameColor()+"%1$s&f: %2$s"));
    }

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e){
        if (e.getEntity() instanceof Player p){
            // 判断玩家是否在安全区外面
            p.setFoodLevel(20);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack itemStack = e.getItem();
        Block block = e.getClickedBlock();
        Action action = e.getAction();

        if (itemStack!=null){
            // 物品的交互
            ItemInteractManager.getInstance().init(itemStack,player);
        }

        if(e.getAction().equals(Action.PHYSICAL)) {
            if (e.getClickedBlock().getType() == Material.STONE_PLATE) {
                Vector v = player.getVelocity();
                v.setX(8);
                v.setY(2);
                player.setVelocity(v);

            }
        }

    }



    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event) {
        // 禁止玩家丢弃物品
        event.setCancelled(true);
    }

    /**
     * 禁止玩家移动物品
     */
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        Player player = e.getWhoClicked().getKiller();
        ItemStack itemStack = e.getCurrentItem();

        if (itemStack!=null && itemStack.getType()!= Material.AIR){
            NBTItem nbtItem = new NBTItem(itemStack);
            if (nbtItem.getBoolean("stopClick")){
                e.setCancelled(true);
            }
        }

    }

}
