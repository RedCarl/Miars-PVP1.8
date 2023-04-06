package io.github.Leonardo0013YT.UltraSkyWars.events;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.GameEvent;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class DragonEvent extends GameEvent {

    EnderDragon dragon;

    public DragonEvent(UltraSkyWars plugin, int time) {
        this.time = time;
        this.reset = time;
        this.dragon = null;
        this.type = "final";
        this.name = "dragon";
        this.sound = plugin.getConfig().getString("sounds.events." + name);
        this.title = plugin.getLang().get("titles." + name + ".title");
        this.subtitle = plugin.getLang().get("titles." + name + ".subtitle");
    }

    public DragonEvent(DragonEvent e) {
        this.time = e.getReset();
        this.reset = e.getReset();
        this.dragon = null;
        this.type = e.getType();
        this.name = e.getName();
        this.sound = e.getSound();
        this.title = e.getTitle();
        this.subtitle = e.getSubTitle();
    }

    @Override
    public void start(Game game) {
        dragon = game.getSpectator().getWorld().spawn(game.getSpectator(), EnderDragon.class);
        dragon.setNoDamageTicks(999999999);
        dragon.setMetadata("CUSTOM", new FixedMetadataValue(UltraSkyWars.get(), "CUSTOM"));
        for (Player on : game.getCached()) {
            CustomSound.EVENTS_DRAGON.reproduce(on);
        }
    }

    @Override
    public void stop(Game game) {
        if (dragon != null) {
            dragon.remove();
        }
    }

    @Override
    public void reset() {
        this.time = this.reset;
        this.dragon = null;
        this.type = "final";
        this.name = "dragon";
    }

    @Override
    public DragonEvent clone() {
        return new DragonEvent(this);
    }

}