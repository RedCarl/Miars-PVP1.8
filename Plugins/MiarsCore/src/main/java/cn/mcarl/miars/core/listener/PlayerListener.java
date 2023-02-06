package cn.mcarl.miars.core.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.easyplugin.utils.EasyCooldown;
import cn.mcarl.miars.core.manager.ItemInteractManager;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: carl0
 * @DATE: 2022/11/30 20:48
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // 初始化玩家数据
        MPlayerDataStorage.getInstance().getMPlayer(player);

        player.setGameMode(GameMode.SURVIVAL);

        // 禁止玩家进入消息
        e.setJoinMessage(null);

    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();

        MPlayerDataStorage.getInstance().clearUserCacheData(player);

        // 禁止玩家进入消息
        e.setQuitMessage(null);
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

    }
}
