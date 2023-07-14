package cn.mcarl.miars.core.utils;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.conf.PluginConfig;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.entity.MServerInfo;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.utils.ToolUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * @Author: carl0
 * @DATE: 2023/1/4 23:50
 */
public class MiarsUtils {

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


    public static void initPlayerNametag(Player player,boolean prefix){

        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(player);
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
        if (prefix){
            MiarsCore.getNametagAPI().setNametag(player,mRank.getPrefix(),mRank.getSuffix(),mRank.getPower());
        }else {
            MiarsCore.getNametagAPI().setNametag(player,mRank.getNameColor(),mRank.getNameColor(),mRank.getPower());
        }

    }


    public static void initPlayerNametag(Player player,String p,String s){
        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(player);

        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
        MiarsCore.getNametagAPI().setNametag(player,mRank.getPrefix(),mRank.getSuffix(),mRank.getPower());
    }


    public static void initPlayerNametag(Player player,String s,boolean prefix){
        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(player);
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
        if (prefix){
            MiarsCore.getNametagAPI().setNametag(player,mRank.getPrefix(),s,mRank.getPower());
        }else {
            MiarsCore.getNametagAPI().setNametag(player,mRank.getNameColor(),s,mRank.getPower());
        }
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

        Map<String, Object> args = (Map<String, Object>) section;

        World world = Bukkit.getWorld((String) args.get("world"));
        if (world == null) {
            throw new IllegalArgumentException("unknown world");
        }

        return new Location(world, NumberConversions.toDouble(args.get("x")), NumberConversions.toDouble(args.get("y")), NumberConversions.toDouble(args.get("z")), NumberConversions.toFloat(args.get("yaw")), NumberConversions.toFloat(args.get("pitch")));
    }

    public static ItemStack itemStackParse(@NotNull Object section) {

        Map<String, Object> args = (Map<String, Object>) section;

        Material type = Material.getMaterial((String) args.get("type"));
        short damage = 0;
        int amount = 1;

        if (args.containsKey("damage")) {
            damage = ((Number) args.get("damage")).shortValue();
        }

        if (args.containsKey("amount")) {
            amount = ((Number) args.get("amount")).intValue();
        }

        ItemStack result = new ItemStack(type, amount, damage);

        if (args.containsKey("enchantments")) { // Backward compatiblity, @deprecated
            Object raw = args.get("enchantments");

            if (raw instanceof Map<?, ?> map) {

                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    Enchantment enchantment = Enchantment.getByName(entry.getKey().toString());

                    if ((enchantment != null) && (entry.getValue() instanceof Integer)) {
                        result.addUnsafeEnchantment(enchantment, (Integer) entry.getValue());
                    }
                }
            }
        } else if (args.containsKey("meta")) { // We cannot and will not have meta when enchantments (pre-ItemMeta) exist
            Object raw = args.get("meta");
            if (raw instanceof ItemMeta) {
                result.setItemMeta((ItemMeta) raw);
            }
        }

        return result;
    }


    public static int accuracy(double num, double total, int scale){
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total * 100;
        return Integer.parseInt(df.format(accuracy_num));
    }

    public static void sendActionText(Player player, String message){
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }


    public static List<Color> getColors(){
        List<Color> colors = new ArrayList<>();
        colors.add(Color.AQUA);
        colors.add(Color.WHITE);
        colors.add(Color.TEAL);
        colors.add(Color.BLUE);
        colors.add(Color.LIME);
        colors.add(Color.OLIVE);
        colors.add(Color.ORANGE);
        colors.add(Color.PURPLE);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(Color.BLACK);
        colors.add(Color.FUCHSIA);
        colors.add(Color.NAVY);
        colors.add(Color.MAROON);
        colors.add(Color.GREEN);
        colors.add(Color.GRAY);
        return colors;
    }

    public static List<String> initLorePapi(List<String> list, boolean is, String type, String value){

        List<String> stringList = new ArrayList<>();

        for (String s:list) {
            String ss = s;



            if (ss.contains("{play_")){
                String name = cn.mcarl.miars.storage.utils.ToolUtils.subString(ss,"{play_","}");
                MServerInfo mServerInfo = ServerManager.getInstance().getServerInfo(value);

                String server = ServerManager.getInstance().getServerOnline(name);

                if (server!=null && mServerInfo!=null){

                    if (Objects.equals(type, "gui")){
                        if (!is){
                            stringList.add("&a  点击连接");
                        }else {
                            stringList.add("&a► 点击连接");
                        }
                    }

                    ss = s.replace(
                            "{play_"+name+"}",
                            server
                    );
                    stringList.add(ss);
                }else {
                    ss = "&c暂未上线，敬请期待...";
                    stringList.add(ss);
                }

            }else {
                if (!("&a► 点击连接".equals(ss))){
                    stringList.add(ss);
                }
            }
        }

        return stringList;
    }


    public static List<String> initLorePapi(List<String> list, boolean is, String type){

        List<String> stringList = new ArrayList<>();

        for (String s:list) {
            String ss = s;



            if (ss.contains("{play_")){
                String name = cn.mcarl.miars.storage.utils.ToolUtils.subString(ss,"{play_","}");


                String server = ServerManager.getInstance().getServerOnline(name);

                if (server!=null){

                    if (Objects.equals(type, "gui")){
                        if (!is){
                            stringList.add("&a  点击连接");
                        }else {
                            stringList.add("&a► 点击连接");
                        }
                    }

                    ss = s.replace(
                            "{play_"+name+"}",
                            server
                    );
                    stringList.add(ss);
                }else {
                    ss = "&c暂未上线，敬请期待...";
                    stringList.add(ss);
                }

            }else {
                if (!("&a► 点击连接".equals(ss))){
                    stringList.add(ss);
                }
            }
        }

        return stringList;
    }


    public static String getServerCode(){
        String port = String.valueOf(MiarsCore.getInstance().getServer().getPort());
        String name = PluginConfig.SERVER_INFO.NAME.get().substring(0,1);
        String pr = null;
        if (PluginConfig.SERVER_INFO.NAME.get().contains("_")){
            pr = ToolUtils.subStringToEnd(PluginConfig.SERVER_INFO.NAME.get(),"_");
        }
        if (PluginConfig.SERVER_INFO.NAME.get().contains("-")){
            pr = ToolUtils.subStringToEnd(PluginConfig.SERVER_INFO.NAME.get(),"-");
        }
        if (pr==null){
            pr="a";
        }
        String code = name+port.substring(1,port.length())+pr.substring(0,1);

        return code.toUpperCase();
    }
}
