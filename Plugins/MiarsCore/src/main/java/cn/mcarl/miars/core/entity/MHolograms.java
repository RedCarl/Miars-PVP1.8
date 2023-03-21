package cn.mcarl.miars.core.entity;

import cn.mcarl.miars.core.MiarsCore;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.PlaceholderSetting;
import me.filoghost.holographicdisplays.api.hologram.line.ItemHologramLine;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: carl0
 * @DATE: 2022/11/18 15:35
 */
public interface MHolograms {

    Map<String, Hologram> map = new HashMap<>();
    static Hologram createHologram(String name, Location location, List<Object> texts){
        HolographicDisplaysAPI holograms = HolographicDisplaysAPI.get(MiarsCore.getInstance());
        Hologram api = holograms.createHologram(location);
        api.setPlaceholderSetting(PlaceholderSetting.ENABLE_ALL);

        for (Object o:texts){
            if (o instanceof ItemStack){
                api.getLines().appendItem((ItemStack) o);
            }else {
                api.getLines().appendText((String) o);
            }
        }

        map.put(name,api);

        return api;
    }
    static Hologram createHologramString(String name, Location location, List<String> texts){
        HolographicDisplaysAPI holograms = HolographicDisplaysAPI.get(MiarsCore.getInstance());
        Hologram api = holograms.createHologram(location);
        api.setPlaceholderSetting(PlaceholderSetting.ENABLE_ALL);

        for (String o:texts){
            api.getLines().appendText(o);
        }


        map.put(name,api);

        return api;
    }

    static Hologram getHologramByName(String name){
        return map.get(name);
    }

    static void clear(){
        HolographicDisplaysAPI holograms = HolographicDisplaysAPI.get(MiarsCore.getInstance());
        holograms.getHolograms().clear();
    }

}
