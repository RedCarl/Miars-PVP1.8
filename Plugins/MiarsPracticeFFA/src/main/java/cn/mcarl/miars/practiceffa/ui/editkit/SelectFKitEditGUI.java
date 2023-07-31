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
import java.util.Map;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 19:54
 */
public class SelectFKitEditGUI extends GUI {

    final Player player;
    final FKitType fKitType;
    final Map<String,FKit> invs;


    public SelectFKitEditGUI(Player player, FKitType fKitType) {
        super(GUIType.ONE_BY_NINE, "&0Archer Loadouts");

        this.fKitType = fKitType;
        this.player = player;
        this.invs = FKitDataStorage.getInstance().getFKitDataByMap(player.getUniqueId(),this.fKitType);

        load();
    }

    private void load(){
        setBookItem(0,"Loadout 1");
        setBookItem(2,"Loadout 2");
        setBookItem(4,"Loadout 3");
        setBookItem(6,"Loadout 4");
        setBookItem(8,"Loadout 5");
    }

    public void setBookItem(int i,String name){

        setItem(i, new GUIItem(
                new ItemBuilder((invs.containsKey(name) ? Material.BOOK : Material.PAPER))
                        .setName("&b"+name)
                        .toItemStack()
        ){
            @Override
            public void onClick(Player clicker, ClickType type) {
                if (type.isLeftClick()){
                    // 是否有数据
                    if (invs.containsKey(name)){
                        // 有数据
                        FKitEditGUI.open(player,invs.get(name));
                    }else {
                        // 没数据
                        FKitEditGUI.open(
                                player,
                                new FKit(
                                        null,
                                        player.getUniqueId().toString(),
                                        fKitType,
                                        name,
                                        FFAUtil.getByFKitType(fKitType),
                                        i,
                                        null,
                                        null
                                )
                        );
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
