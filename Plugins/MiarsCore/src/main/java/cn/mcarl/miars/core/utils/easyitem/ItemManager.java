package cn.mcarl.miars.core.utils.easyitem;

import cn.hutool.core.map.MapUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemManager implements Listener {
    private static final Map<String, AbstractItem> registeredItems = new HashMap<>();

    public static void register(AbstractItem item) {
        registeredItems.put(item.id, item);
    }
    public AbstractItem getAbstractItem(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        if (itemStack.getType() == Material.AIR) {
            return null;
        }
        NBTItem nbti = new NBTItem(itemStack);
        if (nbti.getString("id") == null || Objects.equals(nbti.getString("id"), "")){
            return null;
        }

        final String id = nbti.getString("id");
        return registeredItems.get(id);
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e){
        if (!e.hasItem()) {
            return;
        }
        final AbstractItem item = getAbstractItem(e.getItem());
        if (item == null) {
            return;
        }
        item.onInteract(e);
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent e) {

        AbstractItem abstractItem = getAbstractItem(e.getItemDrop().getItemStack());

        if (abstractItem!=null){
            abstractItem.onDropItem(e);
        }
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player player){

            if (e.isCancelled()) {
                return;
            }
            if (e.getClickedInventory() == null) {
                return;
            }
            if (e.getClickedInventory().getType() == InventoryType.CREATIVE) {
                return;
            }
            if (e.getInventory().getType() == InventoryType.CREATIVE) {
                return;
            }
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().getType() == Material.AIR) {
                return;
            }
            final AbstractItem item = getAbstractItem(e.getCurrentItem());
            if (item == null) {
                return;
            }
            item.onInventoryClick(e);
        }
    }

}