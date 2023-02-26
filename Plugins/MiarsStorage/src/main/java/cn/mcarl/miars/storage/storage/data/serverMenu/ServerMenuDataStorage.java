package cn.mcarl.miars.storage.storage.data.serverMenu;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.serverMenu.ServerMenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: carl0
 * @DATE: 2022/11/30 23:10
 */
public class ServerMenuDataStorage {
    private static final ServerMenuDataStorage instance = new ServerMenuDataStorage();
    public static ServerMenuDataStorage getInstance() {
        return instance;
    }

    private final Map<String, List<ServerMenuItem>> dataMap = new HashMap<>();

    public List<ServerMenuItem> getServerMenuItem(String guiName){
        if (dataMap.containsKey(guiName)){
            return dataMap.get(guiName);
        }
        List<ServerMenuItem> data = MiarsStorage.getMySQLStorage().queryServerMenu(guiName);

        if (data.size()==0){
            return new ArrayList<>();
        }

        dataMap.put(data.get(0).getGuiName(),data);

        return dataMap.get(data.get(0).getGuiName());
    }

    public void clear(){
        dataMap.clear();
    }

}
