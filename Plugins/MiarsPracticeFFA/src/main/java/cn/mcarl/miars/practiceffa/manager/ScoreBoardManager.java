package cn.mcarl.miars.practiceffa.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import cn.mcarl.miars.storage.entity.practice.QueueInfo;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import cn.mcarl.miars.storage.storage.data.practice.FPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import cn.mcarl.miars.storage.storage.data.serverInfo.ServerInfoDataStorage;
import cn.mcarl.miars.storage.utils.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
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
    DecimalFormat decimalFormat = new DecimalFormat("00");
    public void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (FastBoard board : new ArrayList<>(boards.values())) {
                    if (boards.containsValue(board)){
                        updateBoard(board);
                    }
                }
            }
        }.runTaskTimerAsynchronously(MiarsCore.getInstance(),0,2);
    }

    private void updateBoard(FastBoard board) {

        Player p = board.getPlayer();
        FPlayer fPlayer = FPlayerDataStorage.getInstance().getFPlayer(p);
        // RankScore score = RankScoreDataStorage.getInstance().getRankScore(p.getUniqueId(),1);

        List<String> lines = new ArrayList<>();
        board.updateTitle("&b&lPractice");
        lines.add("&f&7&m---------------------");
        lines.add("&fOnline: &b"+ ServerManager.getInstance().getServerOnline("practice"));
        int playing = PracticeArenaStateDataStorage.getInstance().getGamePlayersByQueueType(QueueType.UNRANKED)+PracticeArenaStateDataStorage.getInstance().getGamePlayersByQueueType(QueueType.RANKED);
        lines.add("&fPlaying: &b"+ playing);
        lines.add("&fPoints: &b"+ MiarsCore.getPpAPI().look(p.getUniqueId()));

        // FFA竞技场后开启
        if (!FFAUtil.isItemRange(
                p.getLocation(),
                PluginConfig.FFA_SITE.LOCATION.get(),
                PluginConfig.FFA_SITE.RADIUS.get()
        )){
            lines.add("");
            // 战斗模式的计分板
            if (CombatManager.getInstance().isCombat(p)){

                Player opponent = Bukkit.getPlayer(CombatManager.getInstance().getCombatInfo(p).getOpponent());
                if (opponent==null) {
                    CombatManager.getInstance().clear(p);
                }else {
//                    MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(opponent);
//                    MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
//                    lines.add("&fOpponent");
//                    lines.add(mRank.getNameColor() + opponent.getName());
//                    lines.add("");

                    int milliseconds = CombatManager.getInstance().getLastMilliSeconds(p);
                    double seconds = milliseconds / 1000.0;

                    DecimalFormat decimalFormat = new DecimalFormat("0.0");
                    String time = decimalFormat.format(seconds) + "s";
                    lines.add("&fStats: &c"+time);
                }

            }else {
                lines.add("&fKills: &b" + fPlayer.getKillsCount());
                lines.add("&fDeath: &b" + fPlayer.getDeathCount());
                lines.add("&fK/D: &b" + FFAUtil.getPlayerKD(fPlayer));
            }
        }

        // 匹配
        QueueInfo queueInfo = PracticeQueueDataStorage.getInstance().getQueue(fPlayer);
        if (queueInfo!=null){
            lines.add("");
            lines.add(queueInfo.getQueueType().getColor()+queueInfo.getQueueType().getName() + " " + queueInfo.getFKitType().getName());

            int seconds = (int) PracticeQueueDataStorage.getInstance().getQueueTime(fPlayer);
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;
            String time = decimalFormat.format(minutes) + ":" + decimalFormat.format(remainingSeconds);
            lines.add("&fTime: &b"+time);
            if (queueInfo.getQueueType()==QueueType.RANKED){
                lines.add("&fElo Range: &b"+999+" &7- &b"+1000);
            }
        }

        lines.add("");
        lines.add("&b"+ ServerInfoDataStorage.getInstance().getServerInfo().getIp());
        lines.add("&f&7&m---------------------");

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
