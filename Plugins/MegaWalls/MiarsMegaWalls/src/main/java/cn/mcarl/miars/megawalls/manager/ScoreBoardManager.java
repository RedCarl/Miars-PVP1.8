package cn.mcarl.miars.megawalls.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.fastboard.FastBoard;
import cn.mcarl.miars.megawalls.conf.PluginConfig;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
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
        board.updateTitle("&cMegaWalls &8| &c"+ ServerInfoDataStorage.getInstance().getServerInfo().getNameCn());
        lines.add("&7"+simpleDateFormat.format(System.currentTimeMillis()));
        lines.add("");
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT -> {
                lines.add("&7地图: &c"+ PluginConfig.NAME.get());
                lines.add("&7人数: &c"+ "&a"+ Bukkit.getOnlinePlayers().size()+"&7/&c"+ (PluginConfig.TEAM_LIMIT.get()*4));
                lines.add("");
                lines.add("&7倒数: &c05:00");
                lines.add("&7还需人数: &c"+(PluginConfig.READY_PLAYERS.get() - Bukkit.getOnlinePlayers().size()));
            }
            case READY -> {

            }
            case CONDUCT -> {

            }
            case END -> {

            }
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
