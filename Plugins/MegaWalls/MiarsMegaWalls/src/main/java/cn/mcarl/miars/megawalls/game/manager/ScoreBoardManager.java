package cn.mcarl.miars.megawalls.game.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.MiarsUtils;
import cn.mcarl.miars.storage.utils.ToolUtils;
import cn.mcarl.miars.storage.utils.fastboard.FastBoard;
import cn.mcarl.miars.megawalls.conf.PluginConfig;
import cn.mcarl.miars.megawalls.game.entitiy.GameInfo;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.entitiy.GameTeam;
import cn.mcarl.miars.megawalls.game.entitiy.enums.GameState;
import cn.mcarl.miars.megawalls.game.entitiy.enums.TeamType;
import cn.mcarl.miars.storage.storage.data.serverInfo.ServerInfoDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
                if (GameManager.getInstance().getGameInfo()!=null){
                    GameManager.getInstance().tick();
                }
                for (FastBoard board : boards.values()) {
                    updateBoard(board);
                }
            }
        }.runTaskTimer(MiarsCore.getInstance(),0,20);
    }

    private void updateBoard(FastBoard board) {
        Player p = board.getPlayer();

        GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(p);
        GameInfo gameInfo = GameManager.getInstance().getGameInfo();
        GameTeam meGameTeam = GameManager.getInstance().getGamePlayerTeam(gamePlayer);
        List<String> lines = new ArrayList<>();
        if (gameInfo.getGameState()!=GameState.WAIT && gameInfo.getGameState()!=GameState.READY){
            board.updateTitle(gamePlayer.getTeamType().getColor()+"MEGA WALLS &8| "+gamePlayer.getTeamType().getColor()+ServerInfoDataStorage.getInstance().getServerInfo().getNameCn());
        }else {
            board.updateTitle("&eMEGA WALLS &8| &e"+ ServerInfoDataStorage.getInstance().getServerInfo().getNameCn());
        }

        lines.add("&7"+simpleDateFormat.format(System.currentTimeMillis())+" &8"+ MiarsUtils.getServerCode());

        if (gameInfo.getGameState() != GameState.WAIT && gameInfo.getGameState() != GameState.READY){
            if (gameInfo.getGameState()== GameState.START){
                if (GameManager.getInstance().getReadyTime()!=0){
                    String s = ToolUtils.getDateMode(GameManager.getInstance().getReadyTime());
                    if (GameManager.getInstance().getReadyTime()%2==0){
                        lines.add("&f开门倒计时: &a"+s);
                    }else {
                        lines.add("&f开门倒计时: &c"+s);
                    }
                    if (GameManager.getInstance().getReadyTime()<=5){
                        gamePlayer.sendMessage("&7大门将在 &c"+GameManager.getInstance().getReadyTime()+" &7秒后开启。");
                    }
                }
                if (GameManager.getInstance().getReadyTime()==0){
                    gamePlayer.givePotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,(int) (PluginConfig.WALL_TIME.get()*20),1));
                    meGameTeam.collapse();
                    gamePlayer.sendMessage("&7做好防御准备迎接敌人的袭击。");
                }
            }else if (gameInfo.getGameState() == GameState.CONDUCT && !gameInfo.getGameWall().isCollapse()){
                if (GameManager.getInstance().getWallTime()!=0){
                    if (GameManager.getInstance().getWallTime()<=30){
                        String s = ToolUtils.getDateMode(GameManager.getInstance().getWallTime());
                        if (GameManager.getInstance().getWallTime()%2==0){
                            lines.add("&f战墙倒塌: &a"+s);
                        }else {
                            lines.add("&f战墙倒塌: &c"+s);
                        }
                    }else {
                        lines.add("&f战墙倒塌: &a"+ ToolUtils.getDateMode(GameManager.getInstance().getWallTime()));
                    }

                }else {
                    gamePlayer.sendTitle("&c巨墙倒塌！","&7开始战斗吧！");
                    gamePlayer.sendMessage("&7巨墙已经倒塌，开始战斗！");
                }
            }else if (gameInfo.isWitherFury()){
                if (GameManager.getInstance().getWitherFury()!=0){
                    if (GameManager.getInstance().getWitherFury()<=30){
                        String s = ToolUtils.getDateMode(GameManager.getInstance().getWitherFury());
                        if (GameManager.getInstance().getWitherFury()%2==0){
                            lines.add("&f凋灵暴怒消失: &a"+s);
                        }else {
                            lines.add("&f凋灵暴怒消失: &c"+s);
                        }
                    }else {
                        lines.add("&f凋灵暴怒消失: &a"+ ToolUtils.getDateMode(GameManager.getInstance().getWitherFury()));
                    }

                }else {
                    gamePlayer.sendTitle("&a凋灵暴怒消失","&7将不再攻击敌人。");
                    gamePlayer.sendMessage("&7凋零暴怒消失。&c(将不再攻击敌人)");
                }
            }else {
                if (GameManager.getInstance().getGameOver()!=0){
                    if (GameManager.getInstance().getGameOver()<=30){
                        String s = ToolUtils.getDateMode(GameManager.getInstance().getGameOver());
                        if (GameManager.getInstance().getGameOver()%2==0){
                            lines.add("&f游戏结束: &a"+s);
                        }else {
                            lines.add("&f游戏结束: &c"+s);
                        }
                    }else {
                        lines.add("&f游戏结束: &a"+ ToolUtils.getDateMode(GameManager.getInstance().getGameOver()));
                    }
                }
            }
        }

        lines.add("");
        switch (gameInfo.getGameState()){
            case WAIT,READY -> {
                lines.add("&f地图: &6"+ PluginConfig.NAME.get());
                lines.add("&f人数: &6"+ Bukkit.getOnlinePlayers().size()+"&f/&6"+ (PluginConfig.TEAM_LIMIT.get()*4));
                lines.add("");
                lines.add("&f倒计时: &6"+ ToolUtils.getDateMode(GameManager.getInstance().getWaitTime()));
                Integer players = (PluginConfig.READY_PLAYERS.get() - Bukkit.getOnlinePlayers().size());
                if (players>0){
                    lines.add("&f还需人数: &6"+(PluginConfig.READY_PLAYERS.get() - Bukkit.getOnlinePlayers().size()));
                }
            }
            case START,CONDUCT -> {


                List<String> list = new ArrayList<>();
                for (TeamType teamType:GameManager.getInstance().getGameInfo().getGameTeams().keySet()) {
                    GameTeam gameTeam = GameManager.getInstance().getGameInfo().getGameTeams().get(teamType);

                    if (gamePlayer.getTeamType()==teamType){
                        if (gameTeam.isWitherDead()){
                            if (gameTeam.getSurvivePlayers().size()>=1){
                                list.add(0,teamType.getColor()+"["+teamType.getType()+"] "+
                                        (gamePlayer.getTeamType()==teamType?teamType.getColor():"&f")+"玩家&7: "+teamType.getColor()+
                                        gameTeam.getSurvivePlayers().size());
                            }else {
                                list.add(0, "&7"+teamType.getType()+"队已经被淘汰！");
                            }
                        }else {
                            list.add(0,teamType.getColor()+"["+teamType.getType()+"] "+
                                    (gamePlayer.getTeamType()==teamType?teamType.getColor():"&f")+"凋灵 "+teamType.getColor()+"❤&7: "+teamType.getColor()+
                                    ToolUtils.decimalFormat(gameTeam.getTeamWither().getHealth(),0));
                        }
                    } else {
                        if (gameTeam.isWitherDead()){
                            if (gameTeam.getSurvivePlayers().size()>=1){
                                list.add(teamType.getColor()+"["+teamType.getType()+"] "+
                                        (gamePlayer.getTeamType()==teamType?teamType.getColor():"&f")+"玩家&7: "+teamType.getColor()+
                                        gameTeam.getSurvivePlayers().size());
                            }else {
                                list.add("&7"+teamType.getType()+"队已经被淘汰！");
                            }
                        }else {
                            list.add(teamType.getColor()+"["+teamType.getType()+"] "+
                                    (gamePlayer.getTeamType()==teamType?teamType.getColor():"&f")+"凋灵 "+teamType.getColor()+"❤&7: "+teamType.getColor()+
                                    ToolUtils.decimalFormat(gameTeam.getTeamWither().getHealth(),0));
                        }
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
