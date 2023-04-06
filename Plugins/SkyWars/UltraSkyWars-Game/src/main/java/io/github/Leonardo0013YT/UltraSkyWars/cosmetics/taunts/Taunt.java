package io.github.Leonardo0013YT.UltraSkyWars.cosmetics.taunts;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Taunt extends Cosmetic {

    private HashMap<String, TauntType> taunts = new HashMap<>();
    private String title, subtitle, player, none;
    private ItemStack icon;
    private UltraSkyWars plugin;

    public Taunt(UltraSkyWars plugin, String s) {
        super(plugin.getTaunt(), s, "taunts");
        this.plugin = plugin;
        this.title = plugin.getTaunt().get(s + ".title");
        this.subtitle = plugin.getTaunt().get(s + ".subtitle");
        this.player = plugin.getTaunt().get(s + ".player");
        this.none = plugin.getTaunt().get(s + ".none");
        this.icon = Utils.getIcon(plugin.getTaunt(), s);
        ConfigurationSection conf = plugin.getTaunt().getConfig().getConfigurationSection(s + ".taunts");
        for (String d : conf.getKeys(false)) {
            taunts.put(d, new TauntType(d, plugin.getTaunt().getList(s + ".taunts." + d + ".msg")));
        }
        plugin.getCos().setLastPage("Taunt", page);
    }

    public void execute(Player d, Game game) {
        String msg = taunts.get("CONTACT").getRandomMessage();
        String death = d.getName();
        msg = msg.replaceAll("<death>", death);
        for (Player p : game.getPlayers()) {
            p.sendMessage(msg + none);
        }
        for (Player p : game.getSpectators()) {
            p.sendMessage(msg + none);
        }
    }

    public void execute(Player d, EntityDamageEvent.DamageCause cause, Game game) {
        Player k = null;
        if (UltraSkyWars.get().getTgm().hasTag(d)) {
            k = UltraSkyWars.get().getTgm().getTagged(d).getLast();
        }
        if (!game.getPlayers().contains(k)) {
            k = null;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (d != null) {
                    plugin.getVc().getReflection().sendTitle(title, subtitle, 0, 60, 0, d);
                }
            }
        }.runTaskLater(UltraSkyWars.get(), 5L);
        if (k == null) {
            String msg = taunts.getOrDefault(cause.name(), taunts.get("CONTACT")).getRandomMessage();
            String death = d.getName();
            msg = msg.replaceAll("<death>", death);
            for (Player p : game.getCached()) {
                p.sendMessage(plugin.getAdm().parsePlaceholders(p, msg + none));
            }
        } else {
            String msg = taunts.getOrDefault(cause.name(), taunts.get("CONTACT")).getRandomMessage();
            String killer = player.replaceAll("<killer>", k.getName());
            String death = d.getName();
            msg = msg.replaceAll("<killer>", killer).replaceAll("<death>", death);
            for (Player p : game.getCached()) {
                p.sendMessage(plugin.getAdm().parsePlaceholders(p, msg + killer));
            }
        }
    }

    public HashMap<String, TauntType> getTypes() {
        return taunts;
    }

    public String getTitle() {
        return title;
    }

    public String getPlayer() {
        return player;
    }

    public String getNone() {
        return none;
    }

}