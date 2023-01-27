package cn.mcarl.miars.practiceffa.utils;

import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import cn.mcarl.miars.practiceffa.listener.ProtocolListener;
import cn.mcarl.miars.practiceffa.manager.CombatManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

/**
 * @Author: carl0
 * @DATE: 2023/1/3 22:38
 */
public class FFAUtil {

    /**
     * 玩家在某个坐标的范围
     *
     * @param player   玩家
     * @param location 坐标
     * @param range    范围
     * @return 是/否
     */
    public static boolean isRange(Player player, Location location, int range) {
        int playerX = player.getLocation().getBlockX();
        //int playerY = player.getLocation().getBlockY();
        int playerZ = player.getLocation().getBlockZ();

        int locationX = location.getBlockX();
        //int locationY = location.getBlockY();
        int locationZ = location.getBlockZ();

        if (locationX - playerX <= range && locationX - playerX >= (-range)) {// || Math.abs(playerY - locationY) >= range
            if (locationZ - playerZ <= range && locationZ - playerZ >= (-range)){// || Math.abs(playerY - locationY) >= range
                return true;
            }
        }
        return false;
    }

    /**
     * 获取某个物品的范围
     *
     * @param locationOne 第一个坐标
     * @param locationTwo 第二个坐标
     * @param range       范围
     * @return 结果
     */
    public static boolean isItemRange(Location locationOne, Location locationTwo, int range) {
        int playerX = locationOne.getBlockX();
        int playerZ = locationOne.getBlockZ();

        int locationX = locationTwo.getBlockX();
        int locationZ = locationTwo.getBlockZ();

        if (locationX - playerX <= range && locationX - playerX >= (-range)) {
            return locationZ - playerZ <= range && locationZ - playerZ >= (-range);
        }
        return false;
    }

    /**
     * 给这个玩家一个虚拟的屏障，使用 红色玻璃 作为墙。
     * Todo 玻璃跟着玩家的高度改变高度
     * @param player 玩家
     */
    public static void setVirtualBorder(Player player) {
        for (int i = 0; i < (PluginConfig.FFA_SITE.RADIUS.get() * 2 + 1); i++) {
            if (isRange(
                    player,
                    new Location(
                            PluginConfig.FFA_SITE.LOCATION.get().getWorld(),
                            (PluginConfig.FFA_SITE.LOCATION.get().getX() + PluginConfig.FFA_SITE.RADIUS.get()),
                            PluginConfig.FFA_SITE.LOCATION.get().getY(),
                            (PluginConfig.FFA_SITE.LOCATION.get().getZ() + PluginConfig.FFA_SITE.RADIUS.get()) - i),
                    PluginConfig.FFA_SITE.BORDER_RADIUS.get()
            )) {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get(); j++) {
                    Location location = new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() + PluginConfig.FFA_SITE.RADIUS.get()), PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() + PluginConfig.FFA_SITE.RADIUS.get()) - i);
                    if (ProtocolListener.isDisablePlace(player,location.getBlock())) {
                        continue;
                    }
                    player.sendBlockChange(location, Material.STAINED_GLASS, (byte) 14);
                }
            } else {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get(); j++) {
                    player.sendBlockChange(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() + PluginConfig.FFA_SITE.RADIUS.get()), PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() + PluginConfig.FFA_SITE.RADIUS.get()) - i), Material.AIR, (byte) 0);
                }
            }

            if (isRange(
                    player,
                    new Location(
                            PluginConfig.FFA_SITE.LOCATION.get().getWorld(),
                            (PluginConfig.FFA_SITE.LOCATION.get().getX() + PluginConfig.FFA_SITE.RADIUS.get()) - i,
                            PluginConfig.FFA_SITE.LOCATION.get().getY(),
                            (PluginConfig.FFA_SITE.LOCATION.get().getZ() + PluginConfig.FFA_SITE.RADIUS.get())),
                    PluginConfig.FFA_SITE.BORDER_RADIUS.get()
            )) {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get(); j++) {
                    Location location = new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() + PluginConfig.FFA_SITE.RADIUS.get()) - i, PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() + PluginConfig.FFA_SITE.RADIUS.get()));
                    if (ProtocolListener.isDisablePlace(player,location.getBlock())) {
                        continue;
                    }
                    player.sendBlockChange(location, Material.STAINED_GLASS, (byte) 14);
                }
            } else {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get(); j++) {
                    player.sendBlockChange(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() + PluginConfig.FFA_SITE.RADIUS.get()) - i, PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() + PluginConfig.FFA_SITE.RADIUS.get())), Material.AIR, (byte) 0);
                }
            }

            if (isRange(
                    player,
                    new Location(
                            PluginConfig.FFA_SITE.LOCATION.get().getWorld(),
                            (PluginConfig.FFA_SITE.LOCATION.get().getX() - PluginConfig.FFA_SITE.RADIUS.get()),
                            PluginConfig.FFA_SITE.LOCATION.get().getY(),
                            (PluginConfig.FFA_SITE.LOCATION.get().getZ() - PluginConfig.FFA_SITE.RADIUS.get()) + i),
                    PluginConfig.FFA_SITE.BORDER_RADIUS.get()
            )) {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get(); j++) {
                    Location location = new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() - PluginConfig.FFA_SITE.RADIUS.get()), PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() - PluginConfig.FFA_SITE.RADIUS.get()) + i);
                    if (ProtocolListener.isDisablePlace(player,location.getBlock())) {
                        continue;
                    }
                    player.sendBlockChange(location, Material.STAINED_GLASS, (byte) 14);
                }
            } else {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get(); j++) {
                    player.sendBlockChange(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() - PluginConfig.FFA_SITE.RADIUS.get()), PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() - PluginConfig.FFA_SITE.RADIUS.get()) + i), Material.AIR, (byte) 0);
                }
            }

            if (isRange(
                    player,
                    new Location(
                            PluginConfig.FFA_SITE.LOCATION.get().getWorld(),
                            (PluginConfig.FFA_SITE.LOCATION.get().getX() - PluginConfig.FFA_SITE.RADIUS.get()) + i,
                            PluginConfig.FFA_SITE.LOCATION.get().getY(),
                            (PluginConfig.FFA_SITE.LOCATION.get().getZ() - PluginConfig.FFA_SITE.RADIUS.get())),
                    PluginConfig.FFA_SITE.BORDER_RADIUS.get()
            )) {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get(); j++) {
                    Location location = new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() - PluginConfig.FFA_SITE.RADIUS.get()) + i, PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() - PluginConfig.FFA_SITE.RADIUS.get()));
                    if (ProtocolListener.isDisablePlace(player,location.getBlock())) {
                        continue;
                    }
                    player.sendBlockChange(location, Material.STAINED_GLASS, (byte) 14);
                }
            } else {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get(); j++) {
                    player.sendBlockChange(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() - PluginConfig.FFA_SITE.RADIUS.get()) + i, PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() - PluginConfig.FFA_SITE.RADIUS.get())), Material.AIR, (byte) 0);
                }
            }
        }
    }

    /**
     * 清理多余的边界
     *
     * @param player 玩家
     */
    public static void removeVirtualBorder(Player player) {

        for (int i = 0; i < (PluginConfig.FFA_SITE.RADIUS.get() * 2 + 1); i++) {
            if (isRange(player, new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() + PluginConfig.FFA_SITE.RADIUS.get()), PluginConfig.FFA_SITE.LOCATION.get().getY(), (PluginConfig.FFA_SITE.LOCATION.get().getZ() + PluginConfig.FFA_SITE.RADIUS.get()) - i), PluginConfig.FFA_SITE.BORDER_RADIUS.get())) {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get() + 1; j++) {
                    player.sendBlockChange(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() + PluginConfig.FFA_SITE.RADIUS.get()), PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() + PluginConfig.FFA_SITE.RADIUS.get()) - i), Material.AIR, (byte) 0);
                }
            } else {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get() + 1; j++) {
                    player.sendBlockChange(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() + PluginConfig.FFA_SITE.RADIUS.get()), PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() + PluginConfig.FFA_SITE.RADIUS.get()) - i), Material.AIR, (byte) 0);
                }
            }

            if (isRange(player, new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() + PluginConfig.FFA_SITE.RADIUS.get()) - i, PluginConfig.FFA_SITE.LOCATION.get().getY(), (PluginConfig.FFA_SITE.LOCATION.get().getZ() + PluginConfig.FFA_SITE.RADIUS.get())), PluginConfig.FFA_SITE.BORDER_RADIUS.get())) {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get() + 1; j++) {
                    player.sendBlockChange(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() + PluginConfig.FFA_SITE.RADIUS.get()) - i, PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() + PluginConfig.FFA_SITE.RADIUS.get())), Material.AIR, (byte) 0);
                }
            } else {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get() + 1; j++) {
                    player.sendBlockChange(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() + PluginConfig.FFA_SITE.RADIUS.get()) - i, PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() + PluginConfig.FFA_SITE.RADIUS.get())), Material.AIR, (byte) 0);
                }
            }

            if (isRange(player, new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() - PluginConfig.FFA_SITE.RADIUS.get()), PluginConfig.FFA_SITE.LOCATION.get().getY(), (PluginConfig.FFA_SITE.LOCATION.get().getZ() - PluginConfig.FFA_SITE.RADIUS.get()) + i), PluginConfig.FFA_SITE.BORDER_RADIUS.get())) {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get() + 1; j++) {
                    player.sendBlockChange(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() - PluginConfig.FFA_SITE.RADIUS.get()), PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() - PluginConfig.FFA_SITE.RADIUS.get()) + i), Material.AIR, (byte) 0);
                }
            } else {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get() + 1; j++) {
                    player.sendBlockChange(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() - PluginConfig.FFA_SITE.RADIUS.get()), PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() - PluginConfig.FFA_SITE.RADIUS.get()) + i), Material.AIR, (byte) 0);

                }
            }

            if (isRange(player, new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() - PluginConfig.FFA_SITE.RADIUS.get()) + i, PluginConfig.FFA_SITE.LOCATION.get().getY(), (PluginConfig.FFA_SITE.LOCATION.get().getZ() - PluginConfig.FFA_SITE.RADIUS.get())), PluginConfig.FFA_SITE.BORDER_RADIUS.get())) {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get() + 1; j++) {
                    player.sendBlockChange(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() - PluginConfig.FFA_SITE.RADIUS.get()) + i, PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() - PluginConfig.FFA_SITE.RADIUS.get())), Material.AIR, (byte) 0);
                }
            } else {
                for (int j = 0; j < PluginConfig.FFA_SITE.BORDER_RADIUS.get() + 1; j++) {
                    player.sendBlockChange(new Location(PluginConfig.FFA_SITE.LOCATION.get().getWorld(), (PluginConfig.FFA_SITE.LOCATION.get().getX() - PluginConfig.FFA_SITE.RADIUS.get()) + i, PluginConfig.FFA_SITE.LOCATION.get().getY() + 1 + j, (PluginConfig.FFA_SITE.LOCATION.get().getZ() - PluginConfig.FFA_SITE.RADIUS.get())), Material.AIR, (byte) 0);
                }
            }
        }

    }

    /**
     * 将出生点的范围使用 黄色羊毛 圈出来
     */
    public static void setBorder(){
        for (int i = 0; i < (PluginConfig.FFA_SITE.RADIUS.get()*2+1); i++) {
            Block currentBlock = PluginConfig.FFA_SITE.LOCATION.get().getWorld().getBlockAt((PluginConfig.FFA_SITE.LOCATION.get().getBlockX()+PluginConfig.FFA_SITE.RADIUS.get()), PluginConfig.FFA_SITE.LOCATION.get().getBlockY(), (PluginConfig.FFA_SITE.LOCATION.get().getBlockZ()+PluginConfig.FFA_SITE.RADIUS.get())-i);
            currentBlock.setType(Material.WOOL);
            currentBlock.setData((byte)14);
        }

        for (int i = 0; i < (PluginConfig.FFA_SITE.RADIUS.get()*2+1); i++) {
            Block currentBlock = PluginConfig.FFA_SITE.LOCATION.get().getWorld().getBlockAt((PluginConfig.FFA_SITE.LOCATION.get().getBlockX()+PluginConfig.FFA_SITE.RADIUS.get())-i, PluginConfig.FFA_SITE.LOCATION.get().getBlockY(), (PluginConfig.FFA_SITE.LOCATION.get().getBlockZ()+PluginConfig.FFA_SITE.RADIUS.get()));
            currentBlock.setType(Material.WOOL);
            currentBlock.setData((byte)14);
        }

        for (int i = 0; i < (PluginConfig.FFA_SITE.RADIUS.get()*2+1); i++) {
            Block currentBlock = PluginConfig.FFA_SITE.LOCATION.get().getWorld().getBlockAt((PluginConfig.FFA_SITE.LOCATION.get().getBlockX()-PluginConfig.FFA_SITE.RADIUS.get()), PluginConfig.FFA_SITE.LOCATION.get().getBlockY(), (PluginConfig.FFA_SITE.LOCATION.get().getBlockZ()-PluginConfig.FFA_SITE.RADIUS.get())+i);
            currentBlock.setType(Material.WOOL);
            currentBlock.setData((byte)14);
        }

        for (int i = 0; i < (PluginConfig.FFA_SITE.RADIUS.get()*2+1); i++) {
            Block currentBlock = PluginConfig.FFA_SITE.LOCATION.get().getWorld().getBlockAt((PluginConfig.FFA_SITE.LOCATION.get().getBlockX()-PluginConfig.FFA_SITE.RADIUS.get())+i, PluginConfig.FFA_SITE.LOCATION.get().getBlockY(), (PluginConfig.FFA_SITE.LOCATION.get().getBlockZ()-PluginConfig.FFA_SITE.RADIUS.get()));
            currentBlock.setType(Material.WOOL);
            currentBlock.setData((byte)14);
        }
    }

    /**
     * 将出生点外部分的范围使用 基岩 圈出来
     */
    public static void setGlassBorder(){
        for (int a = 0; a < PluginConfig.FFA_SITE.LOCATION.get().getY()+1; a++) {
            for (int j = 1; j <= PluginConfig.FFA_SITE.BORDER_RADIUS.get(); j++) {
                for (int i = 0; i < ((PluginConfig.FFA_SITE.RADIUS.get()+j)*2+1); i++) {
                    Block currentBlock = PluginConfig.FFA_SITE.LOCATION.get().getWorld().getBlockAt((PluginConfig.FFA_SITE.LOCATION.get().getBlockX()+(PluginConfig.FFA_SITE.RADIUS.get()+j)), a, (PluginConfig.FFA_SITE.LOCATION.get().getBlockZ()+(PluginConfig.FFA_SITE.RADIUS.get()+j))-i);
                    currentBlock.setType(Material.GRASS);
                    currentBlock.setData((byte)14);
                }

                for (int i = 0; i < ((PluginConfig.FFA_SITE.RADIUS.get()+j)*2+1); i++) {
                    Block currentBlock = PluginConfig.FFA_SITE.LOCATION.get().getWorld().getBlockAt((PluginConfig.FFA_SITE.LOCATION.get().getBlockX()+(PluginConfig.FFA_SITE.RADIUS.get()+j))-i, a, (PluginConfig.FFA_SITE.LOCATION.get().getBlockZ()+(PluginConfig.FFA_SITE.RADIUS.get()+j)));
                    currentBlock.setType(Material.GRASS);
                    currentBlock.setData((byte)14);
                }

                for (int i = 0; i < ((PluginConfig.FFA_SITE.RADIUS.get()+j)*2+1); i++) {
                    Block currentBlock = PluginConfig.FFA_SITE.LOCATION.get().getWorld().getBlockAt((PluginConfig.FFA_SITE.LOCATION.get().getBlockX()-(PluginConfig.FFA_SITE.RADIUS.get()+j)), a, (PluginConfig.FFA_SITE.LOCATION.get().getBlockZ()-(PluginConfig.FFA_SITE.RADIUS.get()+j))+i);
                    currentBlock.setType(Material.GRASS);
                    currentBlock.setData((byte)14);
                }

                for (int i = 0; i < ((PluginConfig.FFA_SITE.RADIUS.get()+j)*2+1); i++) {
                    Block currentBlock = PluginConfig.FFA_SITE.LOCATION.get().getWorld().getBlockAt((PluginConfig.FFA_SITE.LOCATION.get().getBlockX()-(PluginConfig.FFA_SITE.RADIUS.get()+j))+i, a, (PluginConfig.FFA_SITE.LOCATION.get().getBlockZ()-(PluginConfig.FFA_SITE.RADIUS.get()+j)));
                    currentBlock.setType(Material.GRASS);
                    currentBlock.setData((byte)14);
                }
            }
        }
    }

    /**
     * 将保护区的范围使用 黑岩石 圈出来
     */
    public static void setFFAGameBorder(){
        for (int j = 1; j < 4; j++) {
            for (int i = 0; i < ((PluginConfig.FFA_SITE.RADIUS.get()*6)*2+1); i++) {
                Block currentBlock = PluginConfig.FFA_SITE.LOCATION.get().getWorld().getBlockAt((PluginConfig.FFA_SITE.LOCATION.get().getBlockX()+(PluginConfig.FFA_SITE.RADIUS.get()*6)), j, ((PluginConfig.FFA_SITE.LOCATION.get().getBlockZ()+(PluginConfig.FFA_SITE.RADIUS.get()*6))-i));
                currentBlock.setType(Material.OBSIDIAN);
            }

            for (int i = 0; i < ((PluginConfig.FFA_SITE.RADIUS.get()*6)*2+1); i++) {
                Block currentBlock = PluginConfig.FFA_SITE.LOCATION.get().getWorld().getBlockAt(((PluginConfig.FFA_SITE.LOCATION.get().getBlockX()+(PluginConfig.FFA_SITE.RADIUS.get()*6))-i), j, (PluginConfig.FFA_SITE.LOCATION.get().getBlockZ()+(PluginConfig.FFA_SITE.RADIUS.get()*6)));
                currentBlock.setType(Material.OBSIDIAN);
            }

            for (int i = 0; i < ((PluginConfig.FFA_SITE.RADIUS.get()*6)*2+1); i++) {
                Block currentBlock = PluginConfig.FFA_SITE.LOCATION.get().getWorld().getBlockAt((PluginConfig.FFA_SITE.LOCATION.get().getBlockX()-(PluginConfig.FFA_SITE.RADIUS.get()*6)), j, ((PluginConfig.FFA_SITE.LOCATION.get().getBlockZ()-(PluginConfig.FFA_SITE.RADIUS.get()*6))+i));
                currentBlock.setType(Material.OBSIDIAN);
            }

            for (int i = 0; i < ((PluginConfig.FFA_SITE.RADIUS.get()*6)*2+1); i++) {
                Block currentBlock = PluginConfig.FFA_SITE.LOCATION.get().getWorld().getBlockAt(((PluginConfig.FFA_SITE.LOCATION.get().getBlockX()-(PluginConfig.FFA_SITE.RADIUS.get()*6))+i), j, (PluginConfig.FFA_SITE.LOCATION.get().getBlockZ()-(PluginConfig.FFA_SITE.RADIUS.get()*6)));
                currentBlock.setType(Material.OBSIDIAN);
            }
        }
    }

    /**
     * 初始化玩家状态
     * @param player
     */
    public static void initializePlayer(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        CombatManager.getInstance().clear(player);
    }


    /**
     * 获取玩家的KD
     * @param fPlayer
     * @return
     */
    public static double getPlayerKD(FPlayer fPlayer){

        if (fPlayer.getKillsCount()==0){
            return 0;
        }

        if (fPlayer.getDeathCount()==0){
            return fPlayer.getKillsCount();
        }

        return Double.parseDouble(ToolUtils.decimalFormat((double) fPlayer.getKillsCount() / (double) fPlayer.getDeathCount(),1));
    }

}
