package cn.mcarl.bungee.manager;

import cn.mcarl.bungee.conf.PluginConfig;
import cn.mcarl.bungee.context.AddServerHttpHandler;
import cn.mcarl.bungee.context.InfoHttpHandler;
import cn.mcarl.bungee.context.RemoveServerHttpHandler;
import cn.mcarl.bungee.context.SendPlayerToServerHttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @Author: carl0
 * @DATE: 2022/11/13 20:51
 */
public class ServerManager{

    private static final ServerManager instance = new ServerManager();
    public static ServerManager getInstance() {
        return instance;
    }

    public void run() {
        HttpServer httpServer;
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PluginConfig.SERVER_INFO.PORT.get()), 0);
            httpServer.createContext("/info", new InfoHttpHandler());
            httpServer.createContext("/add/server", new AddServerHttpHandler());
            httpServer.createContext("/remove/server", new RemoveServerHttpHandler());
            httpServer.createContext("/send/player", new SendPlayerToServerHttpHandler());
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
