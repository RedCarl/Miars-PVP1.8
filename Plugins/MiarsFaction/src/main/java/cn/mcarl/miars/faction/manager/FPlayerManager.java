package cn.mcarl.miars.faction.manager;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import cn.mcarl.miars.faction.MiarsFaction;
import cn.mcarl.miars.faction.entity.FPlayerEntity;

import java.io.File;
import java.util.*;

/**
 * @Author: carl0
 * @DATE: 2022/6/24 16:45
 */
public class FPlayerManager {

    private Set<FPlayerEntity> factionsPlayerEntities;
    private static final FPlayerManager instance = new FPlayerManager();

    public FPlayerManager() {
        factionsPlayerEntities = new HashSet<>(getAllFactions());
    }

    public static FPlayerManager getInstance() {
        return instance;
    }

    public List<FPlayerEntity> getAllFactions(){
        List<FPlayerEntity> list = new ArrayList<>();

        return list;
    }


    public boolean saveFactions(FPlayerEntity fPlayerEntity) {
        return true;
    }

    public FPlayerEntity getFactionsPlayerByUUID(UUID uuid) {
        return factionsPlayerEntities.stream().filter(e -> Objects.equals(e.getUuid(), uuid)).findFirst().orElse(null);
    }
}
