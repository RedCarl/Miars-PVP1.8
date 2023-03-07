package cn.mcarl.miars.skypvp.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.core.utils.fastboard.FastBoard;
import cn.mcarl.miars.skypvp.entitiy.GamePlayer;
import cn.mcarl.miars.skypvp.utils.PlayerUtils;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.FPlayerDataStorage;
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
        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(p);
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());

        List<String> lines = new ArrayList<>();
        board.updateTitle("&eSKYPVP &8| &e"+ ServerInfoDataStorage.getInstance().getServerInfo().getNameCn());
        lines.add("&7"+simpleDateFormat.format(System.currentTimeMillis())+" &8"+ ToolUtils.getServerCode());
        lines.add("");
        lines.add("&f 等级 &6"+0);
        lines.add("");

        // 战斗模式的计分板
        if (CombatManager.getInstance().isCombat(p)){

            Player opponent = Bukkit.getPlayer(CombatManager.getInstance().getCombatInfo(p).getOpponent());
            if (opponent==null){
                CombatManager.getInstance().clear(p);
            }else {
                lines.add("&f 对 手");
                lines.add("&f   名称 &6" + opponent.getName());
                lines.add("&f   K/D &6" + GamePlayer.get(opponent).getKb());
                lines.add("&f   血量 &6" + ToolUtils.decimalFormat(opponent.getHealth(), 2));
                lines.add("&r");
                lines.add("&7   战斗中... (&6" + CombatManager.getInstance().getLastSecond(p)+"&7)");
            }

        }else {
            lines.add("&f 击杀 &6" + GamePlayer.get(p).getSPlayer().getKillsCount());
            lines.add("&f 死亡 &6" + GamePlayer.get(p).getSPlayer().getDeathCount());
        }

        // KD
        lines.add("");
        lines.add("&f K/D &6" + GamePlayer.get(p).getKb());
        lines.add("&f 硬币 &6" + GamePlayer.get(p).getSPlayer().getCoin());
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
