package cn.mcarl.bungee.util.bungee;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.bungee.command.HubCommands;
import cn.mcarl.bungee.util.CustomSort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerHelper {

    public static boolean serverExists(String name) {
        return getServerInfo(name) != null;
    }

    public static ServerInfo getServerInfo(String name) {
        return getServers().get(name);
    }

    public static String getServerOnline(String name) {
        Map<String, ServerInfo> map = getServers();


        int i = 0;
        boolean is = false;
        for (String s:map.keySet()) {
            if (s.contains(name)){
                i+=map.get(s).getPlayers().size();
                is = true;
            }
        }

        if (!is){
            return null;
        }

        return String.valueOf(i);
    }

    public static void addServer(ServerInfo serverInfo) {
        if (serverExists(serverInfo.getName())) {
            return;
        }

        getServers().put(serverInfo.getName(), serverInfo);
    }

    public static void removeServer(String name) {
        if (!serverExists(name)) {
            return;
        }

        ServerInfo info = getServerInfo(name);

        for (ProxiedPlayer p : info.getPlayers()) {
            p.connect(getServers().get(p.getPendingConnection().getListener().getFallbackServer()));
        }

        getServers().remove(name);
    }

    public static Map<String, ServerInfo> getServers() {
        return ProxyServer.getInstance().getServers();
    }

    public static List<ServerInfo> getLobby() {
        List<ServerInfo> list = new ArrayList<>();

        Map<String,ServerInfo> map = ProxyServer.getInstance().getServers();
        for (ServerInfo info:map.values()) {
            if (info.getName().contains("lobby-")){
                list.add(info);
            }
        }

        return list;
    }

    public static void sendLobbyServer(ProxiedPlayer player){
        List<ServerData> list = new ArrayList<>();
        for (ServerInfo s:ServerHelper.getLobby()) {
            list.add(new ServerData(
                    s.getName(),
                    s.getPlayers().size()
            ));
        }

        if (list.size()==0){
            player.sendMessage(ColorParser.parse("&c暂无大厅服务器可供进入..."));
        }else {
            CustomSort.sort(list,"players",true);

            player.sendMessage(ColorParser.parse("&7正在连接到 &a"+list.get(0).getName()+" &7服务器..."));
            player.connect(ServerHelper.getServerInfo(list.get(0).getName()));
        }
    }


    public static String getLobbyServer(){
        List<ServerData> list = new ArrayList<>();
        for (ServerInfo s:ServerHelper.getLobby()) {
            list.add(new ServerData(
                    s.getName(),
                    s.getPlayers().size()
            ));
        }

        if (list.size()==0){
            return null;
        }else {
            CustomSort.sort(list,"players",true);

            return list.get(0).getName();
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ServerData{
        private String name;
        private Integer players;
    }



    public static String getServerDisplayName(String input){
        // 找到第一个 "-" 的位置
        int dashIndex = input.indexOf("-");

        if (dashIndex != -1) {
            // 提取 "-" 前面的内容
            input = input.substring(0, dashIndex);
        }

        switch (input){
            case "practice" -> {
                return "Practice";
            }
            case "skywars" -> {
                return "SkyWars";
            }
            case "bedwars" -> {
                return "BedWars";
            }
            case "uhc" -> {
                return "UHCGame";
            }
        }

        return input;
    }
}