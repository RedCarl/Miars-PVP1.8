package cn.mcarl.miars.practice.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.practice.MiarsPractice;
import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.practice.entity.GamePlayer;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.PlayerState;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.*;
import cn.mcarl.miars.storage.utils.BukkitUtils;
import cn.mcarl.miars.storage.utils.ToolUtils;
import cn.mcarl.miars.storage.utils.jsonmessage.JSONMessage;
import cn.mcarl.miars.practice.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    private static final ArenaManager instance = new ArenaManager();

    public static ArenaManager getInstance() {
        return instance;
    }

    List<Arena> arenaData = new ArrayList<>();

    public void init(){

        for (int i = 0; i < PluginConfig.PRACTICE_SITE.WORLD.getNotNull(); i++) {
            WorldUtils.copyWorld(Bukkit.getWorld("world"),("world_"+(i+1)));
        }

        List<Arena> list = new ArrayList<>(PracticeArenaDataStorage.getInstance().getArenaData(MiarsPractice.getModeType()));
        List<ArenaState> states = new ArrayList<>();

        // 根据世界数量分配更多的房间
        for (World world:Bukkit.getWorlds()) {
            for (Arena arena:new ArrayList<>(list)) {

                Arena data = arena.clone();

                data.setWorld(world.getName());

                data.getCenter().setWorld(world);

                data.getLoc2().setWorld(world);

                data.getLoc1().setWorld(world);

                arenaData.add(data);

                states.add(new ArenaState(
                        data.getId(),
                        data.getWorld(),
                        ArenaState.State.IDLE
                ));

            }
        }

        PracticeArenaStateDataStorage.getInstance().init(PluginConfig.PRACTICE_SITE.MODE.get(),states);

    }

    /**
     * 房间分配,如果返回 NULL 就是没有房间
     */
    public Arena allotArena(String a, String b, ArenaState state, QueueType queueType){
        PracticeArenaStateDataStorage.getInstance().allotArena(a,b,state,PluginConfig.PRACTICE_SITE.MODE.get(),queueType);

        ServerManager.getInstance().sendPlayerToServer(a,cn.mcarl.miars.core.conf.PluginConfig.SERVER_INFO.NAME.get());
        ServerManager.getInstance().sendPlayerToServer(b,cn.mcarl.miars.core.conf.PluginConfig.SERVER_INFO.NAME.get());


        new BukkitRunnable() {
            int i; // 记录时间
            @Override
            public void run() {
                // 比赛开始提示
                Player player_a = null;
                Player player_b = null;

                try {
                    player_a = Bukkit.getPlayer(state.getPlayerA());
                    player_b = Bukkit.getPlayer(state.getPlayerB());
                    if (player_a != null && player_b != null) {
                        readyGame(state);
                        cancel();
                    }else {
                        if (player_a != null) {
                            player_a.sendTitle(new Title(ColorParser.parse("&bWaiting for the opponent...")));
                        }
                        if (player_b != null) {
                            player_b.sendTitle(new Title(ColorParser.parse("&bWaiting for the opponent...")));
                        }

                        if (i>=5){
                            // 传送对战玩家至大厅
                            if (player_a != null) {
                                ServerManager.getInstance().sendPlayerToServer(player_a.getName(), "practice-ffa");
                                player_a.sendTitle(new Title(ColorParser.parse("&3Opponent not connected.")));
                            }
                            if (player_b != null) {
                                ServerManager.getInstance().sendPlayerToServer(player_b.getName(), "practice-ffa");
                                player_b.sendTitle(new Title(ColorParser.parse("&3Opponent not connected.")));
                            }
                            state.setState(ArenaState.State.IDLE);
                            // 更新游戏房间状态为空闲，可以接纳下一批玩家
                            PracticeArenaStateDataStorage.getInstance().getArenaStateById(state).init();
                            PracticeArenaStateDataStorage.getInstance().updateRedis(PluginConfig.PRACTICE_SITE.MODE.get());
                            cancel();
                        }
                    }
                } catch (NullPointerException | IllegalArgumentException e) {
                    if (player_a != null) {
                        player_a.sendTitle(new Title("&bWaiting for the opponent..."));
                    }
                    if (player_b != null) {
                        player_b.sendTitle(new Title("&bWaiting for the opponent..."));
                    }

                    if (i>=5){
                        // 传送对战玩家至大厅
                        if (player_a != null) {
                            ServerManager.getInstance().sendPlayerToServer(player_a.getName(), "practice-ffa");
                            player_a.sendTitle(new Title(ColorParser.parse("&3Opponent not connected.")));
                        }
                        if (player_b != null) {
                            ServerManager.getInstance().sendPlayerToServer(player_b.getName(), "practice-ffa");
                            player_b.sendTitle(new Title(ColorParser.parse("&3Opponent not connected.")));
                        }
                        state.setState(ArenaState.State.IDLE);
                        // 更新游戏房间状态为空闲，可以接纳下一批玩家
                        PracticeArenaStateDataStorage.getInstance().getArenaStateById(state).init();
                        PracticeArenaStateDataStorage.getInstance().updateRedis(PluginConfig.PRACTICE_SITE.MODE.get());
                        cancel();
                    }
                }

                i++;
            }
        }.runTaskTimer(MiarsPractice.getInstance(),0,20);

        return getArenaById(state);
    }

    public Arena getArenaById(ArenaState state){

        for (Arena a:arenaData) {
            if (a.getId().equals(state.getArenaId()) && a.getWorld().equals(state.getWorld())){
                return a;
            }
        }

        return null;
    }

    /**
     * 游戏准备
     */
    public void readyGame(ArenaState state){
        PracticeArenaStateDataStorage.getInstance().getArenaStateById(state).setState(ArenaState.State.READY);
        PracticeArenaStateDataStorage.getInstance().updateRedis(PluginConfig.PRACTICE_SITE.MODE.get());

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                try {
                    Player a = Bukkit.getPlayer(state.getPlayerA());
                    Player b = Bukkit.getPlayer(state.getPlayerB());

                    if (a!=null && b!=null){
                        JSONMessage.create(ColorParser.parse("&fMatch starting in &b"+(5-i)+"..."))
                                .send(a,b);
                        a.sendTitle(ColorParser.parse("&fMatch starting..."),ColorParser.parse("The match will be starting in &b"+(5-i)+"&f..."));
                        b.sendTitle(ColorParser.parse("&fMatch starting..."),ColorParser.parse("The match will be starting in &b"+(5-i)+"&f..."));

                        a.playSound(a.getLocation(), Sound.NOTE_PLING,1,1);
                        b.playSound(b.getLocation(),Sound.NOTE_PLING,1,1);
                        if (i == 4){
                            ArenaManager.getInstance().startGame(state);
                            JSONMessage.create(ColorParser.parse("&fThe match has started."))
                                    .send(a,b);


                            a.sendTitle(ColorParser.parse("&fMatch started!"),null);
                            b.sendTitle(ColorParser.parse("&fMatch started!"),null);
                            a.playSound(a.getLocation(),Sound.ORB_PICKUP,1,1);
                            b.playSound(b.getLocation(),Sound.ORB_PICKUP,1,1);
                            JSONMessage.create(ColorParser.parse("&r"))
                                    .send(a,b);
                            JSONMessage.create(ColorParser.parse("&4&l(WARNING) &cButterfly clicking / Block glitch is allowed &cand they may resulting into a ban."))
                                    .send(a,b);

                            cancel();
                        }
                    }else {
                        if (a!=null){
                            ServerManager.getInstance().sendPlayerToServer(a.getName(),"practice-ffa");
                        }

                        if (b!=null){
                            ServerManager.getInstance().sendPlayerToServer(b.getName(),"practice-ffa");
                        }

                        // 更新游戏房间状态为空闲，可以接纳下一批玩家
                        PracticeArenaStateDataStorage.getInstance().getArenaStateById(state).init();
                        PracticeArenaStateDataStorage.getInstance().updateRedis(PluginConfig.PRACTICE_SITE.MODE.get());

                        cancel();
                    }
                }catch (IllegalArgumentException ignored){
                    cancel();
                }

                i++;
            }
        }.runTaskTimer(MiarsPractice.getInstance(),20,20);
    }

    /**
     * 游戏开始
     */
    public void startGame(ArenaState state){
        state.setStartTime(System.currentTimeMillis());
        PracticeArenaStateDataStorage.getInstance().getArenaStateById(state).setState(ArenaState.State.GAME);
        PracticeArenaStateDataStorage.getInstance().updateRedis(PluginConfig.PRACTICE_SITE.MODE.get());
    }

    /**
     * 游戏结束
     */
    public void endGame(GamePlayer win, GamePlayer fail){

        // 设置本场游戏情况
        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(win.getName());;

        // Build UHC 清理战场上的方块
        BuildUHCManager.getInstance().clearBlock(win.getPlayer().getWorld(),ArenaManager.getInstance().getArenaById(state));

        state.setWin(win.getName());
        state.setEndTime(System.currentTimeMillis());
        state.setFKitType(MiarsPractice.getModeType());
        state.setState(ArenaState.State.END);

        // 设置玩家库存以及信息参数
        if (fail.getName().equals(state.getPlayerA())){
            state.setAFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(fail.getPlayer(), MiarsPractice.getModeType())));
            state.setPlayerStateA(
                    new PlayerState(
                            Double.parseDouble(ToolUtils.decimalFormat(fail.getHealth(), 2)),
                            fail.getFoodLevel(),
                            cn.mcarl.miars.storage.utils.ToolUtils.potionEffectToString(
                                    new ArrayList<>(fail.getActivePotionEffects())
                            )
                    )
            );
            state.setBFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(win.getPlayer(), MiarsPractice.getModeType())));
            state.setPlayerStateB(
                    new PlayerState(
                            Double.parseDouble(ToolUtils.decimalFormat(win.getHealth(), 2)),
                            win.getFoodLevel(),
                            cn.mcarl.miars.storage.utils.ToolUtils.potionEffectToString(
                                    new ArrayList<>(win.getActivePotionEffects())
                            )
                    )
            );
        }else {
            state.setAFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(win.getPlayer(), MiarsPractice.getModeType())));
            state.setPlayerStateA(
                    new PlayerState(
                            Double.parseDouble(ToolUtils.decimalFormat(win.getHealth(), 2)),
                            win.getFoodLevel(),
                            cn.mcarl.miars.storage.utils.ToolUtils.potionEffectToString(
                                    new ArrayList<>(win.getActivePotionEffects())
                            )
                    )
            );
            state.setBFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(fail.getPlayer(), MiarsPractice.getModeType())));
            state.setPlayerStateB(
                    new PlayerState(
                            Double.parseDouble(ToolUtils.decimalFormat(fail.getHealth(), 2)),
                            fail.getFoodLevel(),
                            cn.mcarl.miars.storage.utils.ToolUtils.potionEffectToString(
                                    new ArrayList<>(fail.getActivePotionEffects())
                            )
                    )
            );
        }

        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(win.getUuid());
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());

        // 为玩家发送胜利/失败提示
        win.sendTitle(ColorParser.parse("&a&lVICTORY!"),ColorParser.parse(mRank.getNameColor()+win.getName()+" &fwon the match"));
        PracticeDailyStreakDataStorage.getInstance().putDailyStreakData(win.getPlayer(),state.getQueueType(), state.getFKitType(),true);
        fail.sendTitle(ColorParser.parse("&c&lDEFEAT!"),ColorParser.parse(mRank.getNameColor()+win.getName()+" &fwon the match"));
        PracticeDailyStreakDataStorage.getInstance().putDailyStreakData(fail.getPlayer(),state.getQueueType(), state.getFKitType(),false);


        // 数据录入
        PracticeGameDataStorage.getInstance().putArenaData(state); // 对战记录存入数据库

        // 发送对局背包信息消息
        ArenaState arenaState = PracticeGameDataStorage.getInstance().getArenaDataByEndTime(state.getEndTime()); // 取出最新的对战数据
        JSONMessage.create(ColorParser.parse("&r"))
                .send(win.getPlayer(),fail.getPlayer());
        JSONMessage.create(ColorParser.parse("&eMatch Results &7(Click player to view)"))
                .send(win.getPlayer(),fail.getPlayer());
        JSONMessage.create(ColorParser.parse("&aWinner: "))
                .then(ColorParser.parse("&e"+win.getName()))
                .tooltip(ColorParser.parse("&7Click to view"))
                .runCommand("/miars practice openInv "+win.getName()+" "+arenaState.getId())
                .then(ColorParser.parse("&7 ┃ &r"))
                .then(ColorParser.parse("&cLoser: "))
                .then(ColorParser.parse("&e"+fail.getName()))
                .tooltip(ColorParser.parse("&7Click to view"))
                .runCommand("/miars practice openInv "+fail.getName()+" "+arenaState.getId())
                .then(ColorParser.parse("&r"))
                .send(win.getPlayer(),fail.getPlayer());
        JSONMessage.create(ColorParser.parse("&r"))
                .send(win.getPlayer(),fail.getPlayer());

        if (state.getQueueType()==QueueType.RANKED){
            RankScoreDataStorage.getInstance().scoreOperation(win.getPlayer(),fail.getPlayer());
        }

        // 5秒过后将玩家送出竞技场
        new BukkitRunnable() {
            @Override
            public void run() {
                // 传送对战玩家至大厅
                ServerManager.getInstance().sendPlayerToServer(fail.getName(),"practice-ffa");
                ServerManager.getInstance().sendPlayerToServer(win.getName(),"practice-ffa");

                // 更新游戏房间状态为空闲，可以接纳下一批玩家
                PracticeArenaStateDataStorage.getInstance().getArenaStateById(state).init();
                PracticeArenaStateDataStorage.getInstance().updateRedis(PluginConfig.PRACTICE_SITE.MODE.get());
            }
        }.runTaskLater(MiarsPractice.getInstance(),100);


        win.clearInv();
        fail.clearInv();

        // 更新房间信息为 游戏结束。
        PracticeArenaStateDataStorage.getInstance().getArenaStateById(state).setState(ArenaState.State.END);
    }

    public void endGame(ArenaState state){
        // 5秒过后将玩家送出竞技场
        new BukkitRunnable() {
            @Override
            public void run() {
                // 比赛开始提示
                Player player_a = null;
                Player player_b = null;
                try {
                    player_a = Bukkit.getPlayer(state.getPlayerA());
                    player_b = Bukkit.getPlayer(state.getPlayerB());

                } catch (NullPointerException | IllegalArgumentException ignored) {}

                // 传送对战玩家至大厅
                if (player_a != null) {
                    ServerManager.getInstance().sendPlayerToServer(player_a.getName(), "practice-ffa");
                }
                if (player_b != null) {
                    ServerManager.getInstance().sendPlayerToServer(player_b.getName(), "practice-ffa");
                }

                state.setState(ArenaState.State.IDLE);
                // 更新游戏房间状态为空闲，可以接纳下一批玩家
                PracticeArenaStateDataStorage.getInstance().getArenaStateById(state).init();
                PracticeArenaStateDataStorage.getInstance().updateRedis(PluginConfig.PRACTICE_SITE.MODE.get());
                cancel();
            }
        }.runTaskLaterAsynchronously(MiarsPractice.getInstance(),100);
    }

    public void clear(){
        PracticeArenaStateDataStorage.getInstance().setArenaStateRedisList(new ArrayList<>(),PluginConfig.PRACTICE_SITE.MODE.get());
    }
}
