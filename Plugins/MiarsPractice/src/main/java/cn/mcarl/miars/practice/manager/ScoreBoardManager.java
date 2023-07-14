package cn.mcarl.miars.practice.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.MiarsUtils;
import cn.mcarl.miars.storage.entity.practice.Arena;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import cn.mcarl.miars.storage.utils.fastboard.FastBoard;
import cn.mcarl.miars.practice.MiarsPractice;
import cn.mcarl.miars.storage.entity.practice.ArenaState;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.storage.data.practice.PracticeArenaStateDataStorage;
import cn.mcarl.miars.storage.storage.data.serverInfo.ServerInfoDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
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
    DecimalFormat decimalFormat = new DecimalFormat("00");
    DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy/M/d");
    ZoneId zoneId = ZoneId.of("America/New_York");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss a", Locale.US);
    public void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    for (FastBoard board : boards.values()) {
                        updateBoard(board);
                    }
                }catch (ConcurrentModificationException ignored){}

                LocalDate currentDate = LocalDate.now();
                String formattedDate = currentDate.format(formatterDate);
                LocalTime currentTime = LocalTime.now();
                String formattedTime = currentTime.format(formatter) + " " + zoneId.getDisplayName(TextStyle.SHORT, Locale.US);

                MiarsCore.getInstance().getTabHeaderAndFooter().getHeader().setLines(
                        "&f"+formattedDate+" &7| &bKazer Network &7| &f"+formattedTime,
                        "&r");
                MiarsCore.getInstance().getTabHeaderAndFooter().getFooter().setLines(
                        "&r",
                        "&fYou are playing &bPractice &fon &bkazer.gg");
            }
        }.runTaskTimerAsynchronously(MiarsCore.getInstance(),0,5);
    }

    private void updateBoard(FastBoard board) {
        Player p = board.getPlayer();

        p.getWorld().setGameRuleValue("doFireTick","false");
        p.getWorld().setGameRuleValue("doDaylightCycle","false");

        ArenaState state = PracticeArenaStateDataStorage.getInstance().getArenaStateByPlayer(p.getName());
        if (state==null){
            return;
        }

        Arena arena = ArenaManager.getInstance().getArenaById(state);

        List<String> lines = new ArrayList<>();
        board.updateTitle("&b&lPractice");
        lines.add("&f&7&m---------------------");

        switch (state.getState()){
            case READY,GAME -> {
                lines.add("&fKit: "+state.getQueueType().getColor()+state.getFKitType().getName()+" &7(DN34G)");
            }
        }
        switch (state.getState()){
            case IDLE -> {

            }
            case READY -> {
                lines.add("");
                lines.add("&fStatus");
                lines.add("&aReady to start!");
            }
            case GAME -> {
                int seconds = (int) ((System.currentTimeMillis()- state.getStartTime()) / 1000);
                int minutes = seconds / 60;
                int remainingSeconds = seconds % 60;
                String time = decimalFormat.format(minutes) + ":" + decimalFormat.format(remainingSeconds);
                lines.add("&fDuration: &b" + time);
                Player their = state.getPlayerA().equals(p.getName()) ? Bukkit.getPlayer(state.getPlayerB()) : Bukkit.getPlayer(state.getPlayerA());

                switch (MiarsPractice.getModeType()){
                    case BUILD_UHC,NO_DEBUFF,SUMO,BOW,COMBO -> {
                        lines.add("");
                        lines.add("&bFighting");
                        lines.add("&f"+their.getName());
                        lines.add("");
                        lines.add("&fYour Ping: &b"+((CraftPlayer) p).getHandle().ping+"ms");
                        lines.add("&fTheir Ping: &b"+((CraftPlayer) their).getHandle().ping+"ms");

                        if (MiarsPractice.getModeType() == FKitType.BUILD_UHC){
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
                    case BOXING -> {
                        lines.add("&fFighting: &f"+their.getName());
                        lines.add("");

                        lines.add("&fHits: "+BoxingManager.getInstance().getDifference(p.getUniqueId(),their.getUniqueId()));
                        lines.add("&a  You: &b"+BoxingManager.getInstance().getBoxingData(p.getUniqueId()));
                        lines.add("&c  Their: &b"+BoxingManager.getInstance().getBoxingData(their.getUniqueId()));
                        lines.add("");
                        lines.add("&aYour Ping: &b"+((CraftPlayer) p).getHandle().ping+"ms");
                        lines.add("&cTheir Ping: &b"+((CraftPlayer) their).getHandle().ping+"ms");
                    }
                }
            }
            case END -> {
//                int seconds = (int) ((state.getEndTime() - state.getStartTime()) / 1000);
//                int minutes = seconds / 60;
//                int remainingSeconds = seconds % 60;
//                String time = decimalFormat.format(minutes) + ":" + decimalFormat.format(remainingSeconds);
//                lines.add("&fMap: &b" +arena.getName());
//                lines.add("&fDuration: &b" + time);
//                lines.add("");
                lines.add("&fMatch ennded!");
            }
        }

        lines.add("");
        lines.add("&b"+ ServerInfoDataStorage.getInstance().getServerInfo().getIp());
        lines.add("&f&7&m---------------------");

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
