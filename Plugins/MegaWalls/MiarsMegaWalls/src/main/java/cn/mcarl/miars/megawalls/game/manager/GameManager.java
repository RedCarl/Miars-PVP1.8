package cn.mcarl.miars.megawalls.game.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.conf.PluginConfig;
import cn.mcarl.miars.megawalls.game.entitiy.GameInfo;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.entitiy.GameTeam;
import cn.mcarl.miars.megawalls.game.entitiy.GameWall;
import cn.mcarl.miars.megawalls.game.entitiy.enums.GameState;
import cn.mcarl.miars.megawalls.game.entitiy.enums.TeamType;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
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
    private Long waitTime = PluginConfig.WAIT_TIME.get();
    private Long readyTime = PluginConfig.READY_TIME.get();
    private Long wallTime = PluginConfig.WALL_TIME.get();
    private Long witherFury = PluginConfig.WITHER_FURY.get();
    private Long gameOver = PluginConfig.GAME_OVER.get();
    private Long kickPlayer = PluginConfig.KICK_PLAYER.get();

    World world = Bukkit.getWorld("world");

    /**
     * 初始化房间
     */
    public void initGameInfo(){

        world.setDifficulty(Difficulty.EASY);

        // 房间名称，会根据该名称查询数据库中房间的详细数据
        String name = PluginConfig.NAME.get();

        // 初始化房间状态
        gameInfo.setGameState(GameState.WAIT);
        gameInfo.setWitherFury(true);

        // 初始化墙
        gameInfo.setGameWall(new GameWall(
                PluginConfig.WALL.MIN.get(),
                PluginConfig.WALL.MAX.get(),
                false
        ));

        // 初始化队伍
        gameInfo.getGameTeams().put(TeamType.RED,
                new GameTeam(
                        TeamType.RED,
                        new ArrayList<>(),
                        PluginConfig.RED_TEAM.SPAWN.get(),
                        PluginConfig.RED_TEAM.WITHER_SPAWN.get(),
                        null,
                        false,
                        PluginConfig.RED_TEAM.DOOR.MIN.get(),
                        PluginConfig.RED_TEAM.DOOR.MAX.get(),
                        PluginConfig.RED_TEAM.DOOR.MATERIAL.get(),
                        PluginConfig.RED_TEAM.CASTLE.MIN.get(),
                        PluginConfig.RED_TEAM.CASTLE.MAX.get()
                )
        );
        gameInfo.getGameTeams().put(TeamType.YELLOW,
                new GameTeam(
                        TeamType.YELLOW,
                        new ArrayList<>(),
                        PluginConfig.YELLOW_TEAM.SPAWN.get(),
                        PluginConfig.YELLOW_TEAM.WITHER_SPAWN.get(),
                        null,
                        false,
                        PluginConfig.YELLOW_TEAM.DOOR.MIN.get(),
                        PluginConfig.YELLOW_TEAM.DOOR.MAX.get(),
                        PluginConfig.YELLOW_TEAM.DOOR.MATERIAL.get(),
                        PluginConfig.YELLOW_TEAM.CASTLE.MIN.get(),
                        PluginConfig.YELLOW_TEAM.CASTLE.MAX.get()
                )
        );
        gameInfo.getGameTeams().put(TeamType.BLUE,
                new GameTeam(
                        TeamType.BLUE,
                        new ArrayList<>(),
                        PluginConfig.BLUE_TEAM.SPAWN.get(),
                        PluginConfig.BLUE_TEAM.WITHER_SPAWN.get(),
                        null,
                        false,
                        PluginConfig.BLUE_TEAM.DOOR.MIN.get(),
                        PluginConfig.BLUE_TEAM.DOOR.MAX.get(),
                        PluginConfig.BLUE_TEAM.DOOR.MATERIAL.get(),
                        PluginConfig.BLUE_TEAM.CASTLE.MIN.get(),
                        PluginConfig.BLUE_TEAM.CASTLE.MAX.get()
                )
        );
        gameInfo.getGameTeams().put(TeamType.GREEN,
                new GameTeam(
                        TeamType.GREEN,
                        new ArrayList<>(),
                        PluginConfig.GREEN_TEAM.SPAWN.get(),
                        PluginConfig.GREEN_TEAM.WITHER_SPAWN.get(),
                        null,
                        false,
                        PluginConfig.GREEN_TEAM.DOOR.MIN.get(),
                        PluginConfig.GREEN_TEAM.DOOR.MAX.get(),
                        PluginConfig.GREEN_TEAM.DOOR.MATERIAL.get(),
                        PluginConfig.GREEN_TEAM.CASTLE.MIN.get(),
                        PluginConfig.GREEN_TEAM.CASTLE.MAX.get()
                )
        );

        gameInfo.setLobbySpawn(
                PluginConfig.LOBBY.SPAWN.get()
        );
    }

    public void tick(){

        if (waitTime==0 && gameInfo.getGameState() == GameState.READY){
            gameInfo.setGameState(GameState.START);

            List<Player> ps = new ArrayList<>(Bukkit.getOnlinePlayers());
            Iterator<Player> players = ps.listIterator();

            // 队伍分配
            while (players.hasNext()){
                for (TeamType teamType:gameInfo.getGameTeams().keySet()) {
                    if (players.hasNext()){
                        GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(players.next());
                        gamePlayer.setTeamType(teamType);
                        gameInfo.getGameTeams().get(teamType).getGamePlayers().add(gamePlayer);
                    }
                }
            }

            // 传送至玩家到出生点
            for (TeamType teamType:gameInfo.getGameTeams().keySet()) {
                GameTeam gameTeam = gameInfo.getGameTeams().get(teamType);
                for (GamePlayer gamePlayer : gameTeam.getGamePlayers()) {
                    gamePlayer.tp(gameTeam.getSpawn());
                    gamePlayer.clearInv();

                    gamePlayer.sendTitle(
                            "&e&l超级战墙 &8| &e&l磐石",
                            gameTeam.getTeamType().getTeamName()+"队"
                    );
                    gamePlayer.sendMessage("&7您被分配到了 "+gameTeam.getTeamType().getTeamName()+"队 &7努力战胜其它队伍。");
                    gamePlayer.sendMessage("&7世界 &e巨墙 &7将在 &e"+ ToolUtils.getDate(PluginConfig.WALL_TIME.get())+" &7后倒塌。");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            gamePlayer.sendTitle(
                                    gameTeam.getTeamType().getTeamName()+"队",
                                    "&7世界 &e巨墙 &7将在 &e"+ ToolUtils.getDate(PluginConfig.WALL_TIME.get())+" &7后倒塌。"
                            );
                        }
                    }.runTaskLater(MiarsMegaWalls.getInstance(),100);

                }
                gameTeam.spawnWither(gameTeam);
            }
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
                    waitTime = PluginConfig.WAIT_TIME.get();
                }
            }
            case START -> {
                if (readyTime!=0){
                    readyTime--;
                }else {
                    gameInfo.setGameState(GameState.CONDUCT);
                }
            }
            case CONDUCT -> {
                if (wallTime!=0){
                    wallTime--;
                }else {
                    if (!gameInfo.getGameWall().isCollapse()){
                        ToolUtils.reloadWallsBlocks(gameInfo.getGameWall().getMinCorner(),gameInfo.getGameWall().getMaxCorner());
                    }
                    gameInfo.getGameWall().setCollapse(true);
                    if (witherFury!=0){
                        witherFury--;
                        gameInfo.setWitherFury(true);
                    }else {
                        gameInfo.setWitherFury(false);
                        if (gameOver!=0){
                            gameOver--;
                            // 游戏结束
                        }
                    }
                }
            }
            case END -> {
                if (kickPlayer.equals(PluginConfig.KICK_PLAYER.get())){
                    // 结算
                    gameEnd();
                }

                if (kickPlayer!=0){
                    kickPlayer--;
                }else {
                    for (Player p:Bukkit.getOnlinePlayers()) {
                        MiarsCore.getBungeeApi().connect(p,"megawalls");
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (Bukkit.getOnlinePlayers().size() == 0) {
                                Bukkit.shutdown();
                            }
                        }
                    }.runTaskTimer(MiarsMegaWalls.getInstance(), 0L, 20L);
                }
            }
        }

        // 如果房间时准备状态，开始倒计时。
        if (gameInfo.getGameState() == GameState.READY){
            waitTime--;
        }
    }

    public Long getWaitTime(){
        return waitTime;
    }

    public Long getReadyTime(){
        return readyTime;
    }

    public Long getWallTime(){
        return wallTime;
    }

    public Long getWitherFury(){
        return witherFury;
    }
    public Long getGameOver(){
        return gameOver;
    }
    public Long getKickPlayer(){
        return kickPlayer;
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

    /**
     * 游戏结束并结算
     */
    public void gameEnd(){
        for (GameTeam gameTeam:gameInfo.getGameTeams().values()) {
            for (GamePlayer gamePlayer:gameTeam.getGamePlayers()) {
                if (gameTeam.getTeamType()==gameInfo.getWinTeam().getTeamType()){
                    gamePlayer.sendTitle(
                            "&6&l胜利",
                            "&7队伍获得本场游戏的冠军！"
                    );
                }else {
                    gamePlayer.sendTitle(
                            "&c&l失败",
                            "&7恭喜 "+gameInfo.getWinTeam().getTeamType().getTeamName()+"队 &7获得本场游戏冠军！"
                    );
                }
            }
        }
        List<String> messages = new ArrayList();
        messages.add("           &6PanshiMC 磐石 &8-&6 超级战墙");
        messages.add(" ");
        messages.add("           &7由 " + gameInfo.getWinTeam().getTeamType().getTeamName() + "队"+" &7获得本场游戏冠军");
        messages.add(" ");
        messages.add(" ");
        for (String s:messages) {
            Bukkit.broadcastMessage(ColorParser.parse(s));
        }


        gameInfo.setGameState(GameState.END);
    }


}
