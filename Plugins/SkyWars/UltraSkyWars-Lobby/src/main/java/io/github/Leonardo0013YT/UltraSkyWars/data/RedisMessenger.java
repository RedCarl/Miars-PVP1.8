package io.github.Leonardo0013YT.UltraSkyWars.data;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.RedisMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisMessenger {

    private static UltraSkyWars plugin;
    private JedisPool jedisPool;
    private Subscription sub;

    public RedisMessenger(UltraSkyWars plugin) {
        RedisMessenger.plugin = plugin;
    }

    public void init() {
        connect();
        if (plugin.getConfig().getBoolean("redis.reconnect.enabled")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    reconnect();
                }
            }.runTaskTimerAsynchronously(plugin, plugin.getConfig().getInt("redis.reconnect.delay") * 20L, plugin.getConfig().getInt("redis.reconnect.delay") * 20L);
        }
    }

    private void connect() {
        boolean ssl = plugin.getConfig().getBoolean("redis.ssl");
        JedisPoolConfig conf = new JedisPoolConfig();
        if (!plugin.getConfig().getString("redis.password").equals("")) {
            this.jedisPool = new JedisPool(conf, plugin.getConfig().getString("redis.host"), plugin.getConfig().getInt("redis.port"), Protocol.DEFAULT_TIMEOUT, plugin.getConfig().getString("redis.password"), ssl);
        } else {
            this.jedisPool = new JedisPool(conf, plugin.getConfig().getString("redis.host"), plugin.getConfig().getInt("redis.port"), Protocol.DEFAULT_TIMEOUT, ssl);
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            this.sub = new Subscription(plugin, this);
            try (Jedis jedis = jedisPool.getResource()) {
                plugin.sendLogMessage("§eCanales registrados.");
                jedis.subscribe(this.sub, "usw:callback", "usw:gameupdate", "usw:party", "usw:remove", "usw:parties");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void reconnect() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.ping();
            plugin.reconnect();
        } catch (JedisConnectionException e) {
            sub.unsubscribe();
            jedisPool.destroy();
            connect();
            plugin.reconnect();
        }
    }

    public void sendOutgoingMessage(String channel, String outgoingMessage) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish(channel, outgoingMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPing() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.ping();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        this.sub.unsubscribe();
        this.jedisPool.destroy();
    }

    private static class Subscription extends JedisPubSub {
        private RedisMessenger parent;
        private UltraSkyWars plugin;

        private Subscription(UltraSkyWars plugin, RedisMessenger parent) {
            this.plugin = plugin;
            this.parent = parent;
        }

        @Override
        public void onMessage(String channel, String message) {
            if (plugin.is1_13to16()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getServer().getPluginManager().callEvent(new RedisMessageEvent(channel, message)));
            } else {
                plugin.getServer().getPluginManager().callEvent(new RedisMessageEvent(channel, message));
            }
        }
    }
}