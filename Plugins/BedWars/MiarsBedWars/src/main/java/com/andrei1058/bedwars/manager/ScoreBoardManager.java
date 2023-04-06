package com.andrei1058.bedwars.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.core.utils.fastboard.FastBoard;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.storage.data.serverInfo.ServerInfoDataStorage;
import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.configuration.ConfigPath;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.andrei1058.bedwars.BedWars.getEconomy;
import static com.andrei1058.bedwars.BedWars.plugin;
import static com.andrei1058.bedwars.api.language.Language.getMsg;
import static com.andrei1058.bedwars.api.language.Language.getScoreboard;

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
    private Player player;
    private IArena arena;
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
        }.runTaskTimerAsynchronously(BedWars.plugin,0,10);
    }

    private void updateBoard(FastBoard board) {
        Player p = board.getPlayer();
        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(p);
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
        IArena arena = Arena.getArenaByPlayer(p);
        if (arena==null){
            return;
        }

        List<String> lines = new ArrayList<>();
        board.updateTitle("&e起床战争 &8| &e"+ ServerInfoDataStorage.getInstance().getServerInfo().getNameCn());
        lines.add("&7"+simpleDateFormat.format(System.currentTimeMillis())+" &8"+ ToolUtils.getServerCode());
        lines.add("");
        lines.addAll(getBoard(arena,p));
        lines.add("");
        lines.add("&7&o"+ ServerInfoDataStorage.getInstance().getServerInfo().getIp());

        board.updateLines(ColorParser.parse(lines));
    }

    public List<String> getBoard(IArena arena,Player player){
        List<String> lines = new ArrayList<>();

        if (arena.getStatus() == GameState.playing){
            int health = (int)player.getHealth();
            Objective obj = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);
            if (obj == null) {
                obj = player.getScoreboard().registerNewObjective(String.valueOf(System.currentTimeMillis()), "health");
                obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
                obj.setDisplayName(ChatColor.RED + "❤");
            }
            obj.setDisplayName(ChatColor.RED + "❤");
            obj.getScore(player).setScore(health);
        }

        if (arena.getStatus() == GameState.waiting) {
            lines = getScoreboard(player, "scoreboard." + arena.getGroup() + ".waiting", Messages.SCOREBOARD_DEFAULT_WAITING);
        } else if (arena.getStatus() == GameState.starting) {
            lines = getScoreboard(player, "scoreboard." + arena.getGroup() + ".starting", Messages.SCOREBOARD_DEFAULT_STARTING);

        } else if (arena.getStatus() == GameState.playing || arena.getStatus() == GameState.restarting) {
            lines = getScoreboard(player, "scoreboard." + arena.getGroup() + ".playing", Messages.SCOREBOARD_DEFAULT_PLAYING);
        }

        return papi(arena,player,lines);
    }

    public List<String> papi(IArena arena,Player player,List<String> lines){

        this.arena = arena;
        this.player = player;

        List<String> list = new ArrayList<>();
        int teamCount = 0;
        Language language = Language.getPlayerLanguage(player);
        String genericTeamFormat = language.m(Messages.FORMATTING_SCOREBOARD_TEAM_GENERIC);

        for (String line : lines) {

            // generic team placeholder {team}
            if ("{team}".equals(line.trim())) {
                if (arena.getTeams().size() > teamCount) {
                    ITeam team = arena.getTeams().get(teamCount++);
                    String teamName = team.getDisplayName(language);
                    line = genericTeamFormat
                            .replace("{TeamLetter}", String.valueOf(teamName.length() != 0 ? teamName.charAt(0) : ""))
                            .replace("{TeamColor}", team.getColor().chat().toString())
                            .replace("{TeamName}", teamName)
                            .replace("{TeamStatus}", "{Team" + team.getName() + "Status}");
                } else {
                    // skip line
                    continue;
                }
            }

            line = line
                    .replace("{map}", arena.getDisplayName())
                    .replace("{map_name}", arena.getArenaName())
                    .replace("{group}", arena.getDisplayGroup(player));

            for (ITeam currentTeam : arena.getTeams()) {
                final ChatColor color = currentTeam.getColor().chat();

                String result;
                if (currentTeam.isBedDestroyed()) {
                    if (currentTeam.getSize() > 0) {
                        result = getMsg(player, Messages.FORMATTING_SCOREBOARD_BED_DESTROYED)
                                .replace("{remainingPlayers}", String.valueOf(currentTeam.getSize()));
                    } else {
                        result = getMsg(player, Messages.FORMATTING_SCOREBOARD_TEAM_ELIMINATED);
                    }
                } else {
                    result = getMsg(player, Messages.FORMATTING_SCOREBOARD_TEAM_ALIVE);
                }
                if (currentTeam.isMember(player)) {
                    result += getMsg(player, Messages.FORMATTING_SCOREBOARD_YOUR_TEAM);
                }

                // Static team placeholders
                line = line
                        .replace("{Team" + currentTeam.getName() + "Color}", color.toString())
                        .replace("{Team" + currentTeam.getName() + "Name}", currentTeam.getDisplayName(language))
                        .replace("{Team" + currentTeam.getName() + "Status}",result)
                ;

            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(getMsg(player, Messages.FORMATTING_SCOREBOARD_DATE));
            String time;
            if (arena.getStatus() == GameState.playing || arena.getStatus() == GameState.restarting) {
                time =  getNextEventTime();
            } else {
                if (arena.getStatus() == GameState.starting) {
                    if (arena.getStartingTask() != null) {
                        time =  String.valueOf(arena.getStartingTask().getCountdown() + 1);
                    }else {
                        time =  dateFormat.format(new Date(System.currentTimeMillis()));
                    }
                }else {
                    time =  dateFormat.format(new Date(System.currentTimeMillis()));
                }

            }



            // General static placeholders
            line = line
                    .replace("{server_ip}", BedWars.config.getString(ConfigPath.GENERAL_CONFIG_PLACEHOLDERS_REPLACEMENTS_SERVER_IP))
                    .replace("{version}", plugin.getDescription().getVersion())
                    .replace("{playername}", player.getName())
                    .replace("{player}", player.getDisplayName())
                    .replace("{money}", String.valueOf(getEconomy().getMoney(player)))
                    .replace("{nextEvent}", getNextEventName())
                    .replace("{time}", time)
                    .replace("{on}",String.valueOf(arena.getPlayers().size()))
                    .replace("{max}",String.valueOf(arena.getMaxPlayers()))
                    .replace("{kills}", String.valueOf(arena.getPlayerKills(player,false)))
                    .replace("{finalKills}", String.valueOf(arena.getPlayerKills(player,true)))
                    .replace("{beds}", String.valueOf(arena.getPlayerBedsDestroyed(player)))
            ;
            // Add the line to the sidebar
            list.add(line);
        }

        return list;
    }

    @NotNull
    public String getNextEventName() {

        return switch (arena.getNextEvent()) {
            case EMERALD_GENERATOR_TIER_II -> getMsg(player, Messages.NEXT_EVENT_EMERALD_UPGRADE_II);
            case EMERALD_GENERATOR_TIER_III -> getMsg(player, Messages.NEXT_EVENT_EMERALD_UPGRADE_III);
            case DIAMOND_GENERATOR_TIER_II -> getMsg(player, Messages.NEXT_EVENT_DIAMOND_UPGRADE_II);
            case DIAMOND_GENERATOR_TIER_III -> getMsg(player, Messages.NEXT_EVENT_DIAMOND_UPGRADE_III);
            case GAME_END -> getMsg(player, Messages.NEXT_EVENT_GAME_END);
            case BEDS_DESTROY -> getMsg(player, Messages.NEXT_EVENT_BEDS_DESTROY);
            case ENDER_DRAGON -> getMsg(player, Messages.NEXT_EVENT_DRAGON_SPAWN);
        };
    }

    @NotNull
    public String getNextEventTime() {
        SimpleDateFormat nextEventDateFormat = new SimpleDateFormat(getMsg(player, Messages.FORMATTING_SCOREBOARD_NEXEVENT_TIMER));

        long time = switch (arena.getNextEvent()) {
            case EMERALD_GENERATOR_TIER_II, EMERALD_GENERATOR_TIER_III -> (((Arena) arena).upgradeEmeraldsCount) * 1000L;
            case DIAMOND_GENERATOR_TIER_II, DIAMOND_GENERATOR_TIER_III -> (((Arena) arena).upgradeDiamondsCount) * 1000L;
            case GAME_END -> (arena.getPlayingTask().getGameEndCountdown()) * 1000L;
            case BEDS_DESTROY -> (arena.getPlayingTask().getBedsDestroyCountdown()) * 1000L;
            case ENDER_DRAGON -> (arena.getPlayingTask().getDragonSpawnCountdown()) * 1000L;
        };
        return time == 0 ? "0" : nextEventDateFormat.format(new Date(time));
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

    /**
     * 添加玩家的记分板
     * @param p
     */
    public void joinPlayer(Player p){
        FastBoard board = new FastBoard(p);
        boards.put(p.getUniqueId(), board);
    }
}
