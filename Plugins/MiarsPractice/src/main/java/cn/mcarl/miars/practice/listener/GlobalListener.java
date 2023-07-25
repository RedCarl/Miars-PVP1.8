package cn.mcarl.miars.practice.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.utils.MiarsUtils;
import cn.mcarl.miars.practice.MiarsPractice;
import cn.mcarl.miars.practice.entity.GamePlayer;
import cn.mcarl.miars.practice.manager.*;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.FKitDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.FPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import cn.mcarl.miars.storage.utils.ToolUtils;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.*;

public class GlobalListener implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();

        ToolUtils.playerInitialize(player);
        player.getInventory().clear();
        player.setLevel(0);

        MiarsUtils.initPlayerNametag(player,false);

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player.getName());

        if (state==null){
            if (!player.hasPermission("miars.admin")){
                player.kickPlayer("&c意外的错误");
            }
        }else {
            Arena arena = ArenaManager.getInstance().getArenaById(state);

            if (state.getPlayerA().equals(player.getName())){
                Location location = arena.getLoc1();
                location.setWorld(Bukkit.getWorld(arena.getWorld()));
                player.teleport(location);
            }else {
                Location location = arena.getLoc2();
                location.setWorld(Bukkit.getWorld(arena.getWorld()));
                player.teleport(location);
            }

            // 初始化玩家记分板
            ScoreBoardManager.getInstance().joinPlayer(player);

            // 初始背包
            PlayerInventoryManager.getInstance().init(player);
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();

        if (player.getGameMode()==GameMode.CREATIVE){return;}

        // 卸载玩家记分板
        ScoreBoardManager.getInstance().removePlayer(player);

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player.getName());

        if (state!=null){
            // 战斗中途退出
            if (state.getState() != ArenaState.State.END){
                Player deathPlayer = player;
                String attackName = deathPlayer.getName().equals(state.getPlayerA()) ? state.getPlayerB() : state.getPlayerA();

                Player attackPlayer = Bukkit.getPlayer(attackName);

                if (attackPlayer==null){
                    ArenaManager.getInstance().endGame(state);
                }else {
                    ArenaManager.getInstance().endGame(
                            new GamePlayer(
                                    attackPlayer.getUniqueId(),
                                    attackPlayer.getName(),
                                    attackPlayer.getHealth(),
                                    attackPlayer.getFoodLevel(),
                                    attackPlayer.getActivePotionEffects()
                            ),
                            new GamePlayer(
                                    deathPlayer.getUniqueId(),
                                    deathPlayer.getName(),
                                    deathPlayer.getHealth(),
                                    deathPlayer.getFoodLevel(),
                                    deathPlayer.getActivePotionEffects()
                            )
                    );
                }
            }
        }

    }

    @EventHandler
    public void PlayerKickEvent(PlayerKickEvent e){
        Player player = e.getPlayer();

        if (player.getGameMode()==GameMode.CREATIVE){return;}

        // 卸载玩家记分板
        ScoreBoardManager.getInstance().removePlayer(player);

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player.getName());

        if (state!=null){
            // 战斗中途退出
            if (state.getState() != ArenaState.State.END){
                Player deathPlayer = player;
                String attackName = deathPlayer.getName().equals(state.getPlayerA()) ? state.getPlayerB() : state.getPlayerA();

                Player attackPlayer = Bukkit.getPlayer(attackName);

                if (attackPlayer == null){
                    ArenaManager.getInstance().endGame(state);
                }else {
                    ArenaManager.getInstance().endGame(
                            new GamePlayer(
                                    attackPlayer.getUniqueId(),
                                    attackPlayer.getName(),
                                    attackPlayer.getHealth(),
                                    attackPlayer.getFoodLevel(),
                                    attackPlayer.getActivePotionEffects()
                            ),
                            new GamePlayer(
                                    deathPlayer.getUniqueId(),
                                    deathPlayer.getName(),
                                    deathPlayer.getHealth(),
                                    deathPlayer.getFoodLevel(),
                                    deathPlayer.getActivePotionEffects()
                            )
                    );
                }
            }
        }

    }


    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();
        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player.getName());

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
        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(deathPlayer.getName());

//        deathPlayer.spigot().respawn(); // 自动重生
        deathPlayer.setHealth(20);
        deathPlayer.getLocation().getWorld().strikeLightning(deathPlayer.getLocation());// 死亡霹雷

        FPlayer deathFPlayer = FPlayerDataStorage.getInstance().getFPlayer(deathPlayer);
        deathFPlayer.addDeathCount();

        Player attackPlayer;
        if (e.getEntity().getKiller() != null) {
            attackPlayer = e.getEntity().getKiller(); // 击杀的玩家

        }else {
            attackPlayer = deathPlayer.getName().equals(state.getPlayerA()) ? Bukkit.getPlayer(state.getPlayerB()):Bukkit.getPlayer(state.getPlayerA());
        }
        FPlayer attackFPlayer = FPlayerDataStorage.getInstance().getFPlayer(attackPlayer);
        attackFPlayer.addKillsCount();


        ArenaManager.getInstance().endGame(
                new GamePlayer(
                        attackPlayer.getUniqueId(),
                        attackFPlayer.getName(),
                        attackPlayer.getHealth(),
                        attackPlayer.getFoodLevel(),
                        attackPlayer.getActivePotionEffects()
                ),
                new GamePlayer(
                        deathPlayer.getUniqueId(),
                        deathPlayer.getName(),
                        deathPlayer.getHealth(),
                        deathPlayer.getFoodLevel(),
                        deathPlayer.getActivePotionEffects()
                )
                );

    }

    @EventHandler
    public void PlayerRespawnEvent(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        e.setRespawnLocation(player.getLocation());
        ToolUtils.playerInitialize(player);
    }

    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(e.getPlayer());
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
        e.setFormat(ColorParser.parse(mRank.getPrefix()+mRank.getNameColor()+"%1$s&f: %2$s"));
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

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player.getName());
        Arena arena = ArenaManager.getInstance().getArenaById(state);


        e.setCancelled(BuildUHCManager.getInstance().placeBlock(arena,state,e.getBlock()));

    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e){
        Player player = e.getPlayer();

        if (player.getGameMode()==GameMode.CREATIVE){return;}

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player.getName());
        Arena arena = ArenaManager.getInstance().getArenaById(state);

        e.setCancelled(BuildUHCManager.getInstance().breakBlock(arena,state,e.getBlock()));

    }

    @EventHandler
    public void PlayerBucketEmptyEvent(PlayerBucketEmptyEvent e){
        Player player = e.getPlayer();
        Block block = e.getBlockClicked().getRelative(e.getBlockFace());

        if (player.getGameMode()==GameMode.CREATIVE){return;}

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player.getName());
        Arena arena = ArenaManager.getInstance().getArenaById(state);

        e.setCancelled(BuildUHCManager.getInstance().placeBlock(arena,state,block));
    }

    @EventHandler
    public void PlayerBucketFillEvent(PlayerBucketFillEvent e){
        Player player = e.getPlayer();
        Block block = e.getBlockClicked().getRelative(e.getBlockFace());

        if (player.getGameMode()==GameMode.CREATIVE){return;}

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player.getName());
        Arena arena = ArenaManager.getInstance().getArenaById(state);

        e.setCancelled(BuildUHCManager.getInstance().breakBlock(arena, state, block));
    }

    @EventHandler
    public void onFromTo(BlockFromToEvent e){
        Collection<Entity> entities = e.getBlock().getLocation().getWorld().getNearbyEntities(e.getBlock().getLocation(),32,256,32);

        if (entities.size()!=0){
            for (Entity entity:entities) {
                if (entity instanceof Player player){
                    ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player.getName());
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

    // Boxing
    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player player) {
            // 判断战斗是否已经结束
            ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(player.getName());
            if (state != null && state.getState()!= ArenaState.State.GAME){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerPickupItemEvent(PlayerPickupItemEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void BlockFromToEvent(BlockFormEvent e){
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
}
