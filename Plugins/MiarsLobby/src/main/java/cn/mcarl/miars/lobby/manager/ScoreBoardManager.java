package cn.mcarl.miars.lobby.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.fastboard.FastBoard;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import cn.mcarl.miars.storage.storage.data.serverInfo.ServerInfoDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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

        List<String> lines = new ArrayList<>();
        board.updateTitle("&cLobby &8| &c"+ ServerInfoDataStorage.getInstance().getServerInfo().getNameCn());
        lines.add("&7"+simpleDateFormat.format(System.currentTimeMillis()));
        lines.add("");
        lines.add("&7玩家: &c"+p.getName());
        lines.add("");
        lines.add("&7硬币: &c"+MiarsCore.getEcon().getBalance(p));
        lines.add("&7金子: &c"+MiarsCore.getPpAPi().look(p.getUniqueId()));
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
