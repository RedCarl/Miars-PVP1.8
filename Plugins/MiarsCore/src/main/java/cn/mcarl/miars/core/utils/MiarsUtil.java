package cn.mcarl.miars.core.utils;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.utils.nametagapi.NametagManager;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

public class MiarsUtil {
    /**
     * 获取服务器版本信息
     *
     * @param player 玩家
     */
    public static void getVersion(CommandSender player) {
        player.sendMessage(ColorParser.parse("&r"));
        player.sendMessage(ColorParser.parse("&3&lMiarsSystem&8(forked Bukkit) &7服务端相关信息"));
        player.sendMessage(ColorParser.parse("&r &b| &7运行版本 &bgit-MiarsSystem-1.8.8 (MC: 1.8.8)"));
        player.sendMessage(ColorParser.parse("&r &b| &7接口版本 &b") + Bukkit.getBukkitVersion());
        player.sendMessage(ColorParser.parse("&r &b| &f相关服务端更新请查询开发者内部构建服务器。"));
        player.sendMessage(ColorParser.parse("&r &b| &f相关插件状态查询请参考 &b/Plugins &f指令。"));
    }

    /**
     * 获取服务器插件列表
     *
     * @param sender 玩家
     */
    public static void getPlugins(CommandSender sender) {
        Plugin[] pluginsOriginal = Bukkit.getPluginManager().getPlugins();
        List<Plugin> pluginsRely = new ArrayList<>();
        StringBuilder pluginName = new StringBuilder();
        int hidePluginCount = 0;
        for (Plugin plugin : pluginsOriginal) {
            String s = plugin.getName().toLowerCase(Locale.ROOT);
            if (sender instanceof Player && !s.contains("miars") && (
                    s.contains("lib")
                            || s.contains("api")
                            || s.contains("world")
                            || s.contains("spark")
                            || s.contains("citizen")
                            || s.contains("vault")
                            || s.contains("lore")
                            || s.contains("perm")
            )) {
                ++hidePluginCount;
                continue;
            }
            if (isPrivatePlugin(plugin.getDescription())) {
                pluginName.append("&b").append(plugin.getName()).append("&fⒸ&8, ");
            } else {
                pluginsRely.add(plugin);
            }
        }
        for (Plugin plugin : pluginsRely) {
            pluginName.append("&3").append(plugin.getName()).append("&7, ");
        }
        sender.sendMessage(ColorParser.parse("&7您好,本服有 &f" + (pluginsOriginal.length - hidePluginCount) + "&7 插件 &b(标有&fⒸ&b为原创)&7:"));
        sender.sendMessage(ColorParser.parse(pluginName.substring(0, pluginName.length() - 2)));
    }

    public static boolean isPrivatePlugin(PluginDescriptionFile desc) {
        if (desc.getAuthors().stream().map(e -> e.toLowerCase(Locale.ROOT)).anyMatch(author -> author.contains("red_carl"))) {
            return true;
        }
        if (desc.getMain().toLowerCase(Locale.ROOT).contains("miars")) {
            return true;
        }
        return false;
    }


    public static void initPlayerNametag(Player player){

        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(player);
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());


        NametagManager.sendTeamsToPlayer(player);
        NametagManager.clear(player.getName());
        NametagManager.overlap(
                player.getName(),
                ColorParser.parse(mRank.getPrefix()),
                ColorParser.parse(mRank.getSuffix())
        );

    }

    public static ItemStack createSkull(String value) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta)item.getItemMeta();
        GameProfile gp = new GameProfile(UUID.randomUUID(), null);
        gp.getProperties().put("textures", new Property("textures", value));

        try {
            Field field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, gp);
        } catch (Exception var4) {
        }

        item.setItemMeta(meta);
        return item;
    }

    public static Location locationParse(@NotNull Object section) {
        Map<String, Object> map = (Map<String, Object>) section;
        return new Location(
                Bukkit.getWorld((String) map.get("world")),
                (Double) map.get("x"),
                (Double) map.get("y"),
                (Double) map.get("z"),
                ((Double) (map.get("yaw") != null ? map.get("yaw") : 0)).floatValue(),
                ((Double) (map.get("pitch") != null ? map.get("pitch") : 0)).floatValue()
        );
    }
}
