package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.trails;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.TrailType;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.inventory.ItemStack;

public class Trail extends Cosmetic {

    private TrailType type;
    private ItemStack icon;
    private String particle;
    private float offsetX, offsetY, offsetZ;
    private int amount;
    private double range, speed;

    public Trail(UltraSkyWars plugin, String path) {
        super(plugin.getTrail(), path, "trails");
        this.type = TrailType.valueOf(plugin.getTrail().getOrDefault(path + ".type", "NORMAL"));
        this.particle = plugin.getTrail().get(path + ".particle");
        this.amount = plugin.getTrail().getInt(path + ".amount");
        this.icon = Utils.getIcon(plugin.getTrail(), path);
        this.range = plugin.getTrail().getConfig().getDouble(path + ".range");
        this.speed = plugin.getTrail().getConfig().getDouble(path + ".speed");
        this.offsetX = (float) plugin.getTrail().getConfig().getDouble(path + ".offsetX");
        this.offsetY = (float) plugin.getTrail().getConfig().getDouble(path + ".offsetY");
        this.offsetZ = (float) plugin.getTrail().getConfig().getDouble(path + ".offsetZ");
        plugin.getCos().setLastPage("Trail", page);
    }

    public TrailType getType() {
        return type;
    }

    public String getParticle() {
        return particle;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public float getOffsetZ() {
        return offsetZ;
    }

    public int getAmount() {
        return amount;
    }

    public double getSpeed() {
        return speed;
    }

    public double getRange() {
        return range;
    }

}