package io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface SoulWellAnimation {
    void execute();

    void cancel(Player p);

    Inventory getInv();
}