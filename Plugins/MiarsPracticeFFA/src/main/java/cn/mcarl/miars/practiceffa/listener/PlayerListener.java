package cn.mcarl.miars.practiceffa.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.practiceffa.manager.*;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.practiceffa.MiarsPracticeFFA;
import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import cn.mcarl.miars.storage.storage.data.practice.FPlayerDataStorage;
import cn.mcarl.miars.practiceffa.ui.BlockGUI;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.sql.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Author: carl0
 * @DATE: 2023/1/3 23:34
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

        // 传送至出生点
        player.teleport(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(),PluginConfig.FFA_SITE.LOCATION.get().getX(),PluginConfig.FFA_SITE.LOCATION.get().getY()+1,PluginConfig.FFA_SITE.LOCATION.get().getZ()));

        // 初始化玩家数据
        FPlayerDataStorage.getInstance().getFPlayer(player);

        // 初始化背包
        PlayerInventoryManager.getInstance().init(player);

        // 初始化玩家记分板
        ScoreBoardManager.getInstance().joinPlayer(player);

        // 初始化玩家状态
        FFAUtil.initializePlayer(player);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();

        // 卸载玩家记分板
        ScoreBoardManager.getInstance().removePlayer(player);

        // 卸载玩家数据缓存
        FPlayerDataStorage.getInstance().clearUserCacheData(player);

        // 初始化玩家状态
        FFAUtil.initializePlayer(player);

        // 移出玩家队列
        PracticeQueueDataStorage.getInstance().removeQueue(player.getName());
        PlayerInventoryManager.getInstance().remove(player);
    }

    @EventHandler
    public void PlayerRespawnEvent(PlayerRespawnEvent e){
        Player player = e.getPlayer();

        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(),PluginConfig.FFA_SITE.LOCATION.get().getX(),PluginConfig.FFA_SITE.LOCATION.get().getY()+1,PluginConfig.FFA_SITE.LOCATION.get().getZ()));

        // 初始化玩家状态
        FFAUtil.initializePlayer(player);

        // 初始化背包
        PlayerInventoryManager.getInstance().init(player);
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();

        // 初始化背包
        PlayerInventoryManager.getInstance().init(player);

        if (FFAUtil.isItemRange(e.getTo(),PluginConfig.FFA_SITE.LOCATION.get(),PluginConfig.FFA_SITE.RADIUS.get())){
            if (CombatManager.getInstance().isCombat(player)){
                player.teleport(e.getFrom());
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e){
        if (e.getEntity() instanceof Player p){
            // 判断玩家是否在安全区外面
            if (FFAUtil.isRange(p,PluginConfig.FFA_SITE.LOCATION.get(),PluginConfig.FFA_SITE.RADIUS.get())){
                p.setFoodLevel(20);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event) {
        // 禁止玩家丢弃物品
        event.setCancelled(true);
    }

    @EventHandler
    public void PlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        // 玩家使用药水后立马将空瓶子清除掉
        if (event.getItem().getType().equals(Material.POTION)) {
            Bukkit.getScheduler().runTaskLater(MiarsPracticeFFA.getInstance(), () -> event.getPlayer().getInventory().remove(Material.GLASS_BOTTLE), 2);
        }
    }

    @EventHandler
    public void PlayerTeleportEvent(PlayerTeleportEvent event) {
        // 防止玩家使用末影珍珠强制进入安全区
        if (FFAUtil.isItemRange(event.getTo(),PluginConfig.FFA_SITE.LOCATION.get(),PluginConfig.FFA_SITE.RADIUS.get())){
            if(event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                event.setCancelled(true);
            }
        }
    }

    HashMap<UUID,Long> hashMap = new HashMap<>();
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack itemStack = e.getItem();
        Block block = e.getClickedBlock();
        Action action = e.getAction();

        if (itemStack!=null){
            // 物品的交互
            ItemInteractManager.getInstance().init(itemStack,player);

            // 末影珍珠的使用 Todo 写成manager
            if (action.equals(Action.RIGHT_CLICK_AIR)||action.equals(Action.RIGHT_CLICK_BLOCK)){
                if (itemStack.getType().equals(Material.ENDER_PEARL)){
                    if (player.getGameMode().equals(GameMode.SURVIVAL)){
                        if (hashMap.containsKey(player.getUniqueId())){
                            if (System.currentTimeMillis()-hashMap.get(player.getUniqueId())>=15000){
                                hashMap.put(player.getUniqueId(),System.currentTimeMillis());
                            }else {
                                player.sendMessage(ColorParser.parse("&7您暂时无法使用 &c末影珍珠 &7还需要等待 " + ToolUtils.getDate((15000-(System.currentTimeMillis()-hashMap.get(player.getUniqueId())))/1000) + " &7才能使用。"));
                                player.playSound(player.getLocation(), Sound.VILLAGER_NO,1,1);
                                e.setCancelled(true);
                            }
                        }else {
                            hashMap.put(player.getUniqueId(),System.currentTimeMillis());
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void InventoryOpenEvent(InventoryOpenEvent e){
        Player player = (Player) e.getPlayer();
        if (e.getInventory().getType().equals(InventoryType.ENDER_CHEST)){
            // 打开建筑仓库
            BlockGUI.open(player);
            e.setCancelled(true);
        }
    }

    /**
     * 禁止玩家移动物品
     */
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        Player player = e.getWhoClicked().getKiller();
        ItemStack itemStack = e.getCurrentItem();

        if (itemStack!=null && itemStack.getType()!=Material.AIR){
            NBTItem nbtItem = new NBTItem(itemStack);
            if (nbtItem.getBoolean("stopClick")){
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            Player deathPlayer = e.getEntity(); // 死亡的玩家
            Player attackPlayer = e.getEntity().getKiller(); // 击杀的玩家


            deathPlayer.getInventory().clear();
            FPlayer deathFPlayer = FPlayerDataStorage.getInstance().getFPlayer(deathPlayer);
            deathFPlayer.setDeathCount(deathFPlayer.getDeathCount()+1);
            deathFPlayer.setUpdateTime(new Date(System.currentTimeMillis()));
            FPlayerDataStorage.getInstance().putFPlayer(deathFPlayer);

            if (attackPlayer!=null){
                FPlayer attackFPlayer = FPlayerDataStorage.getInstance().getFPlayer(attackPlayer);
                attackFPlayer.setKillsCount(attackFPlayer.getKillsCount()+1);
                attackFPlayer.setUpdateTime(new Date(System.currentTimeMillis()));
                FPlayerDataStorage.getInstance().putFPlayer(attackFPlayer);
                FFAUtil.initializePlayer(attackPlayer);
            }
        }


        e.setDeathMessage(null);
        e.setKeepInventory(true);
    }

    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(e.getPlayer());
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
        e.setFormat(ColorParser.parse(mRank.getPrefix()+mRank.getNameColor()+"%1$s&f: %2$s"));
    }
}
