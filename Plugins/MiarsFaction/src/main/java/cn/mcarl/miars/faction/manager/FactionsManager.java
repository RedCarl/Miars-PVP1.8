package cn.mcarl.miars.faction.manager;

import cn.mcarl.miars.faction.entity.BannerEntity;
import cn.mcarl.miars.faction.entity.ChunkData;
import cn.mcarl.miars.faction.entity.FactionsEntity;
import cn.mcarl.miars.storage.entity.faction.enums.Group;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @Author: carl0
 * @DATE: 2022/6/24 14:42
 */
public class FactionsManager {

    private Set<FactionsEntity> factionsEntities;
    private static final FactionsManager instance = new FactionsManager();

    public FactionsManager() {
        factionsEntities = new HashSet<>(getAllFactions());
    }

    public static FactionsManager getInstance() {
        return instance;
    }


    public Set<FactionsEntity> getFactionsEntities(){
        return factionsEntities;
    }

    private List<FactionsEntity> getAllFactions() {
        List<FactionsEntity> list = new ArrayList<>();
        
        return list;
    }

    public boolean saveFactions(FactionsEntity factionsEntity) {
        return true;
    }

    public void deleteFactions(FactionsEntity factionsEntity) {

    }


    public FactionsEntity getFactionsByUUID(UUID uuid) {
        return factionsEntities.stream().filter(e -> Objects.equals(e.getUuid(), uuid)).findFirst().orElse(null);
    }

    public FactionsEntity getFactionsByPlayer(UUID uuid) {
        return factionsEntities.stream().filter(e -> e.getPlayerGroups().containsKey(uuid)).findFirst().orElse(null);
    }

    public FactionsEntity getFactionsByName(String name) {
        return factionsEntities.stream().filter(e -> Objects.equals(e.getName(), name)).findFirst().orElse(null);
    }

    public FactionsEntity getFactionsByBlock(Location location) {
        return factionsEntities.stream().filter(e -> Objects.equals(e.getTerritoryCabinet(), location)).findFirst().orElse(null);
    }


    public int getFactionsPower(UUID uuid) {
        int power = 0;
        for (UUID p : getFactionsByUUID(uuid).getPlayerGroups().keySet()) {
            power += FPlayerManager.getInstance().getFactionsPlayerByUUID(p).getPower();
        }
        return power;
    }

    public int getFactionsPowerMax(UUID uuid) {
        int power = 0;
        for (UUID p : getFactionsByUUID(uuid).getPlayerGroups().keySet()) {
            power += FPlayerManager.getInstance().getFactionsPlayerByUUID(p).getPowerMax();
        }
        return power;
    }


    /**
     * 创建派系
     *
     * @param player  玩家
     * @param name    派系名称
     * @param present 派系简介
     * @return 是否成功
     */
    public boolean createFactions(Player player, String name, String present, Location block) {
        if (getFactionsByPlayer(player.getUniqueId()) == null) {
            HashMap<UUID, Group> map = new HashMap<>();
            map.put(player.getUniqueId(), Group.OWNER);
            FactionsEntity factions = new FactionsEntity(UUID.randomUUID(), name, new BannerEntity(DyeColor.WHITE, new ArrayList<>(0)), present, map, new HashSet<>(), 0, 0, block);
            return FactionsManager.getInstance().saveFactions(factions);
        }
        return false;
    }


    /**
     * 查询所在区块是否可以占领
     *
     * @return 是否成功
     */
    public boolean checkFactionsChunk(Player player, ChunkData chunkData) {
        FactionsEntity f = getFactionsChunk(chunkData);

        if (f == null) {
            //这个领土没有被其它派系占领
            if (getFactionsRound(player.getWorld(), chunkData) != null) {
                //周围有其它派系，请远离后重试。
                //player.sendMessage(new TranslateMessage("factions.check.chunk.msg.error.a"));
                return false;
            } else {
                //成功占领该领土
                return true;
            }
        } else if (getFactionsByPlayer(player.getUniqueId()) != null && getFactionsByPlayer(player.getUniqueId()).getUuid() == f.getUuid()) {
            //很抱歉这个领土已经被您占领了
            //player.sendMessage(new TranslateMessage("factions.check.chunk.msg.error.b"));
            return false;
        } else if (getFactionsPower(f) <= 0) {
            //这个敌人的领土可以被占领
            return true;
        } else {
            //很抱歉，这个领土已被其它派系占领
            //player.sendMessage(new TranslateMessage("factions.check.chunk.msg.error.c"));
            return false;
        }

    }

    /**
     * 占领区块
     *
     * @param factionsEntity 派系
     * @param chunkData      区块
     * @return 是否
     */
    public boolean addFactionsChunk(FactionsEntity factionsEntity, ChunkData chunkData) {
        factionsEntity.getChunks().add(chunkData);
        saveFactions(factionsEntity);
        return true;
    }


    /**
     * 获取玩家附近有没有其它派系
     *
     * @param chunkData 区块
     * @return 其它派系
     */
    public FactionsEntity getFactionsRound(World world, ChunkData chunkData) {

        int halfWidth = 6 / 2;
        // Use player's value for height
        int halfHeight = 6 / 2;

        Location topLeft = getRelative(world, chunkData, -halfWidth, -halfHeight);

        int width = halfWidth * 2 + 1;
        int height = halfHeight * 2 + 1;

        // For each row
        for (int dz = 0; dz < height; dz++) {
            for (int dx = (dz < 3 ? 6 : 3); dx < width; dx++) {
                if (dx == halfWidth && dz == halfHeight) {
                    break;
                } else {
                    ChunkData isChunk = new ChunkData((int) (dx + topLeft.getX()), (int) (dz + topLeft.getZ()),0);
                    FactionsEntity isFactions = getFactionsChunk(isChunk);
                    FactionsEntity factions = getFactionsChunk(chunkData);
                    if (isFactions != null && factions != null && isFactions.getUuid() != factions.getUuid()) {
                        return isFactions;
                    }
                }
            }

        }

        return null;
    }

    public Location getRelative(World world, ChunkData chunkData, int dx, int dz) {
        return new Location(world, chunkData.getX() + dx, 0, chunkData.getZ() + dz);
    }

    /**
     * 获取这个区块的派系信息
     *
     * @param chunk 区块
     * @return 派系实体
     */
    public FactionsEntity getFactionsChunk(ChunkData chunk) {
        for (FactionsEntity f : factionsEntities) {
            if (f.getChunks().contains(chunk)) {
                return f;
            }
        }
        return null;
    }

    /**
     * 获取派系最大能量
     *
     * @param f 派系
     * @return 最大能量
     */
    public int getFactionsPowerMax(FactionsEntity f) {
        return f.getPlayerGroups().size() * 10;
    }

    /**
     * 获取派系当前能量
     *
     * @param f 派系
     * @return 当前能量
     */
    public int getFactionsPower(FactionsEntity f) {
        int power = 0;

        for (UUID uuid : f.getPlayerGroups().keySet()) {
            power += FPlayerManager.getInstance().getFactionsPlayerByUUID(uuid).getPower();
        }
        return power;
    }

    public ItemStack setFactionsNameNBT(ItemStack i, String name, Player player) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(i);
        NBTTagCompound tag = nmsItem.getTag().getCompound("PanshiMC");
        tag.setString("name", name);
        ItemStack itemStack = CraftItemStack.asBukkitCopy(nmsItem);
        itemStack.setItemMeta(updateFactionsToolLores(itemStack, player));
        return itemStack;
    }

    public ItemStack setFactionsPresentNBT(ItemStack i, String present, Player player) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(i);
        NBTTagCompound tag = nmsItem.getTag().getCompound("PanshiMC");
        tag.setString("present", present);
        ItemStack itemStack = CraftItemStack.asBukkitCopy(nmsItem);
        itemStack.setItemMeta(updateFactionsToolLores(itemStack, player));
        return itemStack;
    }

    public ItemMeta updateFactionsToolLores(ItemStack i, Player player) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(i);
        NBTTagCompound tag = nmsItem.getTag().getCompound("PanshiMC");

        ItemMeta itemMeta = i.getItemMeta();

        itemMeta.getLore().clear();
        itemMeta.setLore(Collections.singletonList("factions.tool.lore>" + (tag.getString("name").equals("") ? "暂无请设置" : tag.getString("name")) + "," + (tag.getString("present").equals("") ? "暂无请设置" : tag.getString("present"))));

        return itemMeta;
    }


    /**
     * 是否能对派系进行一系列的操作
     * @param fe 派系
     * @param p 请求的玩家
     * @return 是否
     */
    public boolean isOperate(FactionsEntity fe,Player p){
        //是否是该派系的玩家
        if (fe.getPlayerGroups().containsKey(p.getUniqueId())){
            return true;
        }else if (getFactionsPower(fe)<=0){
            return true;
        }else if (fe.getMoneyBank()<getFactionsCost(fe)){
            return true;
        }
        return false;
    }


    /**
     * 获取该派系每分钟所需的费用
     * @param f 派系
     * @return 费用
     */
    public long getFactionsCost(FactionsEntity f){
        return (f.getPlayerGroups().size()*2L)+(f.getChunks().size()* 4L);
    }

    public boolean costFactions(FactionsEntity f){
        if (f.getMoneyBank()>=getFactionsCost(f)){
            f.setMoneyBank(f.getMoneyBank()-getFactionsCost(f));
            saveFactions(f);
        }else {
            for (UUID u : f.getPlayerGroups().keySet()) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(u);
                if (offlinePlayer.isOnline()){
                    Player player = Bukkit.getPlayer(u);
                    //player.sendMessage(new TranslateMessage("factions.bank.warn"));
                    return false;
                }
            }
        }
        return true;
    }
}
