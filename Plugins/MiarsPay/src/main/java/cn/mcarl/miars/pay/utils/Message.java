
package cn.mcarl.miars.pay.utils;

import org.bukkit.entity.Player;
import java.util.Map;
import java.util.HashMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

public class Message
{
    private static File file;
    private static YamlConfiguration config;
    
    public static void loadMessage(final Plugin plugin) {
        Message.file = new File(plugin.getDataFolder(), "message.yml");
        if (!Message.file.exists()) {
            plugin.saveResource("message.yml", false);
        }
        Message.config = YamlConfiguration.loadConfiguration(Message.file);
    }
    
    public static void sendMessage(final String node, final CommandSender sender) {
        sendMessage(node, new HashMap<String, Object>(0), sender);
    }
    
    public static void sendMessage(final String node, final Map<String, Object> replace, final CommandSender sender) {
        if (sender instanceof Player) {
            sendMessage(node, replace, (Player)sender);
        }
        else {
            final String string = Message.config.getString(node, node);
            if (!string.isEmpty()) {
                final String parse = Utils.parse(string, replace);
                for (final String s : parse.split("\n")) {
                    sender.sendMessage(s);
                }
            }
        }
    }
    
    public static void sendMessage(final String node, final Player player) {
        sendMessage(node, new HashMap<String, Object>(0), player);
    }
    
    public static void sendMessage(final String node, final Map<String, Object> replace, final Player player) {
        final String string = Message.config.getString(node, node);
        if (!string.isEmpty()) {
            final String parse = Utils.parse(string, replace, player);
            for (final String s : parse.split("\n")) {
                player.sendMessage(s);
            }
        }
    }
    
    public static String get(final String node) {
        final String string = Message.config.getString(node, node);
        return Utils.parse(string, new HashMap<String, Object>());
    }
}
