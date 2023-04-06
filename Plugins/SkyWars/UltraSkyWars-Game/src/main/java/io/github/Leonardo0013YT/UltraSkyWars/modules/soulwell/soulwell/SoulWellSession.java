package io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.enums.RewardType;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.animations.SoulWell3DAnimation;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.animations.SoulWellFenixAnimation;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.animations.SoulWellNormalAnimation;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.interfaces.SoulWellAnimation;
import io.github.Leonardo0013YT.UltraSkyWars.objects.Reward;
import io.github.Leonardo0013YT.UltraSkyWars.utils.InstantFirework;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SoulWellSession {

    private UltraSkyWars plugin;
    private InjectionSoulWell is;
    private SoulWellAnimation animation;
    private Player p;
    private Location loc;
    private SoulWellRow row;
    @Setter
    @Getter
    private boolean confirm, rolling = false, deleted = false;

    public SoulWellSession(UltraSkyWars plugin, InjectionSoulWell is, Player p, Location loc) {
        this.plugin = plugin;
        this.is = is;
        this.p = p;
        this.loc = loc;
    }

    public void execute(SWPlayer sw, int rows) {
        row = is.getSwm().getRows().get(rows);
        confirm = true;
        if (sw.getSoulanimation() == 0) {
            rolling = true;
            animation = new SoulWellNormalAnimation(plugin, is, this, p, row, loc);
            animation.execute();
        } else if (sw.getSoulanimation() == 1) {
            rolling = true;
            animation = new SoulWellFenixAnimation(plugin, is, this, p, row, loc);
            animation.execute();
        } else {
            p.closeInventory();
            animation = new SoulWell3DAnimation(plugin, is, this, p, row, loc);
            animation.execute();
        }
    }

    public void executeReward(Player p, ItemStack item) {
        Reward swr = plugin.getLvl().getSoulWellRewardByIcon(item);
        swr.execute(p);
        p.sendMessage(plugin.getLang().get(p, "messages.reward").replaceAll("<name>", swr.getName()).replaceAll("<rarity>", plugin.getLang().get(p, "soulwell.rarity." + swr.getRarity().name().toLowerCase())));
        if (swr.getRarity().equals(RewardType.COMMON)) {
            CustomSound.REWARDS_COMMON.reproduce(p);
        } else if (swr.getRarity().equals(RewardType.UNCOMMON)) {
            CustomSound.REWARDS_UNCOMMON.reproduce(p);
        } else if (swr.getRarity().equals(RewardType.RARE)) {
            CustomSound.REWARDS_RARE.reproduce(p);
        } else if (swr.getRarity().equals(RewardType.EPIC)) {
            CustomSound.REWARDS_EPIC.reproduce(p);
        } else {
            CustomSound.REWARDS_LEGENDARY.reproduce(p);
        }
    }

    public void firework(Location loc) {
        new InstantFirework(FireworkEffect.builder().withColor(Utils.getRandomColor()).withFade(Utils.getRandomColor()).with(FireworkEffect.Type.BALL).build(), loc);
    }

    public void deleteHologram() {
        SoulWell sw = is.getSwm().getSoulWells().get(loc);
        if (plugin.getAdm().hasHologramPlugin()) {
            if (!plugin.getAdm().hasHologram(sw.getHologram())) return;
            deleted = true;
            plugin.getAdm().deleteHologram(sw.getHologram());
        }
    }

    public void recreateHologram() {
        SoulWell sw = is.getSwm().getSoulWells().get(loc);
        if (plugin.getAdm().hasHologramPlugin()) {
            if (plugin.getAdm().hasHologram(sw.getHologram())) return;
            deleted = false;
            plugin.getAdm().createHologram(sw.getHologram(), plugin.getLang().getList("holograms.soulwell"));
        }
    }

    public void cancel(Player p) {
        if (animation == null) return;
        animation.cancel(p);
    }

    public Inventory getInv() {
        return animation.getInv();
    }
}