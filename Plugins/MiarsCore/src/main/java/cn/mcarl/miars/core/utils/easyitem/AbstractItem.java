package cn.mcarl.miars.core.utils.easyitem;

import lombok.Data;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public abstract class AbstractItem {
    protected String id;
    protected ItemStack item;

    protected void register() {
        ItemManager.getInstance().register(this);
    }

    public boolean give(Player player, int slot) {
        player.getInventory().setItem(slot, item);
        return true;
    }


    public void onInteract(PlayerInteractEvent e) {}
    public void onDropItem(PlayerDropItemEvent e) {}

    public void onInventoryClick(InventoryClickEvent e) {}

    public void onHeldItem(PlayerItemHeldEvent e) {}
}
