package io.github.Leonardo0013YT.UltraSkyWars.data;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.specials.RedisPartyMessageEvent;
import io.github.Leonardo0013YT.UltraSkyWars.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.UUID;

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
            if (!channel.startsWith("usw:")) return;
            if (channel.equals("usw:parties")) {
                Bukkit.getServer().getPluginManager().callEvent(new RedisPartyMessageEvent(message));
            }
            if (channel.equals("usw:party")) {
                String[] msg = message.split(":");
                String action = msg[0];
                if (action.equalsIgnoreCase("SEND")) {
                    UUID uuid = UUID.fromString(msg[1]);
                    String server = msg[2];
                    Player on = Bukkit.getPlayer(uuid);
                    if (on != null) {
                        plugin.sendToServer(on, server);
                        on.sendMessage(plugin.getLang().get(on, "parties.joinGame"));
                    }
                }
            }
            if (channel.equals("usw:callback")) {
                String[] msg = message.split(":");
                String type = msg[0];
                if (plugin.getCm().isBungeeModeGame() && plugin.getCm().getGameType().name().equals(type)) {
                    Game game = plugin.getGm().getBungee();
                    if (game != null) {
                        if (game.isState(State.WAITING) || (game.isState(State.STARTING) && game.getStarting() > 3)) {
                            plugin.getBm().sendUpdateGame(game);
                            plugin.getGem().updateInventories(type, "none");
                            plugin.sendDebugMessage("§aChannel", "CallBack", "§eMensaje Recibido", message);
                        }
                    }
                }
            }
            if (channel.equals("usw:gameupdate")) {
                String[] msg = message.split(":");
                String server = msg[0], map = msg[1], type = msg[3], state = msg[2], color = msg[6];
                int players = Integer.parseInt(msg[4]), max = Integer.parseInt(msg[5]);
                plugin.getGm().updateGame(server, map, color, state, type, players, max);
                plugin.sendDebugMessage("§aChannel", "GameUpdate", "§eMensaje Recibido", message);
                plugin.getGem().updateInventories(type, "none");
            }
        }
    }
}