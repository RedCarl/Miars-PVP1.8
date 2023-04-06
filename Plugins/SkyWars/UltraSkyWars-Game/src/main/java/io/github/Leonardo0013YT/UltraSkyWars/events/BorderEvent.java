package io.github.Leonardo0013YT.UltraSkyWars.events;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.GameEvent;
import org.bukkit.Location;
import org.bukkit.WorldBorder;

public class BorderEvent extends GameEvent {

    public BorderEvent(UltraSkyWars plugin, int time) {
        this.time = time;
        this.reset = time;
        this.type = "final";
        this.name = "border";
        this.sound = plugin.getConfig().getString("sounds.events." + name);
        this.title = plugin.getLang().get("titles." + name + ".title");
        this.subtitle = plugin.getLang().get("titles." + name + ".subtitle");
    }

    public BorderEvent(BorderEvent e) {
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
        WorldBorder wb = game.getSpectator().getWorld().getWorldBorder();
        wb.setCenter(new Location(game.getSpectator().getWorld(), game.getBorderX(), 75, game.getBorderZ()));
        wb.setDamageAmount(1.0);
        wb.setWarningDistance(0);
        wb.setWarningTime(0);
        wb.setSize(game.getBorderStart());
        wb.setSize(game.getBorderEnd(), UltraSkyWars.get().getCm().getTimeBorderReduction());
    }

    @Override
    public void stop(Game game) {
        WorldBorder wb = game.getSpectator().getWorld().getWorldBorder();
        wb.reset();
    }

    @Override
    public void reset() {
        this.time = this.reset;
        this.type = "final";
        this.name = "border";
    }

    @Override
    public BorderEvent clone() {
        return new BorderEvent(this);
    }

}