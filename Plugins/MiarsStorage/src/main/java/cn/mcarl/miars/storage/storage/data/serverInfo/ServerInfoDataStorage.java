package cn.mcarl.miars.storage.storage.data.serverInfo;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.serverInfo.ServerInfo;
import cn.mcarl.miars.storage.entity.serverMenu.ServerMenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: carl0
 * @DATE: 2022/11/30 23:10
 */
public class ServerInfoDataStorage {
    private static final ServerInfoDataStorage instance = new ServerInfoDataStorage();
    public static ServerInfoDataStorage getInstance() {
        return instance;
    }

    private ServerInfo serverInfo = new ServerInfo();

    public ServerInfo getServerInfo(){
        if (serverInfo.getIp() == null){
            serverInfo = MiarsStorage.getMySQLStorage().queryServerInfo();
        }

        return serverInfo;
    }

    public void clear(){
        serverInfo = new ServerInfo();
    }

}
