package io.github.Leonardo0013YT.UltraSkyWars.modules.challenges.enums;

import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public enum ChallengeType {

    UHC(true, true, XMaterial.GOLDEN_APPLE.parseMaterial(), 1, 10, new ArrayList<>()),
    NO_CHEST(true, true, XMaterial.CHEST.parseMaterial(), 3, 12, new ArrayList<>()),
    ARCHERY(true, true, XMaterial.BOW.parseMaterial(), 5, 14, new ArrayList<>()),
    PAPER(true, true, XMaterial.PAPER.parseMaterial(), 7, 16, new ArrayList<>()),
    HEARTS(true, true, XMaterial.MELON_SLICE.parseMaterial(), 28, 37, new ArrayList<>()),
    WARRIOR(true, true, XMaterial.STONE_SWORD.parseMaterial(), 30, 39, new ArrayList<>()),
    NO_BLOCKS(true, true, XMaterial.BEDROCK.parseMaterial(), 32, 41, new ArrayList<>()),
    NOOB(true, true, XMaterial.WOODEN_PICKAXE.parseMaterial(), 34, 43, new ArrayList<>());

    private boolean enabled, onWinEnabled;
    private Material material;
    private int iconSlot, statusSlot;
    private List<String> onWinCommands;

    ChallengeType(boolean enabled, boolean onWinEnabled, Material material, int iconSlot, int statusSlot, List<String> onWinCommands) {
        this.enabled = enabled;
        this.onWinEnabled = onWinEnabled;
        this.material = material;
        this.iconSlot = iconSlot;
        this.statusSlot = statusSlot;
        this.onWinCommands = onWinCommands;
    }

    public int getIconSlot() {
        return iconSlot;
    }

    public void setIconSlot(int iconSlot) {
        this.iconSlot = iconSlot;
    }

    public int getStatusSlot() {
        return statusSlot;
    }

    public void setStatusSlot(int statusSlot) {
        this.statusSlot = statusSlot;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isOnWinEnabled() {
        return onWinEnabled;
    }

    public void setOnWinEnabled(boolean onWinEnabled) {
        this.onWinEnabled = onWinEnabled;
    }

    public List<String> getOnWinCommands() {
        return onWinCommands;
    }

    public void setOnWinCommands(List<String> onWinCommands) {
        this.onWinCommands = onWinCommands;
    }
}