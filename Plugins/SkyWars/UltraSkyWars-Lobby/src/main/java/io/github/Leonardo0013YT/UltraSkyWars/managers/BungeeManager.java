package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.data.RedisMessenger;

public class BungeeManager {

    // UltraSkyWars-GameAdd || UltraSkyWars-GameUpdate || UltraSkyWars-GameRemove
    private RedisMessenger redis;
    private int games = 0;
    private UltraSkyWars plugin;

    public BungeeManager(UltraSkyWars plugin) {
        this.plugin = plugin;
        if (plugin.getCm().isBungeeModeEnabled() && plugin.getConfig().getBoolean("redis.enabled")) {
            redis = new RedisMessenger(plugin);
            redis.init();
        }
    }

    public void sendMessage(String channel, String message) {
        if (redis == null) return;
        redis.sendOutgoingMessage(channel, message);
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public void close() {
        if (redis != null) {
            redis.close();
        }
    }

    public RedisMessenger getRedis() {
        return redis;
    }
}