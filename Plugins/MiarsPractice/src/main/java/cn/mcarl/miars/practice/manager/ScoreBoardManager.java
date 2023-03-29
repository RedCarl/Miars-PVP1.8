package cn.mcarl.miars.practice.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.core.utils.fastboard.FastBoard;
import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import cn.mcarl.miars.storage.storage.data.serverInfo.ServerInfoDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

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

        p.getWorld().setGameRuleValue("doFireTick","false");
        p.getWorld().setGameRuleValue("doDaylightCycle","false");

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(p);

        List<String> lines = new ArrayList<>();
        board.updateTitle("&ePractice &8| &c"+ ServerInfoDataStorage.getInstance().getServerInfo().getNameCn());
        lines.add("&7"+simpleDateFormat.format(System.currentTimeMillis())+" &8"+ ToolUtils.getServerCode());
        lines.add("");

        if (state!=null){
            switch (state.getState()){
                case IDLE -> {

                }
                case READY -> {
                    lines.add("&7Ready to start!");
                }
                case GAME -> {
                    Player their = state.getPlayerA().equals(p.getName()) ? Bukkit.getPlayer(state.getPlayerB()) : Bukkit.getPlayer(state.getPlayerA());

                    lines.add("&7Fighting: &f"+their.getName());
                    lines.add("");
                    lines.add("&aYour Ping: &f"+((CraftPlayer) p).getHandle().ping+"ms");
                    lines.add("&cTheir Ping: &f"+((CraftPlayer) their).getHandle().ping+"ms");

                    if (FKitType.valueOf(PluginConfig.PRACTICE_SITE.MODE.get())==FKitType.BUILD_UHC){
                        int health = (int)p.getHealth();
                        Objective obj = p.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);
                        if (obj == null) {
                            obj = p.getScoreboard().registerNewObjective(String.valueOf(System.currentTimeMillis()), "health");
                            obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
                            obj.setDisplayName(ChatColor.RED + "❤");
                        }
                        obj.setDisplayName(ChatColor.RED + "❤");
                        obj.getScore(p).setScore(health);
                    }
                }
                case END -> {
                    lines.add("&7Game over!");
                }
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
