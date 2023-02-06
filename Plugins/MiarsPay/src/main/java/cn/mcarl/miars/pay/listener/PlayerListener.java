package cn.mcarl.miars.pay.listener;

import cn.mcarl.miars.pay.MiarsPay;
import cn.mcarl.miars.pay.event.ClosePayEvent;
import cn.mcarl.miars.pay.event.OpenPayEvent;
import cn.mcarl.miars.pay.event.QrCodeCreateEvent;
import cn.mcarl.miars.pay.manager.PayManager;
import cn.mcarl.miars.pay.utils.Utils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class PlayerListener implements Listener {
    private final MiarsPay plugin;

    public static HashMap<UUID, List<ItemStack>> playerInv = new HashMap<>();

    public PlayerListener(MiarsPay plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClosePay(ClosePayEvent event) {
        Player player = event.getPlayer();

        //释放玩家背包
        for (int i = 0; i < 9; i++) {
            player.getInventory().setItem(i,playerInv.get(player.getUniqueId()).get(i));
        }
    }

    @EventHandler
    public void onOpenPay(OpenPayEvent event) {
        Map<String, Object> map = new HashMap<>();
        map.put("%order%", event.getOrder());
        map.put("%money%", event.getMoney().toString());
        map.put("%payway%", event.getPaywayType().getChineseName());
        List<String> stringList = MiarsPay.getInstance().getConfig().getStringList("open-command");
        List<String> parse = Utils.parse(stringList, map, event.getPlayer());
        parse.forEach(e -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), e));
    }

    @EventHandler
    public void onQrcodeCreate(QrCodeCreateEvent event) {
        int size = this.plugin.getConfig().getInt("map.qrcode-size", 128);
        int hw = (128 - size) / 2;
        BufferedImage qrCode = event.getQrCode();
        BufferedImage newQrCode = new BufferedImage(qrCode.getWidth(), qrCode.getHeight(), qrCode.getType());
        Graphics2D graphics = newQrCode.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, qrCode.getWidth(), qrCode.getHeight());
        graphics.drawImage(qrCode, hw, hw, size, size, null);
        graphics.dispose();
        newQrCode.flush();
        event.setQrCode(newQrCode);

        //储存玩家背包
        List<ItemStack> is = new ArrayList<>();
        for (ItemStack i:event.getPlayer().getInventory()) {
            is.add(i);
        }
        playerInv.put(event.getPlayer().getUniqueId(),is);
    }

    @EventHandler
    public void onClickInv(InventoryClickEvent event) {
        Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
        closePay(player);
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        closePay(player);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        closePay(player);
    }

    @EventHandler
    public void onToggleHand(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        closePay(player);
    }

    public void closePay(Player player){
        if (PayManager.getPaying().contains(player.getUniqueId())) {
            if (PayManager.getPaying().remove(player.getUniqueId())){
                Bukkit.getScheduler().runTask(this.plugin, () -> {
                    ClosePayEvent closePayEvent = new ClosePayEvent(this.plugin, player);
                    Bukkit.getPluginManager().callEvent(closePayEvent);
                });
            }
        }
    }
}