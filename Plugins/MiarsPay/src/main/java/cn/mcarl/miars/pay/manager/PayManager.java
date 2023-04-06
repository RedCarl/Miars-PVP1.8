package cn.mcarl.miars.pay.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.pay.MiarsPay;
import cn.mcarl.miars.pay.api.MiarsPayAPI;
import cn.mcarl.miars.pay.api.ImageRenderer;
import cn.mcarl.miars.pay.enums.PaywayType;
import cn.mcarl.miars.pay.event.OpenPayEvent;
import cn.mcarl.miars.pay.event.QrCodeCreateEvent;
import cn.mcarl.miars.pay.utils.MapColor;
import cn.mcarl.miars.pay.utils.Utils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
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
            player.sendMessage(ColorParser.parse("&e&l商城! &7正在生成订单..."));
            String order = miarsPayAPI.createOrder(player.getName(), paywayType, money);
            if (order != null) {
                BufferedImage qrCode = miarsPayAPI.createQrCode(order);
                if (qrCode != null) {
                    QrCodeCreateEvent qrCodeCreateEvent = new QrCodeCreateEvent(plugin, player, paywayType, money, qrCode);
                    Bukkit.getPluginManager().callEvent(qrCodeCreateEvent);
                    qrCode = qrCodeCreateEvent.getQrCode();

                    //sendMapViewPacket(player, qrCode);

                    ItemStack item = Utils.createItem(Material.MAP, "&a&l扫码地图", List.of(
                            "&7请扫描地图上的二维码",
                            "&7支付完成后服务器",
                            "&7会在数秒后自动发货"
                    ));
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.addItemFlags(ItemFlag.values());
                    item.setItemMeta(itemMeta);

                    //sendMapItemPacket(player, item);


                    sendMapItem(player, item);
                    BufferedImage finalQrCode = qrCode;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            sendMapView(player, finalQrCode);
                            paying.add(player.getUniqueId());
                            player.sendMessage(ColorParser.parse("&e&l商城! &7丢弃物品可关闭订单。"));
                            player.sendMessage(ColorParser.parse("&e&l商城! &7订单创建成功，等待您扫码完成支付操作..."));
                        }
                    }.runTaskLater(MiarsPay.getInstance(),5);

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        OpenPayEvent openPayEvent = new OpenPayEvent(plugin, player, order, paywayType, money);
                        Bukkit.getPluginManager().callEvent(openPayEvent);
                    });
                } else {
                }
            } else {
                player.sendMessage(ColorParser.parse("&c&l商城! &7订单生成失败，请联系管理员。"));
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

    public static void sendMapViewPacket(Player player, BufferedImage bufferedImage) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.MAP);
        byte[] bytes = MapColor.getByte(bufferedImage);
        packet.getIntegers().write(0, 0);
        packet.getIntegers().write(1, 0);
        packet.getIntegers().write(2, 0);
        packet.getIntegers().write(3, 128);
        packet.getIntegers().write(4, 128);
        packet.getBytes().write(0, (byte) 0);
        packet.getByteArrays().write(0, bytes);
        // packet.getBooleans().write(0, Boolean.TRUE);
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void sendMapItemPacket(Player player, ItemStack map) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.WINDOW_ITEMS);
        ArrayList<ItemStack> items = new ArrayList<>(46);
        int slot = 36 + player.getInventory().getHeldItemSlot();
        for (int i = 0; i < 46; i++) {
            items.add(new ItemStack(Material.AIR));
        }
        items.set(slot, map);
        packet.getIntegers().write(0, 0);
        packet.getItemListModifier().write(0, items);
        try {
            paying.add(player.getUniqueId());
            protocolManager.sendServerPacket(player, packet, false);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }



    public static List<UUID> getPaying() {
        return paying;
    }
}