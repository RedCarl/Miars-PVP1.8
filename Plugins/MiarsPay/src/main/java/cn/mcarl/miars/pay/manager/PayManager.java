package cn.mcarl.miars.pay.manager;

import cn.mcarl.miars.pay.MiarsPay;
import cn.mcarl.miars.pay.api.MiarsPayAPI;
import cn.mcarl.miars.pay.api.ImageRenderer;
import cn.mcarl.miars.pay.enums.PaywayType;
import cn.mcarl.miars.pay.event.OpenPayEvent;
import cn.mcarl.miars.pay.event.QrCodeCreateEvent;
import cn.mcarl.miars.pay.utils.Utils;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.util.*;

public class PayManager {
    private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    private static final List<UUID> creating = new ArrayList<>();

    private static final List<UUID> paying = new ArrayList<>();

    public static void initiatePay(Player player, PaywayType paywayType, Double money) {
        if (creating.contains(player.getUniqueId()) || paying.contains(player.getUniqueId())) {
            return;
        }
        creating.add(player.getUniqueId());
        MiarsPay plugin = MiarsPay.getInstance();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            MiarsPayAPI miarsPayAPI = MiarsPay.getGlazedPayAPI();
            String order = miarsPayAPI.createOrder(player.getName(), paywayType, money);
            if (order != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("%order%", order);
                map.put("%money%", money.toString());
                map.put("%payway%", paywayType.getChineseName());
                BufferedImage qrCode = miarsPayAPI.createQrCode(order);
                if (qrCode != null) {
                    Map<String, Object> orderMap = new HashMap<>();
                    orderMap.put("%order%", order);
                    orderMap.put("%money%", money.toString());
                    orderMap.put("%payway%", paywayType.getChineseName());
                    QrCodeCreateEvent qrCodeCreateEvent = new QrCodeCreateEvent(plugin, player, paywayType, money, qrCode);
                    Bukkit.getPluginManager().callEvent(qrCodeCreateEvent);
                    qrCode = qrCodeCreateEvent.getQrCode();
                    FileConfiguration config = plugin.getConfig();
                    ItemStack item = Utils.createItem(Material.MAP, config.getString("map.display"), config.getStringList("map.lore"));
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.addItemFlags(ItemFlag.values());
                    item.setItemMeta(itemMeta);
                    sendMapItem(player, item);
                    sendMapView(player, qrCode);
                    paying.add(player.getUniqueId());

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        OpenPayEvent openPayEvent = new OpenPayEvent(plugin, player, order, paywayType, money);
                        Bukkit.getPluginManager().callEvent(openPayEvent);
                    });
                } else {
                }
            } else {
            }
            creating.remove(player.getUniqueId());
        });
    }

    public static void sendMapView(Player player, BufferedImage bufferedImage) {
        MapView view = Bukkit.getMap(player.getItemInHand().getDurability());
        for (MapRenderer mapRenderer : view.getRenderers()) {
            view.removeRenderer(mapRenderer);
        }
        ImageRenderer renderer = new ImageRenderer(bufferedImage);
        view.addRenderer(renderer);
    }

    public static void sendMapItem(Player player, ItemStack item) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            player.getInventory().setItem(i,new ItemStack(Material.AIR));
        }
        player.setItemInHand(item);
    }

    public static List<UUID> getPaying() {
        return paying;
    }
}