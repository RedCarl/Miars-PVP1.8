package io.github.Leonardo0013YT.UltraSkyWars;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.world.World;
import io.github.Leonardo0013YT.UltraSkyWars.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WorldEdit;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class WorldEditUtils_Old implements WorldEdit {

    private HashMap<String, Schematic> cache = new HashMap<>();
    private String path;
    private UltraSkyWars plugin;

    public WorldEditUtils_Old(UltraSkyWars plugin) {
        this.plugin = plugin;
        this.path = Bukkit.getWorldContainer() + "/plugins/WorldEdit/schematics";
    }

    @Override
    public void paste(Location loc, String schematic, boolean air, CallBackAPI<Boolean> done){
        String s = schematic.replaceAll(".schematic", "");
        Vector v = new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        World w = FaweAPI.getWorld(loc.getWorld().getName());
        if (!cache.containsKey(s)) {
            File file = new File(path, s + ".schematic");
            ClipboardFormat cf = ClipboardFormat.findByFile(file);
//            try {
//                if (cf != null) {
//                    cache.put(s, cf.load(file));
//                }
//            } catch (IOException ignored) {
//            }
        }
        if (cache.containsKey(s)){
            Schematic sh = cache.get(s);
            EditSession editSession = sh.paste(w, v, false, air, null);
            editSession.flushQueue();
            done.done(true);
        }
    }

}