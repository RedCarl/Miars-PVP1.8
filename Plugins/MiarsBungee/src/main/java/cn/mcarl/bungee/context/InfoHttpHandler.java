package cn.mcarl.bungee.context;

import cn.mcarl.bungee.entity.MPlayer;
import cn.mcarl.bungee.entity.MServerInfo;
import cn.mcarl.bungee.util.GetUrlParameter;
import cn.mcarl.bungee.util.bungee.ServerHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: carl0
 * @DATE: 2022/11/13 23:23
 */
public class InfoHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String url = exchange.getRequestURI().toString();
        String name = GetUrlParameter.getOneParameter(url,"name");

        String response;

        ServerInfo s = ServerHelper.getServerInfo(name);

        /*
        如果有服务器名称,就返回该服务器的信息
         */
        if (s!=null) {
            List<MPlayer> mPlayerList = new ArrayList<>();

            for (ProxiedPlayer p:s.getPlayers()){
                mPlayerList.add(new MPlayer(p.getDisplayName(),p.getName(),p.getUniqueId().toString(),p.getPing()));
            }

            response = new JSONObject(new MServerInfo(s.getName(),s.getMotd(),mPlayerList)).toString();
        }else {
            response = "error";
        }

        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
