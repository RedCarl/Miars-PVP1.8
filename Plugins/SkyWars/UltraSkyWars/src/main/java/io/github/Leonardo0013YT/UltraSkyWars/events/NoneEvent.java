package io.github.Leonardo0013YT.UltraSkyWars.events;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.GameEvent;

public class NoneEvent extends GameEvent {

    public NoneEvent(UltraSkyWars plugin, int time) {
        this.time = time;
        this.reset = time;
        this.type = "final";
        this.name = "none";
        this.sound = plugin.getConfig().getString("sounds.events." + name);
        this.title = plugin.getLang().get("titles." + name + ".title");
        this.subtitle = plugin.getLang().get("titles." + name + ".subtitle");
    }

    public NoneEvent(NoneEvent e) {
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
    }

    @Override
    public void stop(Game game) {
    }

    @Override
    public void reset() {
        this.time = this.reset;
        this.type = "final";
        this.name = "none";
    }

    @Override
    public NoneEvent clone() {
        return new NoneEvent(this);
    }

}