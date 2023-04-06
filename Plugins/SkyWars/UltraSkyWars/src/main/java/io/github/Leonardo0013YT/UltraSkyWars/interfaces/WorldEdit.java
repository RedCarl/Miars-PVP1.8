package io.github.Leonardo0013YT.UltraSkyWars.interfaces;

import io.github.Leonardo0013YT.UltraSkyWars.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.objects.GlassBlock;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.HashMap;

public interface WorldEdit {

    void paste(Location loc, String schematic, boolean air, CallBackAPI<Boolean> done);

    HashMap<Vector, GlassBlock> getBlocks(String schematic);

}
