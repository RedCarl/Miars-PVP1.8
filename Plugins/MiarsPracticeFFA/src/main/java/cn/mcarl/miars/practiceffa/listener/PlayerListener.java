package cn.mcarl.miars.practiceffa.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.manager.EnderPearlManager;
import cn.mcarl.miars.core.utils.MiarsUtils;
import cn.mcarl.miars.practiceffa.entity.GamePlayer;
import cn.mcarl.miars.practiceffa.manager.*;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.practiceffa.MiarsPracticeFFA;
import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import cn.mcarl.miars.storage.storage.data.practice.*;
import cn.mcarl.miars.practiceffa.ui.BlockGUI;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.sql.Date;

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

        // 初始化数据
        GamePlayer gamePlayer = GamePlayer.get(player);
        PracticeQueueDataStorage.getInstance().init();

        // 初始化玩家记分板
        ScoreBoardManager.getInstance().joinPlayer(player);

        // 初始化头衔
        MiarsUtils.initPlayerNametag(player,false);

        for (FKitType ft:FKitType.values()) {
            // 初始化Kit
            if (FKitDataStorage.getInstance().getFKitData(player.getUniqueId(),ft).size()==0) {
                FKitDataStorage.getInstance().putFKitData(
                        new FKit (
                                null,
                                player.getUniqueId().toString(),
                                ft,
                                "Loadout 1",
                                FFAUtil.getByFKitType(ft),
                                0,
                                null,
                                new Date(System.currentTimeMillis())
                        )
                );
            }
        }

    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();

        // 卸载玩家记分板
        ScoreBoardManager.getInstance().removePlayer(player);

        // 卸载玩家数据缓存
        FPlayerDataStorage.getInstance().clearUserCacheData(player);
        GamePlayer.remove(player);

        // 移出玩家队列
        PracticeQueueDataStorage.getInstance().removeQueue(player.getName());
        RankScoreDataStorage.getInstance().clearUserCacheData(player.getUniqueId(),1);
    }

    @EventHandler
    public void PlayerRespawnEvent(PlayerRespawnEvent e){
        Player player = e.getPlayer();

        player.setGameMode(GameMode.SURVIVAL);

        e.setRespawnLocation(new Location(PluginConfig.FFA_SITE.LOCATION.getNotNull().getWorld(),PluginConfig.FFA_SITE.LOCATION.getNotNull().getX(),PluginConfig.FFA_SITE.LOCATION.getNotNull().getY()+1,PluginConfig.FFA_SITE.LOCATION.getNotNull().getZ()));

        GamePlayer.init(player);
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();

        if (CombatManager.getInstance().isCombat(player)){
            if (FFAUtil.isItemRange(e.getTo(),PluginConfig.FFA_SITE.LOCATION.getNotNull(),PluginConfig.FFA_SITE.RADIUS.getNotNull())){

                player.teleport(e.getFrom());
                Vector v = player.getVelocity();
                // 写死的，需要后期改善
                if (player.getLocation().getX()<=-50){
                    v.setX(-0.5);
                    v.setY(0.5);
                }else if (player.getLocation().getZ()<=-50 ){
                    v.setZ(-0.5);
                    v.setY(0.5);
                }else if (player.getLocation().getX()>=50){
                    v.setX(0.5);
                    v.setY(0.5);
                }else if (player.getLocation().getZ()>=50 ){
                    v.setZ(0.5);
                    v.setY(0.5);
                }
                player.setVelocity(v);

                e.setCancelled(true);
            }
        }else {
            GamePlayer.get(player).initData();
        }

    }

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e){
        if (e.getEntity() instanceof Player p){
            // 判断玩家是否在安全区外面
            if (FFAUtil.isRange(p,PluginConfig.FFA_SITE.LOCATION.getNotNull(),PluginConfig.FFA_SITE.RADIUS.getNotNull())){
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
        if (FFAUtil.isItemRange(event.getTo(),PluginConfig.FFA_SITE.LOCATION.getNotNull(),PluginConfig.FFA_SITE.RADIUS.getNotNull())){
            if(event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                event.setCancelled(true);
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

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent e) {

        Player deathPlayer = e.getEntity(); // 死亡的玩家
        if (e.getEntity().getKiller() != null) {
            Player attackPlayer = e.getEntity().getKiller(); // 击杀的玩家


            deathPlayer.getInventory().clear();
            FPlayer deathFPlayer = FPlayerDataStorage.getInstance().getFPlayer(deathPlayer);
            deathFPlayer.addDeathCount();

            if (attackPlayer!=null){
                FPlayer attackFPlayer = FPlayerDataStorage.getInstance().getFPlayer(attackPlayer);
                attackFPlayer.addKillsCount();
                attackPlayer.setHealth(20);
            }
        }

        DeathChestManager.getInstance().generateChests(deathPlayer,deathPlayer.getLocation());

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
