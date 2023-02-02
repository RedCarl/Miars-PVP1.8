package cn.mcarl.bungee.context;

import cn.mcarl.bungee.util.GetUrlParameter;
import cn.mcarl.bungee.util.bungee.ServerHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Author: carl0
 * @DATE: 2023/2/2 20:24
 */
public class SendPlayerToServerHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String url = exchange.getRequestURI().toString();
        String userName = GetUrlParameter.getOneParameter(url,"user");
        String serverName = GetUrlParameter.getOneParameter(url,"server");

        String response;

        /*
        增加服务器
         */
        if (userName!=null && serverName!=null) {
            ServerInfo server = ServerHelper.getServerInfo(serverName);
            if (server!=null){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(userName);
                if (player!=null){
                    player.connect(server);
                    response = "ok";
                }else {
                    response = "玩家 "+userName+" 不存在";
                }

            }else {
                response = "服务器 "+serverName+" 不存在";
            }
        }else {
            response = "error";
        }

        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
