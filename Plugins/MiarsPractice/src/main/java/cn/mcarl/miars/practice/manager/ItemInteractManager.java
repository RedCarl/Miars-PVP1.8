package cn.mcarl.miars.practice.manager;

import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.storage.data.practice.FKitDataStorage;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 21:06
 */
public class ItemInteractManager {
    private static final ItemInteractManager instance = new ItemInteractManager();
    public static ItemInteractManager getInstance() {
        return instance;
    }

    public void init(ItemStack i, Player p){

        NBTItem nbtItem = new NBTItem(i);

        if (nbtItem.getString("practice_kit")!=null && !nbtItem.getString("practice_kit").equals("")){
            FKit fKit = FKitDataStorage.getInstance().getFKitDataById(Integer.valueOf(nbtItem.getString("practice_kit")));
            PlayerInventoryManager.getInstance().autoEquip(p,fKit.getInventory());
        }
    }
}
