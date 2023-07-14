package com.andrei1058.bedwars.proxy.lobby.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.MiarsUtils;
import cn.mcarl.miars.storage.utils.fastboard.FastBoard;
import cn.mcarl.miars.storage.storage.data.serverInfo.ServerInfoDataStorage;
import com.andrei1058.bedwars.proxy.BedWarsProxy;
import me.clip.placeholderapi.PlaceholderAPI;
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
        }.runTaskTimerAsynchronously(BedWarsProxy.getPlugin(),0,20);
    }

    private void updateBoard(FastBoard board) {
        Player p = board.getPlayer();

        List<String> lines = new ArrayList<>();
        board.updateTitle("&e起床战争 &8┃ &e"+ ServerInfoDataStorage.getInstance().getServerInfo().getNameCn());
        lines.add("&7"+simpleDateFormat.format(System.currentTimeMillis())+" &8"+ MiarsUtils.getServerCode());
        lines.add("");
        lines.add("&6&l┃ &f等级: "+"&7["+BedWarsProxy.getLevelManager().getLevel(p)+"&7]");
        lines.add("&6&l┃ &f进度: &b"+BedWarsProxy.getLevelManager().getCurrentXpFormatted(p)+"&7/&a"+BedWarsProxy.getLevelManager().getRequiredXpFormatted(p));
        lines.add(BedWarsProxy.getLevelManager().getProgressBar(p));
        lines.add("");
        lines.add("&6&l┃ &f奖励箱: &8开发中");
        lines.add("&6&l┃ &f硬币: &6"+MiarsCore.getEcon().getBalance(p));
        lines.add("");
        lines.add("&6&l┃ &f总击杀数: &c" + (BedWarsProxy.getStatsCache().getPlayerKills(p.getUniqueId())+BedWarsProxy.getStatsCache().getPlayerFinalKills(p.getUniqueId())));
        lines.add("&6&l┃ &f总胜利数: &a" + BedWarsProxy.getStatsCache().getPlayerWins(p.getUniqueId()));
        lines.add("");
        lines.add("&7&o"+ ServerInfoDataStorage.getInstance().getServerInfo().getIp());

        board.updateLines(ColorParser.parse(PlaceholderAPI.setPlaceholders(p,lines)));
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
