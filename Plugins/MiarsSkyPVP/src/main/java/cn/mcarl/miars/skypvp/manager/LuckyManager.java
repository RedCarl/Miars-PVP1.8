package cn.mcarl.miars.skypvp.manager;

import cn.mcarl.miars.skypvp.MiarsSkyPVP;
import cn.mcarl.miars.skypvp.entity.GamePlayer;
import cn.mcarl.miars.skypvp.entity.LuckyBlock;
import cn.mcarl.miars.skypvp.enums.LuckBlockType;
import cn.mcarl.miars.skypvp.utils.RotatingHead;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuckyManager {
    private static final LuckyManager instance = new LuckyManager();
    public static LuckyManager getInstance() {
        return instance;
    }

    Map<Location, Long> timeMap = new HashMap<>();
    Map<Location, LuckBlockType> typeMap = new HashMap<>();
    Map<Location, ArmorStand> standMap = new HashMap<>();
    List<Chunk> chunks = new ArrayList<>();
    public void init(){

        for (Location l:LuckyBlock.get(LuckBlockType.NORMAL).getLocationList()) {
            spawnLucky(l,LuckBlockType.NORMAL);
        }

        for (Location l:LuckyBlock.get(LuckBlockType.RARE).getLocationList()) {
            spawnLucky(l,LuckBlockType.RARE);
        }

        for (Location l:LuckyBlock.get(LuckBlockType.EPIC).getLocationList()) {
            spawnLucky(l,LuckBlockType.EPIC);
        }

        for (Location l:LuckyBlock.get(LuckBlockType.LEGENDARY).getLocationList()) {
            spawnLucky(l,LuckBlockType.LEGENDARY);
        }

        tick();
    }

    private void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Location l:standMap.keySet()) {
                    ArmorStand stand = standMap.get(l);
                    if (stand.isDead()){
                        // 判断是否到时间了
                        if (timeMap.containsKey(stand.getLocation())){
                            long s = ((System.currentTimeMillis() - timeMap.get(stand.getLocation()))/1000);
                            String name = stand.getCustomName().substring(stand.getCustomName().indexOf(".")+1);
                            LuckyBlock luckyBlock = new LuckyBlock(LuckBlockType.valueOf(name));
                            if (s>=luckyBlock.getRefresh()){
                                spawnLucky(l,luckyBlock.getType());
                                break;
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(MiarsSkyPVP.getInstance(),0,20);
    }

    public void useBlock(ArmorStand stand, Player player){
        timeMap.put(stand.getLocation(),System.currentTimeMillis());
        String name = stand.getCustomName().substring(stand.getCustomName().indexOf(".")+1);
        typeMap.put(stand.getLocation(),LuckBlockType.valueOf(name));

        stand.remove();

        ItemStack i = new ItemStack(LuckyBlock.get(LuckBlockType.valueOf(name)).randomItem());
        ItemMeta meta = i.getItemMeta();
        meta.setLore(new ArrayList<>());
        i.setItemMeta(meta);
        // 给予玩家物品
        Location location = stand.getLocation();
        location.add(0,1,0);
        player.getWorld().dropItemNaturally(location,i);

        player.playSound(player.getLocation(), Sound.ORB_PICKUP,1,1);

        GamePlayer.get(player).addLucky(1L);
    }

    public void spawnLucky(Location l,LuckBlockType type){
        if (!chunks.contains(l.getChunk())){
            chunks.add(l.getChunk());
        }
        l.getWorld().loadChunk(l.getChunk());
        ArmorStand stand = RotatingHead.getInstance().spawnLucky(l,type);
        timeMap.remove(l);
        standMap.put(l,stand);
    }

    public void clear(){
        for (ArmorStand stand:standMap.values()) {
            stand.remove();
        }
        typeMap.clear();
        timeMap.clear();
        standMap.clear();

        for (Entity entity:Bukkit.getWorld("world").getEntities()) {
            if (entity instanceof ArmorStand stand){
                stand.remove();
            }
        }
    }

    public List<Chunk> getChunks(){
        return chunks;
    }
}
