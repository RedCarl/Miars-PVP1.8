package io.github.Leonardo0013YT.UltraSkyWars.interfaces;

import io.github.Leonardo0013YT.UltraSkyWars.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;
import java.util.UUID;

public interface Database {

    int getRanking(UUID uuid);

    void loadMultipliers(CallBackAPI<Boolean> request);

    void createMultiplier(String type, String name, double amount, long ending, CallBackAPI<Boolean> request);

    boolean removeMultiplier(int id);

    void loadTopElo();

    void loadTopCoins();

    void loadTopKills();

    void loadTopWins();

    void loadTopDeaths();

    void loadPlayer(Player p);

    void savePlayer(Player p);

    void saveAll(CallBackAPI<Boolean> done);

    void savePlayerSync(UUID uuid);

    HashMap<UUID, SWPlayer> getPlayers();

    void close();

    void createPlayer(UUID uuid, String name, SWPlayer ps);

    Connection getConnection();

    void clearStats(Player p);

    SWPlayer getSWPlayer(Player p);
}