package io.github.Leonardo0013YT.UltraSkyWars.cosmetics;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Parting extends Cosmetic {

    private ArrayList<String> lines;
    private ItemStack icon;

    public Parting(UltraSkyWars plugin, String s) {
        super(plugin.getParting(), s, "parting");
        this.lines = (ArrayList<String>) plugin.getParting().getList(s + ".message");
        this.icon = Utils.getIcon(plugin.getParting(), s);
        plugin.getCos().setLastPage("Parting", page);
    }

    public void execute(Player d) {
        Location l = d.getLocation().add(0, 1, 0);
        UltraSkyWars.get().getAdm().createHologram(l, lines);
        new BukkitRunnable() {
            @Override
            public void run() {
                UltraSkyWars.get().getAdm().deleteHologram(l);
            }
        }.runTaskLater(UltraSkyWars.get(), 20 * 5);
    }

}