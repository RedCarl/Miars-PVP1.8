package cn.mcarl.miars.practiceffa.manager;

import cn.hutool.core.util.RandomUtil;
import cn.mcarl.miars.practiceffa.MiarsPracticeFFA;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeathChestManager {
    private static final DeathChestManager instance = new DeathChestManager();
    public static DeathChestManager getInstance() {
        return instance;
    }

    private HashMap<Location, Long> deathChest = new HashMap<>();


    public void init(){
        tick();
    }

    public void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {
                //清理死亡奖励箱
                for (Location l : new ArrayList<>(deathChest.keySet())) {
                    if (System.currentTimeMillis() - deathChest.get(l) > 15000) {
                        if (l.getBlock().getType() != Material.CHEST) {
                            continue;
                        }
                        if (l.getBlock().getType() != Material.CHEST) {
                            return;
                        }
                        Chest chest = (Chest) l.getBlock().getState();
                        chest.getBlockInventory().clear();
                        chest.getBlock().setType(Material.AIR);
                        deathChest.remove(l);
                    }
                }
            }
        }.runTaskTimer(MiarsPracticeFFA.getInstance(),0,20);
    }

    /**
     * 生成死亡箱子
     *
     * @param player 死亡的玩家
     */
    public void generateChests(Player player, Location deathLocation) {

        World world = deathLocation.getWorld();
        Block currentBlock = world.getBlockAt(deathLocation);
        // 判断是否没有方块，如果有就向上取
        while (currentBlock.getType()!=Material.AIR){
            deathLocation = currentBlock.getLocation();
            deathLocation.add(0,1,0);

            currentBlock = world.getBlockAt(deathLocation);
        }

        currentBlock.setType(Material.CHEST);

        if (deathLocation.getBlock().getType() == Material.CHEST) {
            Chest chest = (Chest) deathLocation.getBlock().getState();

            chest.getBlockInventory().setContents(initRewardChest(player).getContents());
            deathChest.put(deathLocation, System.currentTimeMillis());
        }

    }

    private Inventory initRewardChest(Player player) {

        //头颅
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta header = (SkullMeta) head.getItemMeta();
        header.setOwner(player.getName());
        head.setItemMeta(header);
        ItemMeta im = head.getItemMeta();
        im.setDisplayName("§c" + player.getName() + " §7的头");
        List<String> lores = new ArrayList<>();
        try {
            if (player.getLastDamageCause().getEntity() instanceof Player p){
                lores.add("§7Ta死在了§f§l" + p.getName() + "§7的手上");
            }else {
                lores.add("§7Ta死的莫名其妙～");
            }

        } catch (Exception ignored) {
        }
        im.setLore(lores);
        head.setItemMeta(im);

        List<Inventory> inventories = new ArrayList<>();
        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST,player.getName());
        //金苹果
        ItemStack heal = new ItemBuilder(Material.POTION, 1).setData((short) 16421).toItemStack();
        ItemStack speed = new ItemBuilder(Material.POTION, 1).setData((short) 8226).toItemStack();
        ItemStack fire = new ItemBuilder(Material.POTION, 1).setData((short) 8259).toItemStack();

        for (int j = 0; j < 27; j++) {
            inventory.setItem(j, heal);
        }

        inventory.setItem(4, fire);

        inventory.setItem(12, speed);

        inventory.setItem(14, speed);

        inventory.setItem(22, speed);

        inventory.setItem(13, new ItemBuilder(Material.ENDER_PEARL,16).toItemStack());

        inventories.add(inventory);
        return RandomUtil.randomEle(inventories);
    }
}
