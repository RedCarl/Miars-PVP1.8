package io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.menus;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.StatType;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.InjectionEloRank;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.ranks.Season;
import io.github.Leonardo0013YT.UltraSkyWars.modules.ranks.ranks.SeasonDivision;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RankedMenu {

    // SELECT UUID, Elo, (SELECT COUNT(*)+1 FROM UltraSkyWars WHERE Elo>x.Elo) AS Ranking FROM UltraSkyWars x WHERE x.UUID = '3f1f95d8-fd39-11ea-ba2b-d05099d6ad05';
    // SET @rank=0;
    // SELECT UUID, @rank:=@rank+1 AS Rank, Elo FROM UltraSkyWars ORDER BY Elo DESC;
    private int[] slots = {2, 3, 4, 5, 6, 7};
    private InjectionEloRank ier;

    public RankedMenu(InjectionEloRank ier) {
        this.ier = ier;
    }

    public void createRankedMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, ier.getRankeds().get("menus.title"));
        int actSeason = ier.getRankeds().getInt("data.season");
        if (ier.getSm().getLasted().size() == 1) {
            Season s = ier.getSm().getLasted().get(0);
            inv.setItem(0, a(actSeason, s));
            int i = 0;
            for (int d : s.getDivisions().keySet()) {
                SeasonDivision sd = s.getDivisions().get(d);
                if (i >= slots.length) {
                    break;
                }
                inv.setItem(slots[i], sd.getIcon());
                i++;
            }
        } else {
            for (int it = 0; it < 3; it++) {
                int number = ier.getSm().getSeason() - it;
                Season s = ier.getSm().getLasted().get(number);
                if (s == null) {
                    continue;
                }
                int i = 0;
                int su = (18 * it);
                ;
                inv.setItem(su, a(actSeason, s));
                for (int d : s.getDivisions().keySet()) {
                    SeasonDivision sd = s.getDivisions().get(d);
                    if (i >= slots.length) {
                        break;
                    }
                    inv.setItem(slots[i] + su, sd.getIcon());
                    i++;
                }
            }
        }
        SWPlayer sw = UltraSkyWars.get().getDb().getSWPlayer(p);
        ItemStack ranked = ItemBuilder.item(XMaterial.GOLD_BLOCK, ier.getRankeds().get("menus.main.nameItem"), ier.getRankeds().get("menus.main.loreItem").replaceAll("<season>", String.valueOf(ier.getSm().getSeason())).replaceAll("<kills>", String.valueOf(sw.getStat(StatType.KILLS, "RANKED"))).replaceAll("<wins>", String.valueOf(sw.getStat(StatType.WINS, "RANKED"))).replaceAll("<lastRating>", String.valueOf(sw.getRanking())).replaceAll("<rating>", String.valueOf(sw.getRanking())));
        inv.setItem(49, ranked);
        p.openInventory(inv);
    }

    private ItemStack a(int actSeason, Season s) {
        return ItemBuilder.item(XMaterial.ENDER_EYE, ier.getRankeds().get("menus.season.nameItem").replaceAll("<season>", String.valueOf(s.getSeason())), ier.getRankeds().get("menus.season.loreItem").replaceAll("<status>", (actSeason > s.getSeason()) ? ier.getRankeds().get("menus.passed").replaceAll("<season>", String.valueOf(s.getSeason())) : ier.getRankeds().get("menus.now").replaceAll("<divisions>", getDivisions())));
    }

    public String getDivisions() {
        StringBuilder sb = new StringBuilder("\n");
        for (SeasonDivision sd : ier.getSm().getActualSeason().getDivisions().values()) {
            sb.append("\n").append(ier.getRankeds().get("menus.division").replaceAll("<amount>", "0").replaceAll("<division>", sd.getName()));
        }
        return sb.toString().replaceFirst("\n", "");
    }

}