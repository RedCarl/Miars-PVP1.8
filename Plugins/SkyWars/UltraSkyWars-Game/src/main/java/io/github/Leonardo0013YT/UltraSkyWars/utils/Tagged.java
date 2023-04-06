package io.github.Leonardo0013YT.UltraSkyWars.utils;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.StatType;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tagged {

    private HashMap<Player, Double> damagers = new HashMap<>();
    private HashMap<Player, Long> timer = new HashMap<>();
    private Player last;
    private Player damaged;
    private Game game;
    private DecimalFormat f = new DecimalFormat("##.#");

    public Tagged(Player damaged, Game game) {
        this.damaged = damaged;
        this.game = game;
    }

    public void addPlayerDamage(Player p, double damage) {
        last = p;
        if (!damagers.containsKey(p)) {
            damagers.put(p, damage);
            timer.put(p, getTime());
            return;
        }
        double d = damagers.get(p);
        damagers.put(p, d + damage);
        timer.put(p, getTime());
    }

    public void removeDamage(double dam) {
        List<Player> to = new ArrayList<>();
        for (Player on : damagers.keySet()) {
            if (timer.get(on) < System.currentTimeMillis()) {
                to.add(on);
                continue;
            }
            if (damagers.get(on) - dam < 0) {
                to.add(on);
                continue;
            }
            damagers.put(on, damagers.get(on) - dam);
        }
        for (Player on : to) {
            timer.remove(on);
            damagers.remove(on);
        }
    }

    public void executeRewards(double maxHealth) {
        List<Player> to = new ArrayList<>();
        for (Player on : damagers.keySet()) {
            if (on == null || !on.isOnline()) continue;
            if (timer.get(on) < System.currentTimeMillis()) {
                to.add(on);
                continue;
            }
            if (on.getName().equals(last.getName())) {
                continue;
            }
            double damage = damagers.get(on);
            double percent = (damage * 100) / maxHealth;
            on.sendMessage(UltraSkyWars.get().getLang().get(on, "assists").replaceAll("<percent>", f.format(percent)).replaceAll("<name>", damaged.getName()));
            SWPlayer up = UltraSkyWars.get().getDb().getSWPlayer(on);
            up.addCoins(UltraSkyWars.get().getCm().getCoinsAssists());
            up.addSouls(UltraSkyWars.get().getCm().getSoulsAssists());
            up.addXp(UltraSkyWars.get().getCm().getXpAssists());
            up.addStat(StatType.ASSISTS, game.getGameType(), 1);
        }
        for (Player on : to) {
            timer.remove(on);
            damagers.remove(on);
        }
        if (last != null) {
            if (!timer.containsKey(last) || !damagers.containsKey(last)) {
                return;
            }
            if (timer.get(last) < System.currentTimeMillis()) {
                timer.remove(last);
                return;
            }
            if (damagers.size() == 1) {
                SWPlayer up = UltraSkyWars.get().getDb().getSWPlayer(last);
                if (up == null) return;
                double percent = (last.getHealth() * 100) / last.getMaxHealth();
                if (percent <= 50 && percent > 25) {
                    up.addStat(StatType.KILL50, game.getGameType(), 1);
                }
                if (percent <= 25 && percent > 5) {
                    up.addStat(StatType.KILL25, game.getGameType(), 1);
                }
                if (percent <= 5 && percent > 1) {
                    up.addStat(StatType.KILL5, game.getGameType(), 1);
                }
            }
        }
    }

    public Player getLast() {
        return last;
    }

    private long getTime() {
        return System.currentTimeMillis() + (10 * 1000);
    }

}