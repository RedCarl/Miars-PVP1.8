package io.github.Leonardo0013YT.UltraSkyWars.inventories.actions;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@Getter
public class InventoryAction {

    private final InventoryClickEvent inventoryClickEvent;
    private final Player player;

    public InventoryAction(InventoryClickEvent inventoryClickEvent, Player player) {
        this.inventoryClickEvent = inventoryClickEvent;
        this.player = player;
    }

}