package cn.mcarl.miars.core.utils.easyitem;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;


public abstract class AbstractItem {

    protected ItemStack item;
    protected String id;

    protected void register() {
        init();
        ItemManager.register(this);
    }

    public abstract void init();

    public boolean give(Player player, int slot) {
        player.getInventory().setItem(slot, bukkit(item));
        return true;
    }

    public ItemStack bukkit(ItemStack item){
        NBTItem nbti = new NBTItem(item);
        nbti.setString("id",id);
        return nbti.getItem();
    }

    public void onInteract(PlayerInteractEvent e) {}
    public void onDropItem(PlayerDropItemEvent e) {}

    public void onInventoryClick(InventoryClickEvent e) {}

    public void onHeldItem(PlayerItemHeldEvent e) {}
}
