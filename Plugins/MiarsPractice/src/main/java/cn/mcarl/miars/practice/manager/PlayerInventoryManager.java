package cn.mcarl.miars.practice.manager;

import cn.mcarl.miars.practice.MiarsPractice;
import cn.mcarl.miars.practice.item.KitBook;
import cn.mcarl.miars.storage.utils.CustomSort;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.practice.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.storage.data.practice.FKitDataStorage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class PlayerInventoryManager {
    private static final PlayerInventoryManager instance = new PlayerInventoryManager();

    public static PlayerInventoryManager getInstance() {
        return instance;
    }

    public void init(Player p){
        List<FKit> list = new ArrayList<>(FKitDataStorage.getInstance().getFKitData(p.getUniqueId(), MiarsPractice.getModeType()));
        CustomSort.sort(list,"power",false);

        p.getInventory().setItem(39,null);
        p.getInventory().setItem(38,null);
        p.getInventory().setItem(37,null);
        p.getInventory().setItem(36,null);

        p.getInventory().clear();

        for (int i = 0; i <= 8; i++) {
            if (list.size()>=i+1){
                FKit fKit = list.get(i);
                new KitBook(fKit).give(p,i);
            }else {
                break;
            }
        }
    }


    public void autoEquip(Player p,FInventory fInv){
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
