package io.github.Leonardo0013YT.UltraSkyWars.interfaces;

import io.github.Leonardo0013YT.UltraSkyWars.calls.CallBackAPI;
import org.bukkit.Location;

public interface WorldEdit {

    void paste(Location loc, String schematic, boolean air, CallBackAPI<Boolean> done);

}
