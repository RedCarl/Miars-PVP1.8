package cn.mcarl.miars.skypvp.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.skypvp.MiarsSkyPVP;
import cn.mcarl.miars.skypvp.conf.PluginConfig;
import cn.mcarl.miars.skypvp.entity.GamePlayer;
import cn.mcarl.miars.skypvp.items.SpawnSlimeball;
import cn.mcarl.miars.skypvp.manager.CombatManager;
import cn.mcarl.miars.skypvp.manager.LuckyManager;
import cn.mcarl.miars.skypvp.manager.ScoreBoardManager;
import cn.mcarl.miars.skypvp.manager.SpawnManager;
import cn.mcarl.miars.skypvp.utils.PlayerUtils;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.entity.ffa.FCombatInfo;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.storage.data.skypvp.SkyPVPDataStorage;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;

import java.util.*;

public class PlayerListener implements Listener {

    @EventHandler
    public void PlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent e){
        Player player = e.getPlayer();
        if (e.getRightClicked() instanceof ArmorStand stand){
            if (stand.getCustomName().contains("miars_lucky")){
                e.setCancelled(true);

                LuckyManager.getInstance().useBlock(stand,player);
            }
        }
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack itemStack = e.getItem();
        if (itemStack!=null &&
                (
                        itemStack.getType().equals(Material.LAVA_BUCKET) ||
                                itemStack.getType().equals(Material.BUCKET) ||
                                itemStack.getType().equals(Material.WATER_BUCKET)
                )
        ){
            if (!player.hasPermission("miars.admin")){
                e.setCancelled(true);
            }
        }

    }

    Map<UUID,Boolean> protectedRegion = new HashMap<>();

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();
        for (Entity entity:player.getWorld().getNearbyEntities(player.getLocation(),0.5,1,0.5)) {
            if (entity instanceof ArmorStand stand){
                if (stand.getCustomName().contains("miars_lucky")){
                    LuckyManager.getInstance().useBlock(stand,player);
                }
            }
        }

        // 移除回城
        if (e.getFrom().getZ() != e.getTo().getZ() && e.getFrom().getX() != e.getTo().getX()) {
            SpawnManager.getInstance().remove(player);
        }

        // 掉入虚空秒死
        if (player.getLocation().getY()<0){
            player.damage(player.getMaxHealth()*9999,player.getKiller());
        }

        // 是否在安全区
        if (protectedRegion.containsKey(player.getUniqueId())){
            if (PlayerUtils.isProtectedRegion(player)){
                if (!protectedRegion.get(player.getUniqueId())){
                    player.sendMessage(ColorParser.parse("&a&l安全! &7您进入了安全区,这里您将绝对安全。"));
                    protectedRegion.put(player.getUniqueId(),true);
                }
            }else {
                if (protectedRegion.get(player.getUniqueId())){
                    player.sendMessage(ColorParser.parse("&c&l危险! &7您走出了安全区,随时会有危险。"));
                    protectedRegion.put(player.getUniqueId(),false);
                }
            }
        }else {
            protectedRegion.put(player.getUniqueId(),PlayerUtils.isProtectedRegion(player));
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if (!SkyPVPDataStorage.getInstance().checkSPlayer(player) || PlayerUtils.isNullInv(player)){
            PlayerUtils.initializePlayer(player);
        }else {
            new SpawnSlimeball().give(player,8);
            player.setGameMode(GameMode.SURVIVAL);
        }
        SkyPVPDataStorage.getInstance().getSPlayer(player);
        ScoreBoardManager.getInstance().joinPlayer(player);

        player.teleport(PluginConfig.PROTECTED_REGION.SPAWN.get());
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();
        ScoreBoardManager.getInstance().removePlayer(player);
        if (CombatManager.getInstance().isCombat(player)){
            player.damage(player.getMaxHealth()*9999,player.getKiller());
        }
    }

    @EventHandler
    public void PlayerKickEvent(PlayerKickEvent e){
        Player player = e.getPlayer();
        ScoreBoardManager.getInstance().removePlayer(player);
        if (CombatManager.getInstance().isCombat(player)){
            player.damage(player.getMaxHealth()*9999,player.getKiller());
        }
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e){
        if (e.getEntity() instanceof Player player){
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Player entity){ // 被攻击者
            if (CitizensAPI.getNPCRegistry().isNPC(entity)){e.setCancelled(true);return;};
            if (e.getDamager() instanceof Player player){// 攻击者

                if (CitizensAPI.getNPCRegistry().isNPC(player)){e.setCancelled(true);return;};

                if (PlayerUtils.isProtectedRegion(player) || PlayerUtils.isProtectedRegion(entity)){
                    e.setCancelled(true);
                    e.setDamage(0);
                }else {
                    CombatManager.getInstance().start(entity,player.getUniqueId().toString(),30);
                    CombatManager.getInstance().start(player,entity.getUniqueId().toString(),30);
                }


                if (entity instanceof ArmorStand stand){// 幸运方块
                    LuckyManager.getInstance().useBlock(stand,player);
                }
            }
            if (e.getDamager() instanceof Arrow arrow){// 箭
                ProjectileSource shooter = arrow.getShooter();
                checkDamager(shooter,entity,e);
            }
            if (e.getDamager() instanceof Snowball snowball){// 雪球
                ProjectileSource shooter = snowball.getShooter();
                checkDamager(shooter,entity,e);
            }
            if (e.getDamager() instanceof Egg egg){// 鸡蛋
                ProjectileSource shooter = egg.getShooter();
                checkDamager(shooter,entity,e);
            }
            if (e.getDamager() instanceof FishHook fishHook){// 鱼竿
                ProjectileSource shooter = fishHook.getShooter();
                checkDamager(shooter,entity,e);
            }
        }
    }
    public void checkDamager(
            ProjectileSource shooter,
            Player entity,
            EntityDamageByEntityEvent e
    ){
        if(shooter instanceof Player player) {
            if (PlayerUtils.isProtectedRegion(player) || PlayerUtils.isProtectedRegion(entity)){
                e.setCancelled(true);
                e.setDamage(0);
            }else {
                CombatManager.getInstance().start(entity,player.getUniqueId().toString(),30);
                CombatManager.getInstance().start(player,entity.getUniqueId().toString(),30);
            }
        }
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent e){
        if (e.getEntity().getKiller() != null) {
            Player deathPlayer = e.getEntity(); // 死亡的玩家
            Player attackPlayer = e.getEntity().getKiller(); // 击杀的玩家

            if (attackPlayer!=null){
                CombatManager.getInstance().clear(attackPlayer);
                e.setDeathMessage(ColorParser.parse("&c&l死亡! &7"+deathPlayer.getName()+" 被 "+attackPlayer.getName()+" 击杀了。"));

                // 结算
                GamePlayer.get(deathPlayer).addDeathCount();
                GamePlayer.get(attackPlayer).addKillsCount();
                long coin = new Random().nextLong(4)+1;
                MiarsCore.getEcon().depositPlayer(attackPlayer,coin);
                MiarsCore.getEcon().withdrawPlayer(deathPlayer,coin);
                attackPlayer.sendMessage(ColorParser.parse("&6&l奖励! &7从敌人身上缴获了 &6"+coin+" &7硬币。"));
                Long exp = new Random().nextLong(80)+20;
                GamePlayer.get(attackPlayer).addExp(exp);
                attackPlayer.sendMessage(ColorParser.parse("&e&l经验! &7战斗中获得了 &e"+exp+" &7经验。"));

            }else {
                FCombatInfo info = CombatManager.getInstance().getCombatInfo(deathPlayer);
                e.setDeathMessage(ColorParser.parse("&c&l死亡! &7"+deathPlayer.getName()+" 被 "+Bukkit.getOfflinePlayer(info.getOpponent()).getName()+" 推入了深渊..."));
            }
        }else {
            e.setDeathMessage(null);
        }

        List<ItemStack> drops = new ArrayList<>(e.getDrops());
        Location location = e.getEntity().getLocation();

        if (e.getEntity().getLocation().getY()>0){
            for (ItemStack i:drops) {
                NBTItem nbt = new NBTItem(i);
                if (!nbt.getBoolean("no-drop")){
                    location.getWorld().dropItemNaturally(location,i);
                }
            }
        }

        e.getDrops().clear();
        e.getEntity().spigot().respawn();
    }

    @EventHandler
    public void PlayerRespawnEvent(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        if (player.getLocation().getY()<=0){
            e.setRespawnLocation(PluginConfig.PROTECTED_REGION.SPAWN.get());
        }else {
            e.setRespawnLocation(player.getLocation());
        }

        player.setGameMode(GameMode.SPECTATOR);
        new BukkitRunnable() {
            int i = 3;
            @Override
            public void run() {
                if (i==0){
                    player.teleport(PluginConfig.PROTECTED_REGION.SPAWN.get());
                    player.setGameMode(GameMode.SURVIVAL);

                    player.sendTitle(new Title(
                            ColorParser.parse("&a&l您已复活!"),
                            ColorParser.parse("&7请珍惜每一次生命。"),
                            0,
                            20,
                            5
                    ));
                    PlayerUtils.initializePlayer(player);
                    cancel();
                }else {
                    player.sendTitle(new Title(
                            ColorParser.parse("&c&l您已死亡!"),
                            ColorParser.parse("&7请等待 &c"+i+" &7秒后复活。"),
                            0,
                            20,
                            5
                    ));
                }
                i--;
            }
        }.runTaskTimer(MiarsSkyPVP.getInstance(),0,20);
    }

    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(e.getPlayer());
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
        e.setFormat(ColorParser.parse(GamePlayer.get(e.getPlayer()).getLevelString()+" "+mRank.getPrefix()+mRank.getNameColor()+"%1$s&f: %2$s"));
    }

    @EventHandler
    public void EntityChangeBlockEvent(EntityChangeBlockEvent e) {
        if (e.getBlock().getType().equals(Material.ANVIL)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerBucketFillEvent(PlayerBucketFillEvent e){
        Player player = e.getPlayer();
        if (!player.hasPermission("miars.admin")){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void PlayerBucketEmptyEvent(PlayerBucketEmptyEvent e){
        Player player = e.getPlayer();
        if (!player.hasPermission("miars.admin")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void ChunkUnloadEvent(ChunkUnloadEvent e) {
        if(LuckyManager.getInstance().getChunks().contains(e.getChunk())){
            e.setCancelled(true);
        }
    }
}
