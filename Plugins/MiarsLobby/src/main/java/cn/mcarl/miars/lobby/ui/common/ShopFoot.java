package cn.mcarl.miars.lobby.ui.common;

import cc.carm.lib.easyplugin.gui.GUIItem;
import cn.mcarl.miars.lobby.ui.ShopRank;
import cn.mcarl.miars.lobby.ui.ShopRecharge;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class ShopFoot {
    Player player;
    public ShopFoot(Player player){
        this.player = player;
    }

    public List<GUIItem> get(){
        List<GUIItem> list = new ArrayList<>();

        list.add(
          new GUIItem(
                  new ItemBuilder(Material.SKULL_ITEM)
                          .setData((short)3)
                          .setName("&e金子")
                          .setLore(
                                  "&7在这里充值 &e金子 &7支持我们。",
                                  "&7您可以使用 &a微信 &b支付宝 &cQQ支付 &7来支付。"
                          )
                          .setSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0")
                          .toItemStack()
          ){
              @Override
              public void onClick(Player clicker, ClickType type) {
                  ShopRecharge.open(clicker);
              }
          }
        );
        list.add(
          new GUIItem(
                  new ItemBuilder(Material.SKULL_ITEM)
                          .setData((short)3)
                          .setName("&a头衔")
                          .setLore(
                                  "&7您可以在这里购买头衔。",
                                  "&7会给予您一定的权限和展示信息。"
                          )
                          .setSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzliOTg4MmIzNTIzMTJmYWRiYTlkODM5YTM3ZjdiZjVhMWE4OTNmZDYwYjZlZWM4YWFmMWI0MjQ1MDMyZTY3NiJ9fX0=")
                          .toItemStack()
          ){
              @Override
              public void onClick(Player clicker, ClickType type) {
                  ShopRank.open(clicker);
              }
          }
        );

        return list;
    }
}
