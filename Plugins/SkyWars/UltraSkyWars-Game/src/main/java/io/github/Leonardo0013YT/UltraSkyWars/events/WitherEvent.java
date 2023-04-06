package io.github.Leonardo0013YT.UltraSkyWars.events;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.GameEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.metadata.FixedMetadataValue;

public class WitherEvent extends GameEvent {

    private Wither wither;

    public WitherEvent(UltraSkyWars plugin, int time) {
        this.time = time;
        this.reset = time;
        this.wither = null;
        this.type = "final";
        this.name = "wither";
        this.sound = plugin.getConfig().getString("sounds.events." + name);
        this.title = plugin.getLang().get("titles." + name + ".title");
        this.subtitle = plugin.getLang().get("titles." + name + ".subtitle");
    }

    public WitherEvent(WitherEvent e) {
        this.time = e.getReset();
        this.reset = e.getReset();
        this.wither = null;
        this.type = e.getType();
        this.name = e.getName();
        this.sound = e.getSound();
        this.title = e.getTitle();
        this.subtitle = e.getSubTitle();
    }

    @Override
    public void start(Game game) {
        wither = game.getSpectator().getWorld().spawn(game.getSpectator(), Wither.class);
        wither.setNoDamageTicks(999999999);
        wither.setMetadata("CUSTOM", new FixedMetadataValue(UltraSkyWars.get(), "CUSTOM"));
        for (Player on : game.getCached()) {
            CustomSound.EVENTS_WITHER.reproduce(on);
        }
    }

    @Override
    public void stop(Game game) {
        if (wither != null) {
            wither.remove();
        }
    }

    @Override
    public void reset() {
        this.time = this.reset;
        this.wither = null;
        this.type = "final";
        this.name = "wither";
    }

    @Override
    public WitherEvent clone() {
        return new WitherEvent(this);
    }

}