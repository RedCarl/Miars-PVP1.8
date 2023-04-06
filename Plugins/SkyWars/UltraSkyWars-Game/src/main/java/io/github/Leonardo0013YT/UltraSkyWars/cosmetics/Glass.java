package io.github.Leonardo0013YT.UltraSkyWars.cosmetics;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Purchasable;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Glass extends Cosmetic implements Purchasable {

    private String schematic, clear, schematicTeam, clearTeam;
    private ItemStack item;
    private boolean isBuy, needPermToBuy;

    public Glass(UltraSkyWars plugin, String path) {
        super(plugin.getGlass(), path, "glass");
        this.item = Utils.getIcon(plugin.getGlass(), path);
        this.schematic = plugin.getGlass().get(path + ".schematic");
        this.clear = plugin.getGlass().get(path + ".clear");
        this.schematicTeam = plugin.getGlass().getOrDefault(path + ".schematicTeam", schematic);
        this.clearTeam = plugin.getGlass().getOrDefault(path + ".clearTeam", clear);
        this.permission = plugin.getGlass().get(path + ".permission");
        this.isBuy = plugin.getGlass().getBoolean(path + ".isBuy");
        this.autoGivePermission = plugin.getGlass().getOrDefault(path + ".autoGivePermission", "ultraskywars.glass.autogive." + name);
        this.needPermToBuy = plugin.getGlass().getBooleanOrDefault(path + ".needPermToBuy", false);
        plugin.getCos().setLastPage("Glass", page);
    }

    public void createCage(Location loc, boolean team, CallBackAPI<Boolean> done) {
        if (!loc.getChunk().isLoaded()) {
            loc.getChunk().load();
        }
        if (team) {
            UltraSkyWars.get().getWc().getEdit().paste(loc, schematicTeam, false, done);
        } else {
            UltraSkyWars.get().getWc().getEdit().paste(loc, schematic, false, done);
        }
    }

    public void deleteCage(Location loc, boolean team, CallBackAPI<Boolean> done) {
        if (loc == null) {
            return;
        }
        if (!loc.getChunk().isLoaded()) {
            loc.getChunk().load();
        }
        if (team) {
            UltraSkyWars.get().getWc().getEdit().paste(loc, clearTeam, true, done);
        } else {
            UltraSkyWars.get().getWc().getEdit().paste(loc, clear, true, done);
        }
    }

}