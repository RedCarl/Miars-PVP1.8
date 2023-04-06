package io.github.Leonardo0013YT.UltraSkyWars.events;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.GameEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class TNTEvent extends GameEvent {

    private BukkitTask task;

    public TNTEvent(UltraSkyWars plugin, int time) {
        this.time = time;
        this.reset = time;
        this.task = null;
        this.type = "final";
        this.name = "tnt";
        this.sound = plugin.getConfig().getString("sounds.events." + name);
        this.title = plugin.getLang().get("titles." + name + ".title");
        this.subtitle = plugin.getLang().get("titles." + name + ".subtitle");
    }

    public TNTEvent(TNTEvent e) {
        this.time = e.getReset();
        this.reset = e.getReset();
        this.task = null;
        this.type = e.getType();
        this.name = e.getName();
        this.sound = e.getSound();
        this.title = e.getTitle();
        this.subtitle = e.getSubTitle();
    }

    @Override
    public void start(Game game) {
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player on : game.getPlayers()) {
                    TNTPrimed tntPrimed = on.getWorld().spawn(on.getLocation().clone().add(0, 10, 0), TNTPrimed.class);
                    tntPrimed.setVelocity(new Vector(0, -2, 0));
                }
            }
        }.runTaskTimer(UltraSkyWars.get(), 40, 40);
        for (Player on : game.getCached()) {
            CustomSound.EVENTS_TNT.reproduce(on);
        }
    }

    @Override
    public void stop(Game game) {
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public void reset() {
        this.time = this.reset;
        this.task = null;
        this.type = "final";
        this.name = "tnt";
    }

    @Override
    public TNTEvent clone() {
        return new TNTEvent(this);
    }

}