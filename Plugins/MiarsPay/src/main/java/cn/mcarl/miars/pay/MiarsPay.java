package cn.mcarl.miars.pay;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.pay.api.MiarsPayAPI;
import cn.mcarl.miars.pay.command.RechargeCommand;
import cn.mcarl.miars.pay.event.OrderShipEvent;
import cn.mcarl.miars.pay.listener.PlayerListener;
import cn.mcarl.miars.pay.utils.PayQueue;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;
import java.util.List;

public class MiarsPay extends JavaPlugin {
    private static MiarsPay instance;
    public static MiarsPay getInstance() {
        return instance;
    }
    private static MiarsPayAPI miarsPayAPI;
    public static MiarsPayAPI getGlazedPayAPI() {
        return miarsPayAPI;
    }
    private BukkitTask shipTask;
    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        log(getName() + " " + getDescription().getVersion() + " &7开始加载...");

        long startTime = System.currentTimeMillis();

        log("正在初始化配置文件...");
        miarsPayAPI = new MiarsPayAPI("cookiemc", "http://glazed7.com/", "241480681412");

        log("正在注册监听器...");
        new PlayerListener(this);

        log("正在注册指令...");
        regCommand("Recharge",new RechargeCommand());

        log("正在开启订单监控...");
        this.createShipTask();

        log("加载完成 ,共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

        showAD();
    }

    @SneakyThrows
    @Override
    public void onDisable() {
        log(getName() + " " + getDescription().getVersion() + " 开始卸载...");
        long startTime = System.currentTimeMillis();

        log("卸载监听器...");
        Bukkit.getServicesManager().unregisterAll(this);

        log("卸载完成 ,共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

        showAD();
    }

    /**
     * 注册监听器
     *
     * @param listener 监听器
     */
    public static void regListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, getInstance());
    }

    /**
     * 注册指令
     *
     * @param name 指令名字
     * @param command 指令
     */
    public static void regCommand(String name, CommandExecutor command) {
        Bukkit.getPluginCommand(name).setExecutor(command);
    }

    /**
     * 日志
     * @param message 日志消息
     */
    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(ColorParser.parse("[" + getInstance().getName() + "] " + message));
    }

    /**
     * 作者信息
     */
    private void showAD() {
        log("&7感谢您使用 &c&l"+getDescription().getName()+" v" + getDescription().getVersion());
        log("&7本插件由 &c&lMCarl Studios &7提供长期支持与维护。");
    }

    public void createShipTask() {
        if (this.shipTask != null) {
            this.shipTask.cancel();
        }
        long cycle = 1L;
        this.shipTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, this::ship, 20L, cycle * 20L);
    }

    public void ship() {
        List<JsonObject> unshipOrder = MiarsPay.miarsPayAPI.getUnshipOrder();
        Iterator<JsonObject> iterator;
        for (iterator = PayQueue.getInstance().getPayQueue().iterator(); iterator.hasNext(); ) {
            JsonObject json = iterator.next();
            String buyerName = json.get("buyerName").getAsString();
            Player player = Bukkit.getPlayerExact(buyerName);
            if (player == null) {
                continue;
            }
            Bukkit.getScheduler().runTask(this, () -> ship(player, json));
        }
        for (iterator = unshipOrder.iterator(); iterator.hasNext(); ) {
            JsonObject jsonObject = iterator.next();
            String orderId = jsonObject.get("orderId").getAsString();
            String buyerName = jsonObject.get("buyerName").getAsString();
            Player player = Bukkit.getPlayerExact(buyerName);
            if (player == null) {
                continue;
            }
            if (MiarsPay.miarsPayAPI.markShip(orderId)) {
                Bukkit.getScheduler().runTask(this, () -> ship(player, jsonObject));
            }
        }
    }

    public void ship(Player player, JsonObject orderInfo) {
        String orderId = orderInfo.get("orderId").getAsString();
        PayQueue queue = PayQueue.getInstance();
        if (queue.isRecentOrder(orderId)) {
            queue.delQueue(orderInfo);
            return;
        }
        if (player.isOnline()) {
            player.updateInventory();
            OrderShipEvent orderShipEvent = new OrderShipEvent(this, orderInfo);
            Bukkit.getPluginManager().callEvent(orderShipEvent);
            if (orderShipEvent.isCancelled()) {
                return;
            }
            orderInfo = orderShipEvent.getOrderInfo();
            double money = orderInfo.get("totalFee").getAsDouble();

            MiarsCore.getPpAPI().give(player.getUniqueId(), (int) money);

            player.sendMessage(ColorParser.parse("&a&l商城! &7成功充值了 &6"+money+" &7金子，请注意查收。"));


            queue.delQueue(orderInfo);
            queue.addRecentOrder(orderId);
        } else {
            queue.addQueue(orderInfo);
        }
    }


}
