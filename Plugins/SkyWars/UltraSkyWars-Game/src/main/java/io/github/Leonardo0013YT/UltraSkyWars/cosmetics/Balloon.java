package io.github.Leonardo0013YT.UltraSkyWars.cosmetics;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Balloon extends Cosmetic {

    private ItemStack icon;
    private String url;
    private List<ItemStack> heads = new ArrayList<>();
    private HashMap<UUID, Giant> giants = new HashMap<>();
    private int animation = 0;

    public Balloon(UltraSkyWars plugin, String path) {
        super(plugin.getBalloon(), path, "balloon");
        Object url = plugin.getBalloon().getConfig().get(path + ".url");
        if (url instanceof String) {
            heads.add(ItemBuilder.createSkull("", "", url.toString()));
            this.url = url.toString();
        } else {
            for (String s : plugin.getBalloon().getList(path + ".url")) {
                heads.add(ItemBuilder.createSkull("", "", s));
                this.url = s;
            }
        }
        this.icon = plugin.getBalloon().getConfig().getItemStack(path + ".icon");
        plugin.getCos().setLastPage("Balloon", page);
    }

    public int spawn(Player p, Location balloon, Location fence) {
        giants.put(p.getUniqueId(), Utils.spawn(balloon, fence, getActualHead()));
        return id;
    }

    public List<ItemStack> getHeads() {
        return heads;
    }

    public boolean needUpdate() {
        return !giants.isEmpty();
    }

    public void remove(Player p) {
        if (giants.containsKey(p.getUniqueId())) {
            Giant g = giants.get(p.getUniqueId());
            if (g != null && !g.isDead()) {
                g.remove();
            }
        }
        giants.remove(p.getUniqueId());
    }

    public void update() {
        if (!isAnimated()) return;
        ItemStack head = getActualHead();
        giants.values().forEach(g -> g.getEquipment().setItemInHand(head));
    }

    public String getUrl() {
        return url;
    }

    public boolean isAnimated() {
        return heads.size() > 1;
    }

    public ItemStack getActualHead() {
        if (isAnimated()) {
            int a = animation;
            animation++;
            if (animation >= heads.size()) {
                animation = 0;
            }
            return heads.get(a);
        }
        return heads.get(0);
    }

}