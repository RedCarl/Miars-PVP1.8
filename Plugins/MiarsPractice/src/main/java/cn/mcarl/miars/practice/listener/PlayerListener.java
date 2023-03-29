package cn.mcarl.miars.practice.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.core.utils.MiarsUtil;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.core.utils.jsonmessage.JSONMessage;
import cn.mcarl.miars.practice.MiarsPractice;
import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.practice.manager.*;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.PlayerState;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeDailyStreakDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeGameDataStorage;
import cn.mcarl.miars.storage.utils.BukkitUtils;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerListener implements Listener {



    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

        ToolUtils.playerInitialize(player);
        player.getInventory().clear();
        player.setLevel(0);

        MiarsUtil.initPlayerNametag(player,false);

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player);

        if (state==null){
            if (!player.hasPermission("miars.admin")){
                player.kickPlayer("&c意外的错误");
            }
        }else {

            Arena arena = ArenaManager.getInstance().getArenaById(state);

            if (state.getPlayerA().equals(player.getName())){
                player.teleport(arena.getLoc1());
            }else {
                player.teleport(arena.getLoc2());
            }

            // 初始化玩家记分板
            ScoreBoardManager.getInstance().joinPlayer(player);
            // 初始背包
            PlayerInventoryManager.getInstance().init(player);

            // 游戏准备
            ArenaManager.getInstance().readyGame(state);

            // 比赛开始提示
            if ((Bukkit.getPlayer(state.getPlayerA()) !=null && Bukkit.getPlayer(state.getPlayerA()).isOnline()) && (Bukkit.getPlayer(state.getPlayerB()) != null && Bukkit.getPlayer(state.getPlayerB()).isOnline())){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 5; i++) {
                            try {
                                JSONMessage.create(ColorParser.parse("&7比赛将在 &c"+(5-i)+" &7秒后正式开始..."))
                                        .send(Bukkit.getPlayer(state.getPlayerA()),Bukkit.getPlayer(state.getPlayerB()));
                                if (i == 4){
                                    ArenaManager.getInstance().startGame(state);

                                    JSONMessage.create(ColorParser.parse("&7比赛开始，祝你好运！"))
                                            .send(Bukkit.getPlayer(state.getPlayerA()),Bukkit.getPlayer(state.getPlayerB()));
                                    break;
                                }
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        cancel();
                    }
                }.runTaskAsynchronously(MiarsPractice.getInstance());
            }

        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();

        if (player.getGameMode()==GameMode.CREATIVE){return;}

        // 卸载玩家记分板
        ScoreBoardManager.getInstance().removePlayer(player);

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player);

        if (state!=null){
            // 战斗中途退出
            if (state.getState() == ArenaState.State.GAME){
                Player deathPlayer = e.getPlayer();
                Player attackPlayer = state.getPlayerA().equals(deathPlayer.getName()) ? Bukkit.getPlayer(state.getPlayerB()) : Bukkit.getPlayer(state.getPlayerA());

                ArenaManager.getInstance().endGame(attackPlayer,deathPlayer);
            }
        }

    }


    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();
        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player);

        // 判断玩家是否跌落出竞技场
        if (!player.hasPermission("miars.admin") && player.getLocation().getY()<0){
            Arena arena = ArenaManager.getInstance().getArenaById(state);
            if (state.getPlayerA().equals(player.getName())){
                player.teleport(arena.getLoc1());
            }else {
                player.teleport(arena.getLoc2());
            }
        }
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent e){

        e.setDeathMessage(null);
        e.setKeepInventory(true);

        Player deathPlayer = e.getEntity(); // 死亡的玩家
        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(deathPlayer);
        deathPlayer.spigot().respawn();

        Player attackPlayer;
        if (e.getEntity().getKiller() != null) {
            attackPlayer = e.getEntity().getKiller(); // 击杀的玩家
        }else {
            attackPlayer = deathPlayer.getName().equals(state.getPlayerA()) ? Bukkit.getPlayer(state.getPlayerB()):Bukkit.getPlayer(state.getPlayerA());
        }

        ArenaManager.getInstance().endGame(attackPlayer,deathPlayer);

    }

    @EventHandler
    public void PlayerRespawnEvent(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        e.setRespawnLocation(player.getLocation());
        ToolUtils.playerInitialize(player);
    }

    /**
     * 禁止玩家移动物品
     */
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (player.getGameMode()==GameMode.CREATIVE){return;}

        ItemStack itemStack = e.getCurrentItem();

        if (itemStack!=null && itemStack.getType()!= Material.AIR){
            NBTItem nbtItem = new NBTItem(itemStack);
            if (nbtItem.getBoolean("stopClick")){
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(e.getPlayer());
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
        e.setFormat(ColorParser.parse(mRank.getPrefix()+mRank.getNameColor()+"%1$s&f: %2$s"));
    }

    HashMap<UUID,Long> hashMap = new HashMap<>();
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack itemStack = e.getItem();
        Block block = e.getClickedBlock();
        Action action = e.getAction();
        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player);

        if (itemStack!=null && state!=null){
            // 物品的交互
            ItemInteractManager.getInstance().init(itemStack,player);

//            if (state.getState()!= ArenaState.State.GAME){
//                e.setCancelled(true);
//                return;
//            }

            // 末影珍珠的使用 Todo 写成manager
            if (action.equals(Action.RIGHT_CLICK_AIR)||action.equals(Action.RIGHT_CLICK_BLOCK)){
                if (itemStack.getType().equals(Material.ENDER_PEARL)){
                    if (player.getGameMode().equals(GameMode.SURVIVAL)){
                        if (hashMap.containsKey(player.getUniqueId())){
                            if (!((System.currentTimeMillis()-hashMap.get(player.getUniqueId()))>=12000)){
                                //player.sendMessage(ColorParser.parse("&7您暂时无法使用 &c末影珍珠 &7还需要等待 " + ToolUtils.getDate((15000-(System.currentTimeMillis()-hashMap.get(player.getUniqueId())))/1000) + " &7才能使用。"));
                                player.playSound(player.getLocation(), Sound.VILLAGER_NO,1,1);
                                e.setCancelled(true);
                            }
                        }else {
                            hashMap.put(player.getUniqueId(),System.currentTimeMillis());
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (hashMap.containsKey(player.getUniqueId())){
                                        if (!((System.currentTimeMillis()-hashMap.get(player.getUniqueId()))>=12000)){
                                            player.setLevel((int) ((12000-(System.currentTimeMillis()-hashMap.get(player.getUniqueId())))/1000));
                                        }else {
                                            hashMap.remove(player.getUniqueId());
                                            cancel();
                                        }

                                    }
                                }
                            }.runTaskTimerAsynchronously(MiarsPractice.getInstance(),0,2);
                            ToolUtils.reduceXpBar(player,12*20);
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void PlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        // 玩家使用药水后立马将空瓶子清除掉
        if (event.getItem().getType().equals(Material.POTION)) {
            Bukkit.getScheduler().runTaskLater(MiarsPractice.getInstance(), () -> event.getPlayer().getInventory().remove(Material.GLASS_BOTTLE), 2);
        }
    }

    @EventHandler
    public void BlockPlaceEvent(BlockPlaceEvent e){
        Player player = e.getPlayer();

        if (player.getGameMode()==GameMode.CREATIVE){return;}

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player);
        Arena arena = ArenaManager.getInstance().getArenaById(state);


        e.setCancelled(BuildUHCManager.getInstance().placeBlock(arena,state,e.getBlock()));

    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e){
        Player player = e.getPlayer();

        if (player.getGameMode()==GameMode.CREATIVE){return;}

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player);
        Arena arena = ArenaManager.getInstance().getArenaById(state);

        e.setCancelled(BuildUHCManager.getInstance().breakBlock(arena,state,e.getBlock()));

    }

    @EventHandler
    public void PlayerBucketEmptyEvent(PlayerBucketEmptyEvent e){
        Player player = e.getPlayer();
        Block block = e.getBlockClicked().getRelative(e.getBlockFace());

        if (player.getGameMode()==GameMode.CREATIVE){return;}

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player);
        Arena arena = ArenaManager.getInstance().getArenaById(state);

        e.setCancelled(BuildUHCManager.getInstance().placeBlock(arena,state,block));
    }

    @EventHandler
    public void PlayerBucketFillEvent(PlayerBucketFillEvent e){
        Player player = e.getPlayer();
        Block block = e.getBlockClicked().getRelative(e.getBlockFace());

        if (player.getGameMode()==GameMode.CREATIVE){return;}

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player);
        Arena arena = ArenaManager.getInstance().getArenaById(state);

        e.setCancelled(BuildUHCManager.getInstance().breakBlock(arena, state, block));
    }

    @EventHandler
    public void BlockFromToEvent(BlockFormEvent e){
        System.out.println(e.getBlock().getType().name());
        e.setCancelled(true);
    }

    @EventHandler
    public void BlockSpreadEvent(BlockSpreadEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void BlockBurnEvent(BlockBurnEvent e){
        e.setCancelled(true);
    }


    @EventHandler
    public void onFromTo(BlockFromToEvent e){
        Collection<Entity> entities = e.getBlock().getLocation().getWorld().getNearbyEntities(e.getBlock().getLocation(),32,256,32);

        if (entities.size()!=0){
            for (Entity entity:entities) {
                if (entity instanceof Player player){
                    ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player);
                    if (state==null){
                        continue;
                    }
                    Arena arena = ArenaManager.getInstance().getArenaById(state);

                    BuildUHCManager.getInstance().placeBlock(arena,state,e.getToBlock());

                    break;
                }
            }
        }else {
            e.setCancelled(true);
        }

    }


}
