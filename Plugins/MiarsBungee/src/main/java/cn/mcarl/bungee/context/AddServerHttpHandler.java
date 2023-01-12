package cn.mcarl.bungee.context;

import cn.mcarl.bungee.util.GetUrlParameter;
import cn.mcarl.bungee.util.bungee.ServerHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @Author: carl0
 * @DATE: 2022/11/13 23:23
 */
public class AddServerHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String url = exchange.getRequestURI().toString();
        String name = GetUrlParameter.getOneParameter(url,"name");
        int port = Integer.parseInt(GetUrlParameter.getOneParameter(url,"port"));

        String response;

        /*
        增加服务器
         */
        if (name!=null) {
            if (ServerHelper.getServerInfo(name)==null){
                ServerInfo info = ProxyServer.getInstance().constructServerInfo(name, new InetSocketAddress(port), "", false);
                ServerHelper.addServer(info);
                response = "ok";
            }else {
                response = "服务器 "+name+" 已经存在";
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
