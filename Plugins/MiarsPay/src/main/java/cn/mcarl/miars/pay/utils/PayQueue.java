
package cn.mcarl.miars.pay.utils;

import java.util.Objects;
import java.util.ArrayList;
import java.io.IOException;

import cn.mcarl.miars.pay.MiarsPay;
import java.io.File;
import com.google.gson.JsonObject;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.LinkedList;
import com.google.gson.Gson;

public class PayQueue
{
    private static Gson gson;
    private static PayQueue instance;
    private LinkedList<InnerOrder> recentOrder;
    private YamlConfiguration queueConfig;
    private List<JsonObject> payQueue;
    private File queueFile;
    
    public static PayQueue getInstance() {
        if (PayQueue.instance != null) {
            return PayQueue.instance;
        }
        return PayQueue.instance = new PayQueue();
    }
    
    private PayQueue() {
        this.recentOrder = new LinkedList<InnerOrder>();
        this.queueFile = new File(MiarsPay.getInstance().getDataFolder(), "pay.queue");
        if (!this.queueFile.exists()) {
            try {
                this.queueFile.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.queueConfig = YamlConfiguration.loadConfiguration(this.queueFile);
        this.payQueue = this.getStringJsonObject(this.queueConfig.getStringList("queue"));
    }
    
    private List<String> getJsonObjectString() {
        final List<String> jsonStrings = new ArrayList<String>();
        for (final JsonObject json : this.payQueue) {
            jsonStrings.add(json.toString());
        }
        return jsonStrings;
    }
    
    private List<JsonObject> getStringJsonObject(final List<String> strings) {
        final List<JsonObject> jsons = new ArrayList<JsonObject>();
        for (final String jsonString : strings) {
            jsons.add((JsonObject)PayQueue.gson.fromJson(jsonString, (Class)JsonObject.class));
        }
        return jsons;
    }
    
    public List<JsonObject> getPayQueue() {
        return this.payQueue;
    }
    
    public void addRecentOrder(final String order) {
        final InnerOrder innerOrder = new InnerOrder(order, Long.valueOf(System.currentTimeMillis()));
        this.recentOrder.remove(innerOrder);
        if (this.recentOrder.size() >= 128) {
            this.recentOrder.remove();
        }
        this.recentOrder.add(innerOrder);
    }
    
    public boolean isRecentOrder(final String order) {
        for (final InnerOrder innerOrder : this.recentOrder) {
            if (innerOrder.order.equals(order)) {
                return System.currentTimeMillis() < innerOrder.time + 1800000L;
            }
        }
        return false;
    }
    
    public boolean addQueue(final JsonObject json) {
        this.payQueue.add(json);
        return this.save();
    }
    
    public boolean delQueue(final JsonObject json) {
        return !this.payQueue.remove(json) || this.save();
    }
    
    private boolean save() {
        this.queueConfig.set("queue", (Object)this.getJsonObjectString());
        try {
            this.queueConfig.save(this.queueFile);
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    static {
        PayQueue.gson = new Gson();
    }
    
    private static class InnerOrder
    {
        private String order;
        private Long time;
        
        private InnerOrder(final String order, final Long time) {
            this.order = order;
            this.time = time;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final InnerOrder that = (InnerOrder)o;
            return Objects.equals(this.order, that.order);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.order);
        }
    }
}
