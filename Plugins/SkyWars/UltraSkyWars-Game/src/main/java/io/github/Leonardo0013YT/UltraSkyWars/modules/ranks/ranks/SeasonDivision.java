package io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.ranks;

import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.InjectionEloRank;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SeasonDivision {

    private ItemStack icon;
    private int topMin, topMax;
    private String name;
    private List<String> lore, ranks, rewards;

    public SeasonDivision(InjectionEloRank ier, String path, int season, boolean passed) {
        this.topMin = ier.getRankeds().getInt(path + ".top.min");
        this.topMax = ier.getRankeds().getInt(path + ".top.max");
        this.name = ier.getRankeds().get(path + ".name");
        this.ranks = ier.getRankeds().getList(path + ".ranks");
        this.rewards = ier.getRankeds().getList(path + ".rewards.commands");
        List<String> lore = new ArrayList<>();
        for (String l : ier.getRankeds().getList(path + ".lore")) {
            lore.add(l.replaceAll("&", "§").replaceAll("<season>", String.valueOf(season)).replaceAll("<status>", passed ? ier.getRankeds().get("passed") : ier.getRankeds().get("now")));
        }
        this.lore = lore;
        this.icon = ItemBuilder.item(new ItemStack(XMaterial.matchDefinedXMaterial(ier.getRankeds().get(path + ".icon.material"), (byte) ier.getRankeds().getInt(path + ".icon.data")).orElse(XMaterial.OAK_WOOD).parseMaterial(), ier.getRankeds().getInt(path + ".icon.amount"), (short) ier.getRankeds().getInt(path + ".icon.data")), name + ier.getRankeds().get("format").replaceAll("<max>", String.valueOf(topMax)).replaceAll("<min>", String.valueOf(topMin)), lore);
    }

    public String getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }
}