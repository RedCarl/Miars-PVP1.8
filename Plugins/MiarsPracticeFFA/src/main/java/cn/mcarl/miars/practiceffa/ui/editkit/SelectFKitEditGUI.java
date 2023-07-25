package cn.mcarl.miars.practiceffa.ui.editkit;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import cn.mcarl.miars.storage.storage.data.practice.FKitDataStorage;
import cn.mcarl.miars.storage.utils.CustomSort;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.ffa.FKit;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 19:54
 */
public class SelectFKitEditGUI extends GUI {

    final Player player;
    final FKitType fKitType;
    final List<FKit> invs;


    public SelectFKitEditGUI(Player player, FKitType fKitType) {
        super(GUIType.ONE_BY_NINE, "&0Select Kit");

        this.fKitType = fKitType;
        this.player = player;
        this.invs = FKitDataStorage.getInstance().getFKitData(player.getUniqueId(),this.fKitType);

        //排序
        CustomSort.sort(invs,"power",true);

        load();
    }

    private void load(){
        for (int i = 1; i <= 9 ; i++) {
            
            if (i == 8 && (player.hasPermission("practice.kits.9"))){
                setBookItem(i);
                continue;
            }

            if (i >= 6 && (player.hasPermission("practice.kits.7") || player.hasPermission("practice.kits.9"))){
                setBookItem(i);
                continue;
            }

            if (i >= 4 && (player.hasPermission("practice.kits.5") || player.hasPermission("practice.kits.7") || player.hasPermission("practice.kits.9"))){
                setBookItem(i);
                continue;
            }

            if (i >= 2 && (player.hasPermission("practice.kits.3") || player.hasPermission("practice.kits.5") || player.hasPermission("practice.kits.7") || player.hasPermission("practice.kits.9"))){
                setBookItem(i);
                continue;
            }

            if (i == 1 && (player.hasPermission("practice.kits.1") || player.hasPermission("practice.kits.3") || player.hasPermission("practice.kits.5") || player.hasPermission("practice.kits.7") || player.hasPermission("practice.kits.9"))){
                setBookItem(i);
                continue;
            }


            
            setItem(i-1, new GUIItem(
                    new ItemBuilder(Material.BARRIER)
                            .setName("&c很抱歉，您没有解锁该位置。")
                            .toItemStack()
            ));

        }
    }

    public void setBookItem(int i){
        setItem(i-1, new GUIItem(
                new ItemBuilder((i<=invs.size() ? Material.BOOK : Material.PAPER))
                        .setName((i<=invs.size() ? invs.get(i-1).getName()+"&f( "+invs.get(i-1).getPower()+" )" : "&f空"))
                        .addLoreLine((i<=invs.size() ? "&7您可以继续编辑这个模式背包。" : "&7这个位置并没有被使用。"))
                        .addLoreLine("&r")
                        .addLoreLine((i<=invs.size() ? "&a左键编辑背包 &7/ &a右键顺序靠前" : "&a点击创建"))
                        .toItemStack()
        ){
            @Override
            public void onClick(Player clicker, ClickType type) {
                if (type.isLeftClick()){
                    // 是否有数据
                    if (i<=invs.size()){
                        // 有数据
                        FKitEditGUI.open(player,invs.get(i-1));
                    }else {
                        // 没数据
                        FKitEditGUI.open(player,new FKit(null,player.getUniqueId().toString(),fKitType,null, FFAUtil.getByFKitType(fKitType),null,null,null));
                    }
                }
            }
        });
    }


    public static void open(Player player, FKitType type) {
        SelectFKitEditGUI gui = new SelectFKitEditGUI(player,type);
        gui.openGUI(player);
    }
}
