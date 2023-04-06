package io.github.Leonardo0013YT.UltraSkyWars.events;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.game.GameChest;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.GameEvent;
import org.bukkit.entity.Player;

public class RefillEvent extends GameEvent {

    public RefillEvent(UltraSkyWars plugin, int time) {
        this.time = time;
        this.reset = time;
        this.type = "game";
        this.name = "refill";
        this.sound = plugin.getConfig().getString("sounds.events." + name);
        this.title = plugin.getLang().get("titles." + name + ".title");
        this.subtitle = plugin.getLang().get("titles." + name + ".subtitle");
    }

    public RefillEvent(RefillEvent e) {
        this.time = e.getReset();
        this.reset = e.getReset();
        this.type = e.getType();
        this.name = e.getName();
        this.sound = e.getSound();
        this.title = e.getTitle();
        this.subtitle = e.getSubTitle();
    }

    @Override
    public void start(Game game) {
        GameChest gc = game.getCenter();
        gc.fill(game, true);
        game.getTeams().values().forEach(t -> t.getChest().fill(game, true));
        for (Player on : game.getCached()) {
            CustomSound.EVENTS_REFILL.reproduce(on);
        }
    }

    @Override
    public void stop(Game game) {
    }

    @Override
    public void reset() {
        this.time = this.reset;
        this.type = "game";
        this.name = "refill";
    }

    @Override
    public RefillEvent clone() {
        return new RefillEvent(this);
    }

}