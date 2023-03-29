package cn.mcarl.miars.practice.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.core.utils.jsonmessage.JSONMessage;
import cn.mcarl.miars.practice.MiarsPractice;
import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.PlayerState;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import cn.mcarl.miars.storage.enums.practice.QueueType;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeDailyStreakDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeGameDataStorage;
import cn.mcarl.miars.storage.utils.BukkitUtils;
import cn.mcarl.miars.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ArenaManager {

    private static final ArenaManager instance = new ArenaManager();

    public static ArenaManager getInstance() {
        return instance;
    }

    List<Arena> arenaData = new ArrayList<>();

    public void init(){

        for (int i = 0; i < PluginConfig.PRACTICE_SITE.WORLD.get(); i++) {
            WorldUtils.copyWorld(Bukkit.getWorld("world"),("world_"+(i+1)));
        }

        List<Arena> list = new ArrayList<>(PracticeArenaDataStorage.getInstance().getArenaData(FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get())));
        List<ArenaState> states = new ArrayList<>();

        // 根据世界数量分配更多的房间
        for (World world:Bukkit.getWorlds()) {
            for (Arena arena:list) {
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
     * 游戏结束
     */
    public void endGame(Player win,Player fail){

        // 设置本场游戏情况
        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(fail);

        // Build UHC 清理战场上的方块
        BuildUHCManager.getInstance().clearBlock(ArenaManager.getInstance().getArenaById(state));
        

        state.setWin(win.getName());
        state.setEndTime(System.currentTimeMillis());
        state.setFKitType(FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()));
        state.setState(ArenaState.State.END);
        // 设置玩家库存以及信息参数
        if (fail.getName().equals(state.getPlayerA())){
            state.setAFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(fail, FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))));
            state.setPlayerStateA(
                    new PlayerState(
                            fail.getHealth(),
                            fail.getFoodLevel(),
                            cn.mcarl.miars.storage.utils.ToolUtils.potionEffectToString(
                                    new ArrayList<>(win.getActivePotionEffects())
                            )
                    )
            );
            state.setBFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(win, FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))));
            state.setPlayerStateB(
                    new PlayerState(
                            win.getHealth(),
                            win.getFoodLevel(),
                            cn.mcarl.miars.storage.utils.ToolUtils.potionEffectToString(
                                    new ArrayList<>(win.getActivePotionEffects())
                            )
                    )
            );
        }else {
            state.setAFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(win, FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))));
            state.setPlayerStateA(
                    new PlayerState(
                            win.getHealth(),
                            win.getFoodLevel(),
                            cn.mcarl.miars.storage.utils.ToolUtils.potionEffectToString(
                                    new ArrayList<>(win.getActivePotionEffects())
                            )
                    )
            );
            state.setBFInventory(BukkitUtils.ItemStackConvertByte(ToolUtils.playerToFInv(fail, FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get()))));
            state.setPlayerStateB(
                    new PlayerState(
                            fail.getHealth(),
                            fail.getFoodLevel(),
                            cn.mcarl.miars.storage.utils.ToolUtils.potionEffectToString(
                                    new ArrayList<>(win.getActivePotionEffects())
                            )
                    )
            );
        }

        // 为玩家发送胜利/失败提示
        win.sendTitle(ColorParser.parse("&a&lVICTORY!"),ColorParser.parse("&7"+win.getName()+" &fwon the match"));
        PracticeDailyStreakDataStorage.getInstance().putDailyStreakData(win,state.getQueueType(), state.getFKitType(),true);
        if (fail.isOnline()){
            fail.sendTitle(ColorParser.parse("&c&lDEFEAT!"),ColorParser.parse("&7"+win.getName()+" &fwon the match"));
        }
        PracticeDailyStreakDataStorage.getInstance().putDailyStreakData(fail,state.getQueueType(), state.getFKitType(),false);

        // 数据录入
        PracticeGameDataStorage.getInstance().putArenaData(state); // 对战记录存入数据库

       // 发送对局背包信息消息
        ArenaState arenaState = PracticeGameDataStorage.getInstance().getArenaDataByEndTime(state.getEndTime()); // 取出最新的对战数据
        JSONMessage.create(ColorParser.parse("&r\n&eMatch Results &7(Click player to view)"))
                .send(win,fail);
        JSONMessage.create(ColorParser.parse("&aWinner: "))
                .then(ColorParser.parse("&e"+win.getName()))
                .tooltip(ColorParser.parse("&7Click to view"))
                .runCommand("/miars practice openInv "+win.getName()+" "+arenaState.getId())
                .then(ColorParser.parse("&7 ┃ &r"))
                .then(ColorParser.parse("&cLoser: "))
                .then(ColorParser.parse("&e"+fail.getName()))
                .tooltip(ColorParser.parse("&7Click to view"))
                .runCommand("/miars practice openInv "+fail.getName()+" "+arenaState.getId())
                .then(ColorParser.parse("\n&r"))
                .send(win,fail);

        // 5秒过后将玩家送出竞技场
        new BukkitRunnable() {
            @Override
            public void run() {
                // 传送对战玩家至大厅
                if (fail.isOnline()){
                    ServerManager.getInstance().sendPlayerToServer(fail.getName(),"practice-ffa");
                }
                if (win.isOnline()){
                    ServerManager.getInstance().sendPlayerToServer(win.getName(),"practice-ffa");
                }

                // 更新游戏房间状态为空闲，可以接纳下一批玩家
                PracticeArenaStateDataStorage.getInstance().getArenaStateById(state).init();
                PracticeArenaStateDataStorage.getInstance().updateRedis(PluginConfig.PRACTICE_SITE.MODE.get());
            }
        }.runTaskLaterAsynchronously(MiarsPractice.getInstance(),100);

        // 清除失败者的背包
        fail.getInventory().clear();
        fail.getInventory().setItem(39,null);
        fail.getInventory().setItem(38,null);
        fail.getInventory().setItem(37,null);
        fail.getInventory().setItem(36,null);

        // 更新房间信息为 游戏结束。
        PracticeArenaStateDataStorage.getInstance().getArenaStateById(state).setState(ArenaState.State.END);
    }

    /**
     * 游戏准备
     */
    public void readyGame(ArenaState state){
        PracticeArenaStateDataStorage.getInstance().getArenaStateById(state).setState(ArenaState.State.READY);
    }


    /**
     * 游戏开始
     */
    public void startGame(ArenaState state){
        PracticeArenaStateDataStorage.getInstance().getArenaStateById(state).setState(ArenaState.State.GAME);
    }

    public void clear(){
        PracticeArenaStateDataStorage.getInstance().setArenaStateRedisList(new ArrayList<>(),PluginConfig.PRACTICE_SITE.MODE.get());
    }
}
