package io.github.Leonardo0013YT.UltraSkyWars.modules.mobfriends.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

import java.util.UUID;

public class TargetListener implements Listener {

    private UltraSkyWars plugin;

    public TargetListener(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSplash(PotionSplashEvent e) {
        if (e.getEntity().getShooter() instanceof Witch) {
            Witch w = (Witch) e.getEntity().getShooter();
            if (w.hasMetadata("OWNER") && w.hasMetadata("TEAM")) {
                Player p = Bukkit.getPlayer(UUID.fromString(w.getMetadata("OWNER").get(0).asString()));
                if (p == null) return;
                Game g = plugin.getGm().getGameByPlayer(p);
                if (g == null) return;
                Team t = g.getTeams().get(w.getMetadata("TEAM").get(0).asInt());
                if (t == null) return;
                for (LivingEntity le : e.getAffectedEntities()) {
                    if (le instanceof Player) {
                        Player on = (Player) le;
                        Team to = g.getTeamPlayer(on);
                        if (to != null && to.getId() == t.getId()) {
                            e.getAffectedEntities().remove(le);
                        }
                    }
                }
            }
        }
    }

}