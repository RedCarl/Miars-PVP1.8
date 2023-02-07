package cn.mcarl.miars.core.utils;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.enums.FKitType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: carl0
 * @DATE: 2023/1/4 23:50
 */
public class ToolUtils {
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static DecimalFormat decimalFormat = new DecimalFormat("#0"); // 保留两位小数，四舍五入
    public static Location spawn = new Location(Bukkit.getWorld("Spawn"),1403,135,809,-179.95163f,-0.09630036f);

    /**
     * 获取范围内最近的玩家
     * @param pixelmonLocation 坐标
     * @return 玩家
     */
    public static Player getRandPlayer(Location pixelmonLocation) {
        double distance = Double.MAX_VALUE;
        Player player = null;
        Iterator<Player> var6 = (Iterator<Player>) Bukkit.getOnlinePlayers().iterator();
        while (var6.hasNext()) {
            Player p = var6.next();
            Location loc = p.getLocation();
            if (pixelmonLocation.getWorld().getName().equalsIgnoreCase(loc.getWorld().getName()) && pixelmonLocation.distance(loc) < distance) {
                distance = pixelmonLocation.distance(loc);
                player = p;
            }
        }
        return player;
    }

    /**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     */
    public static String subString(String str, String strStart, String strEnd) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* 开始截取 */
        return str.substring(strStartIndex, strEndIndex).substring(strStart.length());
    }

    /**
     * 随机字符串
     * @param length 长度
     * @return 字符
     */
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取库存该物品的数量
     * @param inventory 库存
     * @param itemStack 物品
     * @return 物品
     */
    public static ItemStack getAllItem(Inventory inventory, ItemStack itemStack){
        itemStack.setAmount(0);
        for (ItemStack i:inventory) {
            if (i!=null&&i.getType()==itemStack.getType()){
                itemStack.setAmount(itemStack.getAmount()+i.getAmount());
            }
        }
        return itemStack;
    }

    /**
     * 秒格式化
     */
    public static String getDate(long date){
        if (date<60) {
            return "&c"+date+" &7秒";
        }else if (date>60&&date<3600) {
            long m = date/60;
            long s = date%60;
            return "&c"+m+" &7分"+" &c"+s+" &7秒";
        }else {
            long h = date/3600;
            long m = (date%3600)/60;
            long s = (date%3600)%60;
            return "&c"+h+" &7小时"+" &c"+m+" &7分"+" &c"+s+" &7秒";
        }
    }

    /**
     * 检测玩家背包是否没有位置了
     * @param player 玩家
     * @return 是否
     */
    public static boolean isInventoryNoNull(Player player,ItemStack itemStack){
        boolean varOn = true;
        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i) == null) {
                varOn = false;
                break;
            }
        }
        if (varOn){
            for (int i = 0; i < 36; i++) {
                if (player.getInventory().getItem(i).getType() == itemStack.getType()) {
                    if (64-player.getInventory().getItem(i).getAmount()>=itemStack.getAmount()){
                        varOn = false;
                        break;
                    }
                }
            }
        }
        if (varOn){
            player.sendMessage(ColorParser.parse("&7您的背包没有多余的位置来存放物品,请整理背包后再试!"));
            player.playSound(player.getLocation(), Sound.VILLAGER_NO,1,1);
            return true;
        }
        return false;
    }

    //数值转换
    public static String toNumber(float number) {
        String str = String.valueOf(number);
        if (number >= 0 && number < 1000) {
            BigDecimal b = new BigDecimal(number);
            str = String.valueOf(b.setScale(2, RoundingMode.DOWN).doubleValue());
        } else if (number >= 1000 && number < 10000) {
            double num = number / 1000;
            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(2, RoundingMode.DOWN).doubleValue();
            str = f1 + " K";
        } else if (number >= 10000 && number < 1000000){
            double num = number / 10000;
            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(2, RoundingMode.DOWN).doubleValue();
            str = f1 + " W";
        } else if (number >= 1000000 && number < 1000000000){
            double num = number / 1000000;
            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(2, RoundingMode.DOWN).doubleValue();
            str = f1 + " M";
        } else if (number >= 1000000000 && number < 1000000000000f){
            double num = number / 1000000000;
            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(2, RoundingMode.DOWN).doubleValue();
            str = f1 + " B";
        } else if (number >= 1000000000000f && number < 1000000000000000f){
            double num = number / 1000000000000f;
            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(2, RoundingMode.DOWN).doubleValue();
            str = f1 + " T";
        } else if (number >= 1000000000000000f){
            double num = number / 1000000000000000f;
            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(2, RoundingMode.DOWN).doubleValue();
            str = f1 + " Q";
        }
        return str;
    }

    /**
     * 玩家状态初始化
     * 初始化 血量 饱食度 氧气度 药水效果
     */
    public static void playerInitialize(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setFireTicks(0);
        player.setGameMode(GameMode.SURVIVAL);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }
    /**
     * 设置玩家库存
     */
    public static void setPlayerInv(Player player, Inventory inv){
        int a = 0;
        for (ItemStack i:inv) {
            player.getInventory().setItem(a,i);
            a++;
        }
    }

    public static String decimalFormat(double d, int length) {
        DecimalFormat decimalFormat;
        if (length <= 0) {
            decimalFormat = new DecimalFormat("########");
        } else {
            decimalFormat = new DecimalFormat("########." + multipleString("#", length));
        }

        return decimalFormat.format(d);
    }

    public static @NotNull String multipleString(@NotNull String string, int multiple) {
        StringBuilder stringBuilder = new StringBuilder();

        for(byte b = 0; b < multiple; ++b) {
            stringBuilder.append(string);
        }

        return stringBuilder.toString();
    }

    /**
     * 将玩家的背包转为FInv
     * @param player
     * @return
     */
    public static FInventory playerToFInv(Player player, FKitType fKitType){

        Map<Integer,ItemStack> backpack = new HashMap<>();
        for (int i = 9; i <= 35; i++) {
            backpack.put(i,player.getInventory().getItem(i));
        }
        Map<Integer,ItemStack> itemCote = new HashMap<>();
        for (int i = 0; i <= 8; i++) {
            itemCote.put(i,player.getInventory().getItem(i));
        }
        return new FInventory(
                fKitType,
                player.getPlayer().getInventory().getItem(39),
                player.getPlayer().getInventory().getItem(38),
                player.getPlayer().getInventory().getItem(37),
                player.getPlayer().getInventory().getItem(36),
                backpack,
                itemCote

        );
    }

    public static void reduceXpBar (final Player p, int ticks)
    {
        p.setExp(1F);


        final float division = p.getExp() / ticks;

        new BukkitRunnable()
        {
            public void run()
            {
                float currentLevel = p.getExp();
                p.setExp(currentLevel - division);
                currentLevel = p.getExp();

                // Reached 0
                if (currentLevel <= 0)
                    cancel();
            }
        }.runTaskTimer(MiarsCore.getInstance(), 0L, 1L);

    }



    public static Long getTheDay(){

        long current = System.currentTimeMillis();

        return current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
    }


    /**
     * 自动初始化该玩家装备
     * @param p
     */
    public static void autoEquip(Player p,FInventory fInv){
        p.getInventory().clear();

        Inventory inv = p.getInventory();

        inv.setItem(39,fInv.getHelmet());
        inv.setItem(38,fInv.getChestPlate());
        inv.setItem(37,fInv.getLeggings());
        inv.setItem(36,fInv.getBoots());

        for (Integer i:fInv.getItemCote().keySet()){
            inv.setItem(i,fInv.getItemCote().get(i));
        }

        for (Integer i:fInv.getBackpack().keySet()){
            inv.setItem(i,fInv.getBackpack().get(i));
        }
    }
}
