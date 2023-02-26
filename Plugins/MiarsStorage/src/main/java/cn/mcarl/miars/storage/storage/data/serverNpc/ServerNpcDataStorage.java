package cn.mcarl.miars.storage.storage.data.serverNpc;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.serverMenu.ServerMenuItem;
import cn.mcarl.miars.storage.entity.serverNpc.ServerNPC;
import cn.mcarl.miars.storage.entity.serverNpc.ServerNPC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: carl0
 * @DATE: 2022/11/30 23:10
 */
public class ServerNpcDataStorage {
    private static final ServerNpcDataStorage instance = new ServerNpcDataStorage();
    public static ServerNpcDataStorage getInstance() {
        return instance;
    }

    private final Map<String, List<ServerNPC>> dataMap = new HashMap<>();

    public List<ServerNPC> getServerNPC(String serverName){
        if (dataMap.containsKey(serverName)){
            return dataMap.get(serverName);
        }
        List<ServerNPC> data = MiarsStorage.getMySQLStorage().queryServerNpc(serverName);

        if (data.size()==0){
            return new ArrayList<>();
        }

        dataMap.put(data.get(0).getServer(),data);
        return dataMap.get(serverName);
    }

    public void clear(){
        dataMap.clear();
    }

}
