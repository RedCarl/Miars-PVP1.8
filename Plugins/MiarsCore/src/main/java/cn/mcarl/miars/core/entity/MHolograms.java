package cn.mcarl.miars.core.entity;

import cn.mcarl.miars.core.MiarsCore;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
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

    Map<String,Hologram> map = new HashMap<>();
    static Hologram createHologram(String name, Location location, List<Object> texts){
        Hologram api = HologramsAPI.createHologram(MiarsCore.getInstance(),location);

        for (Object o:texts){
            if (o instanceof ItemStack){
                api.appendItemLine((ItemStack) o);
            }else {
                api.appendTextLine((String) o);
            }
        }

        map.put(name,api);

        return api;
    }

    static Hologram getHologramByName(String name){
        return map.get(name);
    }

    static void clear(){
        HologramsAPI.getHolograms(MiarsCore.getInstance()).clear();
    }

}
