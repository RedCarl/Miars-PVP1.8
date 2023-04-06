package io.github.Leonardo0013YT.UltraSkyWars.utils;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Utils {

    private static String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ", "BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ"};
    private static DecimalFormat df = new DecimalFormat("##.#");
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static ItemStack getIcon(Settings config, String path) {
        if (config.isSet(path + ".icon.value")) {
            return new ItemUtils(XMaterial.PLAYER_HEAD).setUrl(config.get(path + ".icon.value")).setDisplayName(config.get(path + ".icon.meta.display-name")).setLore(config.get(path + ".icon.meta.lore")).build();
        } else {
            if (config.isSet(path + ".icon")){
                return config.getConfig().getItemStack(path + ".icon");
            }
            return config.getConfig().getItemStack(path + ".item");
        }
    }

    public static String formatDouble(double d) {
        return df.format(d);
    }

    public static String toGson(SWPlayer sw) {
        return UltraSkyWars.getGson().toJson(sw, SWPlayer.class);
    }

    public static SWPlayer fromGson(String data) {
        return UltraSkyWars.getGson().fromJson(data, SWPlayer.class);
    }

    public static ArmorStand getArmorStand(Location loc) {
        ArmorStand armor = loc.getWorld().spawn(loc, ArmorStand.class);
        armor.setGravity(false);
        armor.setVisible(false);
        armor.setSmall(true);
        return armor;
    }

    public static List<String> orderABC(List<String> names) {
        return names.stream().sorted().collect(Collectors.toList());
    }

    public static List<Integer> orderDESC(List<Integer> names) {
        names.sort(Collections.reverseOrder());
        return names;
    }

    public static String convertTime(int timeInSeconds) {
        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;
        String formattedTime = "";
        if (hours > 0) {
            if (hours < 10)
                formattedTime += "0";
            formattedTime += hours + ":";
        }
        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";
        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds;
        return formattedTime;
    }

    public static String getDate() {
        return sdf.format(new Date());
    }

    public static void setData(Block b, byte data) {
        b.setData(data);
    }

    public static void updateSB(Player p) {
        UltraSkyWars.get().getSb().update(p);
    }

    public static void updateSB() {
        Bukkit.getOnlinePlayers().forEach(pl -> UltraSkyWars.get().getSb().update(pl));
    }

    public static String parseBoolean(boolean bool) {
        return (bool) ? UltraSkyWars.get().getLang().get(null, "activated") : UltraSkyWars.get().getLang().get(null, "deactivated");
    }

    public static String getFormatedLocation(Location loc) {
        if (loc == null) {
            return "§cNot set!";
        }
        return loc.getWorld().getName() + ", " + df.format(loc.getX()) + ", " + df.format(loc.getY()) + ", " + df.format(loc.getZ());
    }

    public static Location getStringLocation(String location) {
        if (location == null) return null;
        String[] l = location.split(";");
        if (l.length < 6) return null;
        World world = Bukkit.getWorld(l[0]);
        double x = Double.parseDouble(l[1]);
        double y = Double.parseDouble(l[2]);
        double z = Double.parseDouble(l[3]);
        float yaw = Float.parseFloat(l[4]);
        float pitch = Float.parseFloat(l[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String getLocationString(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
    }

    public static boolean existsFile(String schematic) {
        String s = schematic + ".schematic";
        String s2 = schematic + ".schem";
        File file = new File(Bukkit.getWorldContainer() + "/plugins/WorldEdit/schematics", s);
        File file2 = new File(Bukkit.getWorldContainer() + "/plugins/FastAsyncWorldEdit/schematics", s2);
        if (UltraSkyWars.get().is1_13to16()) {
            return file2.exists();
        }
        return file.exists();
    }

    public static void setCleanPlayer(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        for (PotionEffect e : p.getActivePotionEffects()) {
            p.removePotionEffect(e.getType());
        }
        p.setFlying(false);
        p.setAllowFlight(false);
        p.setFireTicks(0);
        p.setWalkSpeed(0.2f);
        p.setFlySpeed(0.1f);
        p.setFoodLevel(20);
        p.setMaxHealth(20.0D);
        p.setHealth(20.0D);
        p.setGameMode(GameMode.SURVIVAL);
    }

    @SuppressWarnings("deprecation")
    public static Block getBlockFaced(Block b) {
        switch (b.getData()) {
            case 2:
                return b.getRelative(BlockFace.SOUTH);
            case 3:
                return b.getRelative(BlockFace.NORTH);
            case 4:
                return b.getRelative(BlockFace.EAST);
            case 5:
                return b.getRelative(BlockFace.WEST);
            default:
                return b;
        }
    }

    public static int getMaxPages(int size, int maxPerPage) {
        return (size / maxPerPage) + 1;
    }

    public static Color getRandomColor() {
        Color[] colors = new Color[]{Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.AQUA, Color.LIME};
        return colors[ThreadLocalRandom.current().nextInt(0, colors.length)];
    }

    public static String getProgressBar(int current, int max) {
        float percent = (float) current / max;
        double por = percent * 100;
        return new DecimalFormat("####.#").format(por);
    }

    public static String getProgressBar(int current, int max, int totalBars) {
        UltraSkyWars plugin = UltraSkyWars.get();
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);
        int leftOver = (totalBars - progressBars);
        StringBuilder sb = new StringBuilder();
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < progressBars; i++) {
            in.append(plugin.getLang().get(null, "progressBar.symbol"));
        }
        for (int i = 0; i < leftOver; i++) {
            out.append(plugin.getLang().get(null, "progressBar.symbol"));
        }
        sb.append(plugin.getLang().get(null, "progressBar.in"));
        sb.append(in.toString());
        sb.append(plugin.getLang().get(null, "progressBar.out"));
        sb.append(out.toString());
        double por = percent * 100;
        String p = new DecimalFormat("####.#").format(por);
        return plugin.getLang().get(null, "progressBar.finish").replaceAll("<progress>", sb.toString()).replaceAll("<percent>", p);
    }

}