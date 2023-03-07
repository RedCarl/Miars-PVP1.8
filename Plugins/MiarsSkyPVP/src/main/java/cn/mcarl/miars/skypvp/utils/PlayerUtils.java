package cn.mcarl.miars.skypvp.utils;

import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.skypvp.conf.PluginConfig;
import cn.mcarl.miars.skypvp.items.SpawnSlimeball;
import cn.mcarl.miars.skypvp.manager.CombatManager;
import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import cn.mcarl.miars.storage.entity.skypvp.SPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

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

        player.getInventory().setItem(0, new ItemStack(Material.STONE_SWORD));
        player.getInventory().setItem(1, new ItemStack(Material.STONE_PICKAXE));
        player.getInventory().setItem(2, new ItemStack(Material.COOKED_BEEF,16));

        player.getInventory().setItem(39, new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setItem(38, new ItemStack(Material.GOLD_CHESTPLATE));
        player.getInventory().setItem(37, new ItemStack(Material.CHAINMAIL_LEGGINGS));
        player.getInventory().setItem(36, new ItemStack(Material.IRON_BOOTS));

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
}
