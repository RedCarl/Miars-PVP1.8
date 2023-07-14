package cn.mcarl.miars.core.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.conf.PluginConfig;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.storage.entity.vault.enums.PriceType;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.MiarsUtils;
import cn.mcarl.miars.storage.entity.serverMenu.ServerMenuItem;
import cn.mcarl.miars.storage.entity.serverMenu.ShopItem;
import cn.mcarl.miars.storage.storage.data.serverMenu.ServerMenuDataStorage;
import com.alibaba.fastjson.JSON;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public class ServerMenuGUI extends GUI {
    boolean is = false;
    public ServerMenuGUI(Player player,String guiName,String sizeType,List<ServerMenuItem> list) {
        super(GUIType.valueOf(sizeType), guiName);

        new BukkitRunnable() {
            @Override
            public void run() {

                if (is){
                    is=false;
                }else {
                    is=true;
                }


                if (getOpenedGUI(player)==null){
                    cancel();
                }

                load(is,list,player);

                updateView();

            }
        }.runTaskTimerAsynchronously(MiarsCore.getInstance(),0,20L);
    }

    public void load(boolean is,List<ServerMenuItem> list,Player player){


        if (list.size()==0){
            return;
        }

        for (ServerMenuItem menuItem:list) {

            switch (menuItem.getType()){
                case "server" -> {
                    setItem(menuItem.getSlot(),new GUIItem(
                            new ItemBuilder(menuItem.getIcon())
                                    .setName(PlaceholderAPI.setPlaceholders(player,menuItem.getName()))
                                    .setLore(MiarsUtils.initLorePapi(PlaceholderAPI.setPlaceholders(player,menuItem.getLore()),is,"gui",menuItem.getValue()))
                                    .toItemStack()
                    ){
                        @Override
                        public void onClick(Player clicker, ClickType type) {
                            if (type.name().equals(menuItem.getClickType()) || "ALL".equals(menuItem.getClickType())){
                                if (Objects.equals(PluginConfig.SERVER_INFO.NAME.get(), menuItem.getValue())){
                                    clicker.sendMessage(ColorParser.parse("&7很抱歉,您正在这个服务器,无需再次加入。"));
                                    return;
                                }
                                if (ServerManager.getInstance().getServerInfo(menuItem.getValue()) == null) {
                                    clicker.sendMessage(ColorParser.parse("&7很抱歉,该服务器暂时无法进入,请耐心等待..."));
                                    return;
                                }
                                MiarsCore.getBungeeApi().connect(clicker.getPlayer(),menuItem.getValue());
                            }
                        }
                    });
                }
                case "menu" -> {
                    setItem(menuItem.getSlot(),new GUIItem(
                            new ItemBuilder(menuItem.getIcon())
                                    .setName(PlaceholderAPI.setPlaceholders(player,menuItem.getName()))
                                    .setLore(PlaceholderAPI.setPlaceholders(player,menuItem.getLore()))
                                    .toItemStack()
                    ){
                        @Override
                        public void onClick(Player clicker, ClickType type) {
                            if (type.name().equals(menuItem.getClickType()) || "ALL".equals(menuItem.getClickType())){
                                ServerMenuGUI.open(clicker,menuItem.getValue());
                            }
                        }
                    });
                }
                case "command" -> {
                    setItem(menuItem.getSlot(),new GUIItem(
                            new ItemBuilder(menuItem.getIcon())
                                    .setName(PlaceholderAPI.setPlaceholders(player,menuItem.getName()))
                                    .setLore(PlaceholderAPI.setPlaceholders(player,menuItem.getLore()))
                                    .toItemStack()
                    ){
                        @Override
                        public void onClick(Player clicker, ClickType type) {
                            if (type.name().equals(menuItem.getClickType()) || "ALL".equals(menuItem.getClickType())){
                                Bukkit.dispatchCommand(player,PlaceholderAPI.setPlaceholders(player,menuItem.getValue()));
                            }
                        }
                    });
                }
                case "buy" -> {

                    ShopItem shopItem = new ShopItem(JSON.toJavaObject(JSON.parseObject(menuItem.getValue()),ShopItem.ToString.class));
                    setItem(menuItem.getSlot(),new GUIItem(
                            shopItem.getItems().size()==1
                                    ?
                                    new ItemBuilder(new ItemStack(shopItem.getItems().get(0)))
                                            .setLore(PlaceholderAPI.setPlaceholders(player,menuItem.getLore()))
                                            .addLoreLine(
                                                    "&7价格: "+
                                                            (
                                                                    shopItem.getDiscount()==0?
                                                                            shopItem.getType().getColor()+shopItem.getPrice():
                                                                            "&7&m"+shopItem.getPrice()+"&r "+shopItem.getType().getColor()+(shopItem.getPrice()*shopItem.getDiscount())
                                                                                    +" &c&l"+(shopItem.getDiscount()*10)+"折优惠"
                                                            )
                                                            +" &7"+(shopItem.getType().getName())
                                            )
                                            .toItemStack()
                                    :
                                    new ItemBuilder(menuItem.getIcon())
                                            .setName(PlaceholderAPI.setPlaceholders(player,menuItem.getName()))
                                            .setLore(PlaceholderAPI.setPlaceholders(player,menuItem.getLore()))
                                            .addLoreLine(
                                                    "&7价格: "+
                                                            (
                                                                    shopItem.getDiscount()==0?
                                                                            shopItem.getType().getColor()+shopItem.getPrice():
                                                                            "&7&m"+shopItem.getPrice()+"&r "+shopItem.getType().getColor()+(shopItem.getPrice()*shopItem.getDiscount())
                                                                                    +" &c&l"+(shopItem.getDiscount()*10)+"折优惠"
                                                            )
                                                            +" &7"+(shopItem.getType().getName())
                                            )
                                            .toItemStack()
                    ){
                        @Override
                        public void onClick(Player clicker, ClickType type) {
                            if (type.name().equals(menuItem.getClickType()) || "ALL".equals(menuItem.getClickType())){

                                double money = shopItem.getDiscount()==0?shopItem.getPrice():shopItem.getPrice()*shopItem.getDiscount();

                                if (shopItem.getType()== PriceType.VAULT){
                                    if (MiarsCore.getEcon().has(player,money)){
                                        MiarsCore.getEcon().withdrawPlayer(player,money);

                                        // 执行指令
                                        for (String s:shopItem.getCommand()) {
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),PlaceholderAPI.setPlaceholders(player,s));
                                        }

                                        // 给予物品
                                        for (ItemStack s:shopItem.getItems()) {
                                            player.getInventory().addItem(s);
                                        }

                                        player.closeInventory();
                                        player.sendMessage(ColorParser.parse("&a&l购买成功! &7您成功购买了这个物品，请注意查收！"));
                                    }else {
                                        player.closeInventory();
                                        player.sendMessage(ColorParser.parse("&c&l余额不足! &7很抱歉，您的余额不足购买这个物品。"));
                                    }
                                }else {
                                    if (MiarsCore.getPpAPI().look(player.getUniqueId()) >= money){
                                        MiarsCore.getPpAPI().take(player.getUniqueId(), (int) money);

                                        // 执行指令
                                        for (String s:shopItem.getCommand()) {
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),PlaceholderAPI.setPlaceholders(player,s));
                                        }

                                        // 给予物品
                                        for (ItemStack s:shopItem.getItems()) {
                                            player.getInventory().addItem(s);
                                        }

                                        player.closeInventory();
                                        player.sendMessage(ColorParser.parse("&a&l购买成功! &7您成功购买了这个物品，请注意查收！"));
                                    }else {
                                        player.closeInventory();
                                        player.sendMessage(ColorParser.parse("&c&l余额不足! &7很抱歉，您的余额不足购买这个物品。"));
                                    }
                                }
                            }
                        }
                    });
                }
                case "sell" -> {

                    ShopItem shopItem = new ShopItem(JSON.toJavaObject(JSON.parseObject(menuItem.getValue()),ShopItem.ToString.class));
                    setItem(menuItem.getSlot(),new GUIItem(
                            new ItemBuilder(menuItem.getIcon())
                                    .setLore(
                                            "&r",
                                            "&7报酬: "+shopItem.getType().getColor()+shopItem.getPrice()+" &7"+(shopItem.getType().getName())
                                    )
                                    .toItemStack()
                    ){
                        @Override
                        public void onClick(Player clicker, ClickType type) {
                            if (type.name().equals(menuItem.getClickType()) || "ALL".equals(menuItem.getClickType())){

                                int amount = 0;

                                for (ItemStack i:player.getInventory().getContents()) {
                                    for (ItemStack ii:shopItem.getItems()) {
                                        if (i!=null && i.getType() == ii.getType()){
                                            amount+=i.getAmount();
                                            player.getInventory().remove(i);
                                        }
                                    }
                                }

                                if (amount!=0){
                                    MiarsCore.getEcon().depositPlayer(player,amount*shopItem.getPrice());
                                    player.sendMessage(ColorParser.parse("&a&l出售成功! &7您成功出售了您背包内所有的该物品，获得 &e"+amount*shopItem.getPrice()+" &7硬币。"));
                                }else {
                                    player.sendMessage(ColorParser.parse("&c&l出售失败! &7您的物品不足，请检查背包。"));
                                }

                                player.closeInventory();
                            }
                        }
                    });
                }
            }
        }
    }


    public static void open(Player player,String guiName) {
        player.closeInventory();
        List<ServerMenuItem> list = ServerMenuDataStorage.getInstance().getServerMenuItem(guiName);
        if (list.size()==0){
            player.sendMessage(ColorParser.parse("&7找不到该菜单，请联系管理员。"));
            return;
        }
        ServerMenuGUI gui = new ServerMenuGUI(player,guiName,list.get(0).getSizeType(),list);
        gui.openGUI(player);
    }
}
