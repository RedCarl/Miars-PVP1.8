package cn.mcarl.miars.practiceffa.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.core.utils.fastboard.FastBoard;
import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import cn.mcarl.miars.storage.entity.practice.QueueInfo;
import cn.mcarl.miars.storage.entity.practice.RankScore;
import cn.mcarl.miars.storage.storage.data.practice.FPlayerDataStorage;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.RankScoreDataStorage;
import cn.mcarl.miars.storage.storage.data.serverInfo.ServerInfoDataStorage;
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
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");

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
        RankScore score = RankScoreDataStorage.getInstance().getRankScore(p.getUniqueId(),1);

        List<String> lines = new ArrayList<>();
        board.updateTitle("&ePractice &8| &c"+ ServerInfoDataStorage.getInstance().getServerInfo().getNameCn());
        lines.add("&7"+simpleDateFormat.format(System.currentTimeMillis())+" &8"+ ToolUtils.getServerCode());
        lines.add("");
        lines.add("&6&l┃ &7Online: &6"+ ServerManager.getInstance().getServerOnline("practice"));
        lines.add("");
        if (score!=null){
            lines.add("&6&l┃ &7Rank: &e"+score.getScore());
            lines.add("");
        }

        // 战斗模式的计分板
        if (CombatManager.getInstance().isCombat(p)){

            Player opponent = Bukkit.getPlayer(CombatManager.getInstance().getCombatInfo(p).getOpponent());
            if (opponent==null){
                CombatManager.getInstance().clear(p);
            }else {
                lines.add("&7Opponent");
                lines.add("&7   Health: &c" + ToolUtils.decimalFormat(opponent.getHealth(), 2));
                lines.add("&e");
                lines.add("&7   Fighting... (&c" + CombatManager.getInstance().getLastSecond(p)+"&7)");
            }

        }else {
            lines.add("&6&l┃ &7Kills: &a" + fPlayer.getKillsCount());
            lines.add("&6&l┃ &7Death: &c" + fPlayer.getDeathCount());
            lines.add("&6&l┃ &7K/D: &6" + FFAUtil.getPlayerKD(fPlayer));
        }



        // 匹配
        QueueInfo queueInfo = PracticeQueueDataStorage.getInstance().getQueue(fPlayer);
        if (queueInfo!=null){

            lines.add("");
            lines.add(queueInfo.getQueueType().getColor()+queueInfo.getQueueType().getName() + " " + queueInfo.getFKitType().getName());
            lines.add("&7正在匹配... ("+ PracticeQueueDataStorage.getInstance().getQueueTime(fPlayer)+"s)");
        }

        lines.add("");
        lines.add("&7&o"+ ServerInfoDataStorage.getInstance().getServerInfo().getIp());

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
