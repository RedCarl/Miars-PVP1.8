package cn.mcarl.miars.practiceffa.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.core.utils.fastboard.FastBoard;
import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import cn.mcarl.miars.storage.entity.practice.QueueInfo;
import cn.mcarl.miars.storage.storage.data.practice.FPlayerDataStorage;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: carl0
 * @DATE: 2023/1/4 22:07
 */
public class ScoreBoardManager {
    private static final ScoreBoardManager instance = new ScoreBoardManager();
    public static ScoreBoardManager getInstance() {
        return instance;
    }

    private final Map<UUID, FastBoard> boards = new HashMap<>();

    public void init(){
        tick();
    }
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (FastBoard board : boards.values()) {
                    updateBoard(board);
                }
            }
        }.runTaskTimerAsynchronously(MiarsCore.getInstance(),0,20);
    }

    private void updateBoard(FastBoard board) {
        Player p = board.getPlayer();
        FPlayer fPlayer = FPlayerDataStorage.getInstance().getFPlayer(p);

        List<String> lines = new ArrayList<>();
        board.updateTitle("&cFFAGAME &8| &cMiars");
        lines.add("&7"+simpleDateFormat.format(System.currentTimeMillis()));
        lines.add("");
        lines.add("&7Level: &f"+0);
        lines.add("");

        // 战斗模式的计分板
        if (CombatManager.getInstance().isCombat(p)){

            Player opponent = Bukkit.getPlayer(CombatManager.getInstance().getCombatInfo(p).getOpponent());
            if (opponent==null){
                CombatManager.getInstance().clear(p);
            }else {
                lines.add("&7Opponent");
                lines.add("&7   Name: &c"+opponent.getName());
                lines.add("&7   K/D: &c"+ FFAUtil.getPlayerKD(FPlayerDataStorage.getInstance().getFPlayer(opponent)));
                lines.add("&7   Health: &c" + ToolUtils.decimalFormat(opponent.getHealth(), 2));
                lines.add("§e");
                lines.add("&7   Fighting... (&c" + CombatManager.getInstance().getLastSecond(p)+"&7)");
            }

        }else {
            lines.add("&7Kills: &c" + fPlayer.getKillsCount());
            lines.add("&7Death: &c" + fPlayer.getDeathCount());
        }

        // KD
        lines.add("");
        lines.add("&7K/D: &c" + FFAUtil.getPlayerKD(fPlayer));

        // 匹配
        QueueInfo queueInfo = PracticeQueueDataStorage.getInstance().getQueue(fPlayer);
        if (queueInfo!=null){

            lines.add("");
            lines.add("&c"+queueInfo.getQueueType().name() + " " + queueInfo.getFKitType().name());
            lines.add("&7正在匹配... ("+ PracticeQueueDataStorage.getInstance().getQueueTime(fPlayer)+"s)");
        }

        lines.add("");
        lines.add("&cplay.miars.cn");

        board.updateLines(ColorParser.parse(lines));
    }

    /**
     * 添加玩家的记分板
     * @param p
     */
    public void joinPlayer(Player p){
        FastBoard board = new FastBoard(p);
        boards.put(p.getUniqueId(), board);
    }

    /**
     * 移出玩家的记分板
     * @param p
     */
    public void removePlayer(Player p){
        FastBoard board = boards.remove(p.getUniqueId());

        if (board != null) {
            board.delete();
        }
    }
}
