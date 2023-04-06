package cn.mcarl.miars.faction.manager;


import cn.mcarl.miars.faction.entity.BedHomeEntity;
import cn.mcarl.miars.faction.entity.FPlayerEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @Author: carl0
 * @DATE: 2022/7/17 19:42
 */
public class BedHomeManager {

    private final HashMap<UUID,List<BedHomeEntity>> homes = new HashMap<>();
    private static final BedHomeManager instance = new BedHomeManager();

    public static BedHomeManager getInstance() {
        return instance;
    }

    public List<BedHomeEntity> getHomes(Player player){
        FPlayerEntity fPlayer = FPlayerManager.getInstance().getFactionsPlayerByUUID(player.getUniqueId());
        homes.put(player.getUniqueId(),fPlayer.getHomes());
        return homes.get(player.getUniqueId());
    }

    public boolean useHome(Player player,BedHomeEntity bedHomeEntity){
        FPlayerEntity fPlayer = FPlayerManager.getInstance().getFactionsPlayerByUUID(player.getUniqueId());
        for (BedHomeEntity b:fPlayer.getHomes()) {
            if (bedHomeEntity.getHeadLocation() == bedHomeEntity.getHeadLocation() && bedHomeEntity.getTailLocation() == bedHomeEntity.getTailLocation()){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }


}
