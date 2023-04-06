package cn.mcarl.miars.skypvp.utils;

import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.MiarsUtil;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.skypvp.conf.PluginConfig;
import cn.mcarl.miars.skypvp.items.SpawnSlimeball;
import cn.mcarl.miars.skypvp.manager.CombatManager;
import cn.mcarl.miars.storage.entity.skypvp.SPlayer;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class PlayerUtils {

    public static boolean isProtectedRegion(Player player){
        Location p = player.getLocation();
        Location min = PluginConfig.PROTECTED_REGION.MIN.get();
        Location max = PluginConfig.PROTECTED_REGION.MAX.get();
        if (p.getBlockX()<=max.getBlockX() && p.getBlockY()<=max.getBlockY() && p.getBlockZ()<=max.getBlockZ()){
            if (p.getBlockX()>=min.getBlockX() && p.getBlockY()>=min.getBlockY() && p.getBlockZ()>=min.getBlockZ()){
                return true;
            }
        }

        return false;
    }

    public static boolean isAfkRegion(Player player){
        Location p = player.getLocation();
        Location min = PluginConfig.AFK_REGION.MIN.get();
        Location max = PluginConfig.AFK_REGION.MAX.get();
        if (p.getBlockX()<=max.getBlockX() && p.getBlockY()<=max.getBlockY() && p.getBlockZ()<=max.getBlockZ()){
            if (p.getBlockX()>=min.getBlockX() && p.getBlockY()>=min.getBlockY() && p.getBlockZ()>=min.getBlockZ()){
                return true;
            }
        }

        return false;
    }

    /**
     * 初始化玩家状态
     * @param player
     */
    public static void initializePlayer(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        CombatManager.getInstance().clear(player);

        player.getInventory().setItem(0,
                new ItemBuilder(Material.STONE_SWORD)
                        .addEnchant(Enchantment.DAMAGE_ALL,1,true)
                        .setNbtBoolean("no-drop",true)
                        .toItemStack()
                );
        player.getInventory().setItem(1,
                new ItemBuilder(Material.STONE_PICKAXE)
                        .addEnchant(Enchantment.DIG_SPEED,1,true)
                        .setNbtBoolean("no-drop",true)
                        .toItemStack()
                );
        player.getInventory().setItem(2,
                new ItemBuilder(Material.COOKED_BEEF)
                        .setNbtBoolean("no-drop",true)
                        .setAmount(16)
                        .toItemStack()
                );

        List<Color> list = MiarsUtil.getColors();
        int index = (int) (Math.random()* list.size());
        player.getInventory().setItem(38,
                new ItemBuilder(Material.LEATHER_CHESTPLATE)
                        .setLeatherArmorColor(list.get(index))
                        .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,4,true)
                        .setNbtBoolean("no-drop",true)
                        .toItemStack()
        );
        player.getInventory().setItem(37,
                new ItemBuilder(Material.LEATHER_LEGGINGS)
                        .setLeatherArmorColor(list.get(index))
                        .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,4,true)
                        .setNbtBoolean("no-drop",true)
                        .toItemStack()
                );
        player.getInventory().setItem(36,
                new ItemBuilder(Material.LEATHER_BOOTS)
                        .setLeatherArmorColor(Color.GRAY)
                        .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,4,true)
                        .setNbtBoolean("no-drop",true)
                        .toItemStack()
                );

        new SpawnSlimeball().give(player,8);
    }


    /**
     * 获取玩家的KD
     * @param sPlayer
     * @return
     */
    public static double getPlayerKD(SPlayer sPlayer){

        if (sPlayer.getKillsCount()==0){
            return 0;
        }

        if (sPlayer.getDeathCount()==0){
            return sPlayer.getKillsCount();
        }

        return Double.parseDouble(ToolUtils.decimalFormat((double) sPlayer.getKillsCount() / (double) sPlayer.getDeathCount(),1));
    }


    public static boolean isNullInv(Player player){

        for (ItemStack is:player.getInventory()) {
            if (is!=null){
                return false;
            }
        }

        return true;
    }
}
