package cn.mcarl.miars.practice.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.core.utils.jsonmessage.JSONMessage;
import cn.mcarl.miars.practice.MiarsPractice;
import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.practice.manager.ArenaManager;
import cn.mcarl.miars.practice.manager.ItemInteractManager;
import cn.mcarl.miars.practice.manager.PlayerInventoryManager;
import cn.mcarl.miars.practice.manager.ScoreBoardManager;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeGameDataStorage;
import cn.mcarl.miars.storage.utils.BukkitUtils;
import com.google.gson.Gson;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PlayerListener implements Listener {

    private final Gson gson = new Gson();
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();
        ToolUtils.playerInitialize(player);
        player.getInventory().clear();

        ArenaState state = ArenaManager.getInstance().getArenaStateByPlayer(player);
        if (state==null){
            player.kickPlayer("&c意外的错误");
        }else {
            Arena arena = ArenaManager.getInstance().getArenaById(state.getArenaId());
            if (state.getPlayerA().equals(player.getName())){
                player.teleport(arena.getLoc1());
            }else {
                player.teleport(arena.getLoc2());
            }

            // 初始化玩家记分板
            ScoreBoardManager.getInstance().joinPlayer(player);
            // 初始背包
            PlayerInventoryManager.getInstance().init(player);

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
                                    ArenaManager.getInstance().startGame(state.getArenaId());

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

        // 卸载玩家记分板
        ScoreBoardManager.getInstance().removePlayer(player);

        // 清除战场
        ArenaState arenaState = ArenaManager.getInstance().getArenaStateByPlayer(player);
        if (arenaState!=null){
            ArenaManager.getInstance().releaseArena(ArenaManager.getInstance().getArenaStateByPlayer(player).getArenaId());
        }

    }


    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();
        ArenaState state = ArenaManager.getInstance().getArenaStateByPlayer(player);

        // 判断玩家是否跌落出竞技场
        if (!player.hasPermission("miars.admin") && player.getLocation().getY()<0){
            Arena arena = ArenaManager.getInstance().getArenaById(state.getArenaId());
            if (state.getPlayerA().equals(player.getName())){
                player.teleport(arena.getLoc1());
            }else {
                player.teleport(arena.getLoc2());
            }
        }
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent e){
        if (e.getEntity().getKiller() != null) {
            Player deathPlayer = e.getEntity(); // 死亡的玩家
            Player attackPlayer = e.getEntity().getKiller(); // 击杀的玩家
            Location location = deathPlayer.getLocation();
            deathPlayer.spigot().respawn();
            deathPlayer.teleport(location);

            ArenaState state = ArenaManager.getInstance().getArenaStateByPlayer(attackPlayer);
            state.setEndTime(System.currentTimeMillis());
            if (deathPlayer.getName().equals(state.getPlayerA())){
                state.setAFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(deathPlayer, FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))));
                state.setBFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(attackPlayer, FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))));
            }else {
                state.setAFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(attackPlayer, FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))));
                state.setBFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(deathPlayer, FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))));
            }
            state.setWin(attackPlayer.getName());
            attackPlayer.sendTitle(ColorParser.parse("&a&lVICTORY!"),ColorParser.parse("&7"+attackPlayer.getName()+" &fwon the match"));
            deathPlayer.sendTitle(ColorParser.parse("&c&lDEFEAT!"),ColorParser.parse("&7"+attackPlayer.getName()+" &fwon the match"));
            state.setFKitType(FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()));
            state.setState(3);

            PracticeGameDataStorage.getInstance().putArenaData(state); // 对战记录存入数据库
            ArenaState arenaState = PracticeGameDataStorage.getInstance().getArenaDataByEndTime(state.getEndTime()); // 取出最新的对战数据
            ArenaManager.getInstance().endGame(state.getArenaId()); // 结束这场战斗

            JSONMessage.create(ColorParser.parse("&r\n&6Post-Match Inventories &7(Click name to view)"))
                    .send(attackPlayer,deathPlayer);
            JSONMessage.create(ColorParser.parse("&aWinner: "))
                    .then(ColorParser.parse("&e"+attackPlayer.getName()))
                    .tooltip(ColorParser.parse("&7Click view"))
                    .runCommand("/miars practice openInv "+attackPlayer.getName()+" "+arenaState.getId())
                    .then(ColorParser.parse("&7 - &r"))
                    .then(ColorParser.parse("&cLoser: "))
                    .then(ColorParser.parse("&e"+deathPlayer.getName()))
                    .tooltip(ColorParser.parse("&7Click view"))
                    .runCommand("/miars practice openInv "+deathPlayer.getName()+" "+arenaState.getId())
                    .then(ColorParser.parse("\n&r"))
                    .send(attackPlayer,deathPlayer);

            // 5秒过后将玩家送出竞技场
            new BukkitRunnable() {
                @Override
                public void run() {
                    ServerManager.getInstance().sendPlayerToServer(deathPlayer.getName(),"practice");
                    ServerManager.getInstance().sendPlayerToServer(attackPlayer.getName(),"practice");
                }
            }.runTaskLaterAsynchronously(MiarsPractice.getInstance(),100);
        }


        e.setDeathMessage(null);
        e.setKeepInventory(true);
    }

    @EventHandler
    public void PlayerRespawnEvent(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        ToolUtils.playerInitialize(player);
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
                                player.sendMessage(ColorParser.parse("&e&l提示 | &7您暂时无法使用 &c末影珍珠 &7还需要等待 " + ToolUtils.getDate((15000-(System.currentTimeMillis()-hashMap.get(player.getUniqueId())))/1000) + " &7才能使用。"));
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
    public void PlayerDropItemEvent(PlayerDropItemEvent event) {
        // 禁止玩家丢弃物品
        event.setCancelled(true);
    }

    @EventHandler
    public void PlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        // 玩家使用药水后立马将空瓶子清除掉
        if (event.getItem().getType().equals(Material.POTION)) {
            Bukkit.getScheduler().runTaskLater(MiarsPractice.getInstance(), () -> event.getPlayer().getInventory().remove(Material.GLASS_BOTTLE), 2);
        }
    }

}
