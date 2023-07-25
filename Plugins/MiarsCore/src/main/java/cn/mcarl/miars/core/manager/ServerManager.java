package cn.mcarl.miars.core.manager;

import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.conf.PluginConfig;
import cn.mcarl.miars.storage.entity.MServerInfo;
import cn.mcarl.miars.storage.utils.HttpClientHelper;
import com.alibaba.fastjson.JSON;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: carl0
 * @DATE: 2022/11/11 23:34
 */
public class ServerManager {
    private static final ServerManager instance = new ServerManager();
    public static ServerManager getInstance() {
        return instance;
    }
    BukkitTask bukkitRunnable;

    Map<String,MServerInfo> serverInfoMap = new HashMap<>();
    Map<String,String> serverOnlineMap = new HashMap<>();
    Map<String,Long> serverLongMap = new HashMap<>();


    /**
     * 开启服务器,并创建Bungee服务器
     * @throws IOException
     */
    public void onStartServer() {
        String name = PluginConfig.SERVER_INFO.NAME.get();
        String url = PluginConfig.SERVER_INFO.URL.get();
        int port = MiarsCore.getInstance().getServer().getPort();
        try {
            HttpClientHelper.sendGet(url+"/add/server?name="+name+"&port="+port);
        } catch (IOException e) {
            MiarsCore.getInstance().log("启动代理模式失败,代理服务器可能没有正常运行...");
        }
        if (bukkitRunnable == null){
            tick();
        }
    }

    /**
     * 关闭服务器,并移除Bungee内的服务器
     * @throws IOException
     */
    public void onStopServer() {
        bukkitRunnable.cancel();
        String name = PluginConfig.SERVER_INFO.NAME.get();
        String url = PluginConfig.SERVER_INFO.URL.get();
        try {
            HttpClientHelper.sendGet(url+"/remove/server?name="+name);
        } catch (IOException e) {
            MiarsCore.getInstance().log("移除代理失败,代理服务器已经提前关闭...");
        }
    }


    /**
     * 获取服务器信息
     * @throws IOException
     */
    public MServerInfo getServerInfo(String name) {

        if (serverLongMap.containsKey(name)){
            if (!(System.currentTimeMillis() - serverLongMap.get(name) >= 3000)){
                return serverInfoMap.get(name);
            }
        }

        String state = "error";
        String url = PluginConfig.SERVER_INFO.URL.get();
        try {
            state = HttpClientHelper.sendGet(url+"/info?name="+name);
        } catch (IOException e){
            MiarsCore.getInstance().log("代理服务器链接已关闭,无法获取有效信息...");
        }
        if (state.contains("error")){
            return null;
        }

        MServerInfo mServerInfo = JSON.toJavaObject(JSON.parseObject(state),MServerInfo.class);

        serverInfoMap.put(name, mServerInfo);
        serverLongMap.put(name, System.currentTimeMillis());

        return mServerInfo;
    }


    /**
     * 获取服务器人数
     * @throws IOException
     */
    public String getServerOnline(String name) {
        name = name+"-";

        if (serverLongMap.containsKey(name)){
            if (!(System.currentTimeMillis() - serverLongMap.get(name) >= 3000)){
                return serverOnlineMap.get(name);
            }
        }

        String state = "error";
        String url = PluginConfig.SERVER_INFO.URL.get();
        try {
            state = HttpClientHelper.sendGet(url+"/online?name="+name);
        } catch (IOException e){
            MiarsCore.getInstance().log("代理服务器链接已关闭,无法获取有效信息...");
        }
        if (state.contains("error")){
            return null;
        }

        serverOnlineMap.put(name, state);
        serverLongMap.put(name, System.currentTimeMillis());

        return state;
    }

    /**
     * 将玩家传送至某个服务器
     * @param name 玩家
     * @param server 服务器
     * @return
     */
    public boolean sendPlayerToServer(String name,String server) {
        String state = "error";
        String url = PluginConfig.SERVER_INFO.URL.get();
        try {
            state = HttpClientHelper.sendGet(url+"/send/player?user="+name+"&server="+server);
        } catch (IOException e){
            MiarsCore.getInstance().log("代理服务器链接已关闭,无法获取有效信息...");
        }
        return !state.contains("error");
    }

    /**
     * 循环判断Bungee中是否正常的存在该服务器的数据
     */
    public void tick(){
        bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (getServerInfo(PluginConfig.SERVER_INFO.NAME.get())==null){
                    onStartServer();
                }
            }
        }.runTaskTimerAsynchronously(MiarsCore.getInstance(),0,600);
    }
}
