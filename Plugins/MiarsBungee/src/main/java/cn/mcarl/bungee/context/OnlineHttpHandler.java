package cn.mcarl.bungee.context;

import cn.mcarl.bungee.util.GetUrlParameter;
import cn.mcarl.bungee.util.bungee.ServerHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @Author: carl0
 * @DATE: 2022/11/13 23:23
 */
public class OnlineHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String url = exchange.getRequestURI().toString();
        String name = GetUrlParameter.getOneParameter(url,"name");

        String response;

        String s = ServerHelper.getServerOnline(name);

        response = Objects.requireNonNullElse(s, "error");

        exchange.sendResponseHeaders(200, 0);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
