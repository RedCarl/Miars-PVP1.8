package cn.mcarl.miars.practiceffa.manager;

import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.practiceffa.kits.NoDeBuff;
import cn.mcarl.miars.practiceffa.kits.Queue;
import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import cn.mcarl.miars.practiceffa.kits.FFAGame;
import cn.mcarl.miars.practiceffa.kits.Practice;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import cn.mcarl.miars.storage.storage.data.practice.FPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerInventoryManager {
    private static final PlayerInventoryManager instance = new PlayerInventoryManager();

    public static PlayerInventoryManager getInstance() {
        return instance;
    }

    private final Map<UUID, FInventory> datas = new HashMap<>();

    public void init(Player p){
        // 根据玩家当前状态给与对应的库存

        // 判断玩家是否在安全区外面
        if (!CombatManager.getInstance().isCombat(p)){
            if (!FFAUtil.isRange(p,PluginConfig.FFA_SITE.LOCATION.get(),PluginConfig.FFA_SITE.RADIUS.get())){
                // 判断当前的背包类型
                if (datas.get(p.getUniqueId()) == null || !datas.get(p.getUniqueId()).getType().equals(FKitType.FFAGAME)){
                    List<FKit> fKitList = MiarsStorage.getMySQLStorage().queryFKitDataList(p.getUniqueId(),FKitType.FFAGAME);

                    // 如果数据库中没有，就选默认的
                    if (fKitList.size()!=0){
                        datas.put(p.getUniqueId(), fKitList.get(0).getInventory());
                    }else {
                        datas.put(p.getUniqueId(), FFAGame.get());
                    }

                    autoEquip(p);
                }
            }else {
                if (datas.get(p.getUniqueId()) == null || !datas.get(p.getUniqueId()).getType().equals(FKitType.PRACTICE)){
                    if (PracticeQueueDataStorage.getInstance().isQueue(FPlayerDataStorage.getInstance().getFPlayer(p))){
                        datas.put(p.getUniqueId(), Queue.get());
                    }else {
                        datas.put(p.getUniqueId(), Practice.get());
                    }
                    autoEquip(p);
                }
            }
        }
    }

    public void setQueue(Player p){
        datas.put(p.getUniqueId(), Queue.get());
        autoEquip(p);
    }

    public void setPractice(Player p){
        datas.put(p.getUniqueId(), Practice.get());
        autoEquip(p);
    }

    public void remove(Player p){
        datas.remove(p.getUniqueId());
    }

    /**
     * 自动初始化该玩家装备
     * @param p
     */
    public void autoEquip(Player p){
        p.getInventory().clear();

        FInventory fInv = datas.get(p.getUniqueId());
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

    /**
     * 根据类型获取对应的库存
     * @param fKitType
     * @return
     */
    public FInventory getByFKitType(FKitType fKitType){
        switch (fKitType){
            case FFAGAME -> {
                return FFAGame.get();
            }
            case NO_DEBUFF -> {
                return NoDeBuff.get();
            }
        }

        return null;
    }

}
