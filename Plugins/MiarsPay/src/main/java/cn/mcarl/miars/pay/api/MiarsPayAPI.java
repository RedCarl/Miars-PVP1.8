package cn.mcarl.miars.pay.api;

import cn.mcarl.miars.pay.enums.PaywayType;
import cn.mcarl.miars.pay.utils.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiarsPayAPI
{
    private final Gson gson;
    private final String server;
    private final String apiUrl;
    private final String apiKey;
    
    public MiarsPayAPI(final String server, final String apiUrl, final String apiKey) {
        this.gson = new Gson();
        this.server = server;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }
    
    public String getServer() {
        return this.server;
    }
    
    public String getApiUrl() {
        return this.apiUrl;
    }
    
    public String getApiKey() {
        return this.apiKey;
    }
    
    public String createOrder(final String name, final PaywayType paywayType, final Double money) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("server", this.server);
        map.put("name", name);
        map.put("money", money.toString());
        map.put("payway", paywayType.name());
        final String body = NetworkUtil.get(this.apiUrl + "order/create", map);
        if (body != null) {
            try {
                final JsonObject jsonObject = (JsonObject)this.gson.fromJson(body, (Class)JsonObject.class);
                final int code = jsonObject.get("code").getAsInt();
                if (code == 200) {
                    final JsonObject data = jsonObject.get("data").getAsJsonObject();
                    return String.valueOf(data.get("orderId").getAsLong());
                }
            }
            catch (JsonSyntaxException | NullPointerException ignored) {}
        }
        return null;
    }
    
    public BufferedImage createQrCode(final String order) {
        final String strUrl = this.apiUrl + "order/qrcode/" + order;
        try {
            return ImageIO.read(new URL(strUrl));
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    public List<JsonObject> getUnshipOrder() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("server", this.server);
        map.put("key", this.apiKey);
        final String body = NetworkUtil.get(this.apiUrl + "api/get_unship_order", map);
        if (body != null) {
            try {
                final JsonObject jsonObject = (JsonObject)this.gson.fromJson(body, (Class)JsonObject.class);
                final int code = jsonObject.get("code").getAsInt();
                if (code == 200) {
                    final ArrayList<JsonObject> jsonObjects = new ArrayList<JsonObject>();
                    final JsonArray data = jsonObject.get("data").getAsJsonArray();
                    data.forEach(e -> jsonObjects.add(e.getAsJsonObject()));
                    return jsonObjects;
                }
            } catch (JsonSyntaxException | NullPointerException ignored) {}
        }
        return new ArrayList<>(0);
    }
    
    public boolean markShip(final String order) {
        final Map<String, Object> map = new HashMap<>();
        map.put("server", this.server);
        map.put("key", this.apiKey);
        map.put("order", order);
        final String body = NetworkUtil.get(this.apiUrl + "api/mark_ship", map);
        if (body != null) {
            try {
                final JsonObject jsonObject = (JsonObject)this.gson.fromJson(body, (Class)JsonObject.class);
                final int code = jsonObject.get("code").getAsInt();
                if (code == 200) {
                    return true;
                }
            } catch (JsonSyntaxException | NullPointerException ignored) {}
        }
        return false;
    }
    
    public JsonObject getServerIncome() {
        final Map<String, Object> map = new HashMap<>();
        map.put("server", this.server);
        map.put("key", this.apiKey);
        final String body = NetworkUtil.get(this.apiUrl + "api/get_server_income", map);
        if (body != null) {
            try {
                final JsonObject jsonObject = (JsonObject)this.gson.fromJson(body, (Class)JsonObject.class);
                final int code = jsonObject.get("code").getAsInt();
                if (code == 200) {
                    return jsonObject.get("data").getAsJsonObject();
                }
            }
            catch (JsonSyntaxException | NullPointerException ignored) {}
        }
        return null;
    }
    
    public JsonObject getOrderVolume() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("server", this.server);
        map.put("key", this.apiKey);
        final String body = NetworkUtil.get(this.apiUrl + "api/get_server_order_count", map);
        if (body != null) {
            try {
                final JsonObject jsonObject = (JsonObject)this.gson.fromJson(body, (Class)JsonObject.class);
                final int code = jsonObject.get("code").getAsInt();
                if (code == 200) {
                    return jsonObject.get("data").getAsJsonObject();
                }
            }
            catch (JsonSyntaxException | NullPointerException ignored) {}
        }
        return null;
    }
}