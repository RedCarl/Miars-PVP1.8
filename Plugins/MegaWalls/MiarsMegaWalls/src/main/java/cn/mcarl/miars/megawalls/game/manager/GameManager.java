package cn.mcarl.miars.megawalls.game.manager;

import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.conf.PluginConfig;
import cn.mcarl.miars.megawalls.game.entitiy.GameInfo;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.entitiy.GameTeam;
import cn.mcarl.miars.megawalls.game.entitiy.GameWall;
import cn.mcarl.miars.megawalls.game.entitiy.enums.GameState;
import cn.mcarl.miars.megawalls.game.entitiy.enums.TeamType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {
    private static final GameManager instance = new GameManager();
    public static GameManager getInstance() {
        return instance;
    }

    private final GameInfo gameInfo = new GameInfo();
    private Long waitTime = PluginConfig.WAIT.get();



    /**
     * 初始化房间
     */
    public void initGameInfo(){

        // 房间名称，会根据该名称查询数据库中房间的详细数据
        String name = PluginConfig.NAME.get();

        // 初始化房间状态
        gameInfo.setGameState(GameState.WAIT);

        // 初始化墙
        gameInfo.setGameWall(new GameWall(
                new Location(Bukkit.getWorld("world"),-43,29,215),
                new Location(Bukkit.getWorld("world"),-24,39,235),
                false

        ));

        // 初始化队伍
        gameInfo.getGameTeams().put(TeamType.RED,
                new GameTeam(
                        TeamType.RED,
                        new ArrayList<>(),
                        new Location(Bukkit.getWorld("world"),-33,30,204),
                        new Location(Bukkit.getWorld("world"),-33,30,204),
                        new Location(Bukkit.getWorld("world"),-33,30,204),
                        null,
                        false
                )
        );
        gameInfo.getGameTeams().put(TeamType.YELLOW,
                new GameTeam(
                        TeamType.YELLOW,
                        new ArrayList<>(),
                        new Location(Bukkit.getWorld("world"),-36,30,242),
                        new Location(Bukkit.getWorld("world"),-36,30,242),
                        new Location(Bukkit.getWorld("world"),-36,30,242),
                        null,
                        false
                )
        );
        gameInfo.getGameTeams().put(TeamType.BLUE,
                new GameTeam(
                        TeamType.BLUE,
                        new ArrayList<>(),
                        new Location(Bukkit.getWorld("world"),-16,30,225),
                        new Location(Bukkit.getWorld("world"),-16,30,225),
                        new Location(Bukkit.getWorld("world"),-16,30,225),
                        null,
                        false
                )
        );
        gameInfo.getGameTeams().put(TeamType.GREEN,
                new GameTeam(
                        TeamType.GREEN,
                        new ArrayList<>(),
                        new Location(Bukkit.getWorld("world"),-52,30,224),
                        new Location(Bukkit.getWorld("world"),-52,30,224),
                        new Location(Bukkit.getWorld("world"),-52,30,224),
                        null,
                        false
                )
        );

        gameInfo.setLobbySpawn(
                new Location(Bukkit.getWorld("world"),-40,60,230)
        );

        tick();
    }

    public void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {

                if (waitTime==0 && gameInfo.getGameState() == GameState.READY){
                    gameInfo.setGameState(GameState.START);

                    List<Player> ps = new ArrayList<>(Bukkit.getOnlinePlayers());
                    Iterator<Player> players = ps.listIterator();

                    // 队伍分配
                    while (players.hasNext()){
                        for (TeamType teamType:gameInfo.getGameTeams().keySet()) {
                            while (players.hasNext()){
                                GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(players.next());
                                gamePlayer.setTeamType(teamType);
                                gameInfo.getGameTeams().get(teamType).getGamePlayers().add(gamePlayer);
                            }
                        }
                    }

                    // 传送至玩家到出生点
                    for (TeamType teamType:gameInfo.getGameTeams().keySet()) {
                        GameTeam gameTeam = gameInfo.getGameTeams().get(teamType);
                        for (GamePlayer gamePlayer:gameTeam.getGamePlayers()) {
                            gamePlayer.tp(gameTeam.getRespawn());
                            gamePlayer.clearInv();
                        }
                        gameTeam.spawnWither(gameTeam);
                    }

                    gameInfo.setGameState(GameState.CONDUCT);
                }

                // 检测房间人数是否达标
                switch (gameInfo.getGameState()){
                    case WAIT,READY -> {
                        if (Bukkit.getOnlinePlayers().size() >= PluginConfig.READY_PLAYERS.get()){
                            // 人数已经满足基本要求，房间进入准备状态
                            gameInfo.setGameState(GameState.READY);
                        }else {
                            // 人数不达标。房间进入等待状态。
                            gameInfo.setGameState(GameState.WAIT);
                            waitTime = PluginConfig.WAIT.get();
                        }
                    }
                }

                // 如果房间时准备状态，开始倒计时。
                if (gameInfo.getGameState() == GameState.READY){
                    waitTime--;
                }


            }
        }.runTaskTimer(MiarsMegaWalls.getInstance(),0,20);
    }

    public Long getWaitTime(){
        return waitTime;
    }

    public GameInfo getGameInfo(){return this.gameInfo;}

    /**
     * 加入某个队伍
     * @param team 队伍
     */
    public void joinGameTeam(TeamType team, GamePlayer gamePlayer){
        GameTeam gameTeam = this.gameInfo
                .getGameTeams()
                .get(team);

        // 判断是否满员
        if(gameTeam.getGamePlayers().size()==PluginConfig.TEAM_LIMIT.get()){
            gamePlayer.sendMessage("&7很抱歉，该队伍已经满员，请尝试选择其它队伍。");
        }else {
            gameTeam.getGamePlayers().add(gamePlayer);
            gamePlayer.sendMessage("&7您成功加入了 "+team.getColor()+team.getType()+" &7队，共同努力获得最后的胜利！");
        }

        // 判断是否已经有队伍了,如果有,就退掉
        GameTeam gt = getGamePlayerTeam(gamePlayer);
        if (gt!=null){
            gt.getGamePlayers().remove(gamePlayer);
        }

    }

    /**
     * 获取该玩家所在的队伍类型
     * @param gamePlayer 游戏玩家
     * @return 队伍
     */
    public GameTeam getGamePlayerTeam(GamePlayer gamePlayer){
        for (GameTeam gt:this.gameInfo.getGameTeams().values()) {
            if (gt.getGamePlayers().contains(gamePlayer)){
                return gt;
            }
        }
        return null;
    }


    /**
     * 判断这两个玩家是否是队友
     * @param a
     * @param b
     * @return
     */
    public boolean isTeam(Player a,Player b){
        GamePlayer gamePlayerA = GamePlayerManager.getInstance().getGamePlayer(a);
        GamePlayer gamePlayerB = GamePlayerManager.getInstance().getGamePlayer(b);
        if (gamePlayerA.getTeamType() == gamePlayerB.getTeamType()){
            return true;
        }
        return false;
    }


}
