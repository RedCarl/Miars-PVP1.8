package cn.mcarl.miars.megawalls.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.core.utils.fastboard.FastBoard;
import cn.mcarl.miars.megawalls.conf.PluginConfig;
import cn.mcarl.miars.megawalls.game.entitiy.GameInfo;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.entitiy.GameTeam;
import cn.mcarl.miars.megawalls.game.entitiy.enums.TeamType;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.game.manager.GamePlayerManager;
import cn.mcarl.miars.storage.storage.data.serverInfo.ServerInfoDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
        }.runTaskTimer(MiarsCore.getInstance(),0,20);
    }

    private void updateBoard(FastBoard board) {
        Player p = board.getPlayer();

        GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(p);
        List<String> lines = new ArrayList<>();
        board.updateTitle("&cMegaWalls &8| &c"+ ServerInfoDataStorage.getInstance().getServerInfo().getNameCn());
        lines.add("&7"+simpleDateFormat.format(System.currentTimeMillis()));
        lines.add("");
        switch (GameManager.getInstance().getGameInfo().getGameState()){
            case WAIT,READY -> {
                lines.add("&7地图: &c"+ PluginConfig.NAME.get());
                lines.add("&7人数: &c"+ "&a"+ Bukkit.getOnlinePlayers().size()+"&7/&c"+ (PluginConfig.TEAM_LIMIT.get()*4));
                lines.add("");
                lines.add("&7倒计时: &c"+ ToolUtils.getDateMode(GameManager.getInstance().getWaitTime()));
                Integer players = (PluginConfig.READY_PLAYERS.get() - Bukkit.getOnlinePlayers().size());
                if (players>0){
                    lines.add("&7还需人数: &c"+(PluginConfig.READY_PLAYERS.get() - Bukkit.getOnlinePlayers().size()));
                }
            }
            case START,CONDUCT -> {
                List<String> list = new ArrayList<>();
                for (TeamType teamType:GameManager.getInstance().getGameInfo().getGameTeams().keySet()) {
                    GameTeam gameTeam = GameManager.getInstance().getGameInfo().getGameTeams().get(teamType);

                    if (gamePlayer.getTeamType()==teamType){
                        String a = null;
                        if (list.size()!=0){
                            a = list.get(0);
                        }
                        list.add(0,teamType.getColor()+"["+teamType.getType()+"] "+
                                (gamePlayer.getTeamType()==teamType?teamType.getColor():"&f")+"凋零 "+teamType.getColor()+"❤&7: "+teamType.getColor()+
                                ToolUtils.decimalFormat(gameTeam.getTeamWither().getHealth(),0));
                        if (a!=null){
                            list.add(a);
                        }
                    }else {
                        list.add(teamType.getColor()+"["+teamType.getType()+"] "+
                                (gamePlayer.getTeamType()==teamType?teamType.getColor():"&f")+"凋零 "+teamType.getColor()+"❤&7: "+teamType.getColor()+
                                ToolUtils.decimalFormat(gameTeam.getTeamWither().getHealth(),0));
                    }

                }
                lines.addAll(list);
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
