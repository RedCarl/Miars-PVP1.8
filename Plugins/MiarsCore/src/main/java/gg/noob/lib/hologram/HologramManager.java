package gg.noob.lib.hologram;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class HologramManager {

    @Getter
    private List<Hologram> holograms = new ArrayList<>();

    public void registerHologram(Hologram hologram) {
        holograms.add(hologram);
    }

}
