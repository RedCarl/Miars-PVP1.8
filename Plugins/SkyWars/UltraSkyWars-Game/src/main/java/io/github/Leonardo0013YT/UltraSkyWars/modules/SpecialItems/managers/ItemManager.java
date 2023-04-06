package io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.InjectionSpecialItems;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.items.SpecialItem;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.items.types.CompassItem;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.items.types.InstantTNTItem;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.items.types.SoupItem;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.items.types.TNTLaunchItem;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class ItemManager {

    private ItemStack compass, instantTNT, soup, endBuff, TNTLaunch;
    private SpecialItem compassItem, instantTNTItem, soupItem, endBuffItem, TNTLaunchItem;
    private UltraSkyWars plugin;
    private InjectionSpecialItems isi;

    public ItemManager(UltraSkyWars plugin, InjectionSpecialItems isi) {
        this.plugin = plugin;
        this.isi = isi;
        reload();
    }

    public void reload() {
        this.compass = ItemBuilder.item(XMaterial.COMPASS, plugin.getLang().get("items.compass.nameItem"), plugin.getLang().get("items.compass.loreItem"));
        this.instantTNT = ItemBuilder.item(XMaterial.TNT, plugin.getLang().get("items.instantTNT.nameItem"), plugin.getLang().get("items.instantTNT.loreItem"));
        this.soup = ItemBuilder.item(XMaterial.MUSHROOM_STEW, plugin.getLang().get("items.soup.nameItem"), plugin.getLang().get("items.soup.loreItem"));
        this.endBuff = ItemBuilder.item(XMaterial.ENDER_PEARL, plugin.getLang().get("items.endBuff.nameItem"), plugin.getLang().get("items.endBuff.loreItem"));
        this.TNTLaunch = ItemBuilder.item(XMaterial.TNT, plugin.getLang().get("items.TNTLaunch.nameItem"), plugin.getLang().get("items.TNTLaunch.loreItem"));
        this.compassItem = new CompassItem(isi);
        this.instantTNTItem = new InstantTNTItem(isi);
        this.TNTLaunchItem = new TNTLaunchItem(isi);
        this.soupItem = new SoupItem(isi);
    }
}