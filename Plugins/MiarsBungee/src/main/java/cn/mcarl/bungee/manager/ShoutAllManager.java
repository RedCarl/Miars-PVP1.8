package cn.mcarl.bungee.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.bungee.entity.ShouAllData;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;

public class ShoutAllManager {
    private static final ShoutAllManager instance = new ShoutAllManager();
    public static ShoutAllManager getInstance() {
        return instance;
    }

    Map<String, ShouAllData> maps = new HashMap<>();

    public void putShoutAll(String token, ShouAllData data){
        maps.put(token, data);
    }

    public void click(ProxiedPlayer player, String token){
        if (maps.containsKey(token)){
            ShouAllData data = maps.get(token);
            if ((System.currentTimeMillis() - data.getTime())/1000 >= 60*5){
                player.sendMessage(ColorParser.parse("&cSorry, the request has timed out and cannot proceed."));
                maps.remove(token);
            }else {
                player.connect(data.getServer());
            }
        }else {
            player.sendMessage(ColorParser.parse("&cSorry, the request has timed out and cannot proceed."));
        }

    }

}
