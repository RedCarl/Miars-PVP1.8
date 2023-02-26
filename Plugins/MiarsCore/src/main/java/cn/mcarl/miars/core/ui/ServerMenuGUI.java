package cn.mcarl.miars.core.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.storage.entity.MServerInfo;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.serverMenu.ServerMenuItem;
import cn.mcarl.miars.storage.storage.data.serverMenu.ServerMenuDataStorage;
import com.comphenix.protocol.PacketType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServerMenuGUI extends GUI {
    boolean is = false;
    public ServerMenuGUI(Player player,String guiName) {
        super(GUIType.SIX_BY_NINE, guiName);

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

                load(is);

                updateView();

            }
        }.runTaskTimerAsynchronously(MiarsCore.getInstance(),0,20L);
    }

    public void load(boolean is){
        List<ServerMenuItem> list = ServerMenuDataStorage.getInstance().getServerMenuItem(getGUIName());

        if (list.size()==0){
            return;
        }

        for (ServerMenuItem menuItem:list) {
            setItem(menuItem.getSlot(),new GUIItem(
                    new ItemBuilder(menuItem.getIcon())
                            .setName(menuItem.getName())
                            .setLore(ToolUtils.initLorePapi(menuItem.getLore(),is,"gui"))
                            .toItemStack()
            ){
                @Override
                public void onClick(Player clicker, ClickType type) {
                    if (type.name().equals(menuItem.getClickType()) || "ALL".equals(menuItem.getClickType())){
                        switch (menuItem.getType()){
                            case "server" -> {
                                if (ServerManager.getInstance().getServerInfo(menuItem.getValue()) == null) {
                                    clicker.sendMessage(ColorParser.parse("&7很抱歉,该服务器暂时无法进入,请耐心等待..."));
                                    return;
                                }
                                MiarsCore.getBungeeApi().connect(clicker.getPlayer(),menuItem.getValue());
                            }
                            case "message" -> {
                                for (String s:menuItem.getValue().split("/n")) {
                                    clicker.sendMessage(ColorParser.parse(s));
                                }
                            }
                        }
                    }
                }
            });
        }
    }


    public static void open(Player player,String guiName) {
        player.closeInventory();
        ServerMenuGUI gui = new ServerMenuGUI(player,guiName);
        gui.openGUI(player);
    }
}
