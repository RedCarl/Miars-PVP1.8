package cn.mcarl.miars.skypvp.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.core.utils.fastboard.FastBoard;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.storage.data.serverInfo.ServerInfoDataStorage;
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
        lines.add("&f 等级 &7[1✩]");
        lines.add("&f 进度 &a0&7/&b1k");
        lines.add("&f &8[&7■■■■■■■■■■&8]");
        lines.add("");
        lines.add("&f 击杀 &60");
        lines.add("&f 死亡 &60");
        lines.add("");
        lines.add("&f 硬币 &6"+MiarsCore.getEcon().getBalance(p));
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
