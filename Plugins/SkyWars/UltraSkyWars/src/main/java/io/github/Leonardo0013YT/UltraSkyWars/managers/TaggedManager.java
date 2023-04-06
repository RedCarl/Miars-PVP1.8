package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Tagged;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TaggedManager {

    private HashMap<Player, Tagged> tagged = new HashMap<>();

    public void clear() {
        tagged.clear();
    }

    public void setTag(Player damager, Player damaged, double damage, Game game) {
        if (!tagged.containsKey(damaged)) {
            tagged.put(damaged, new Tagged(damaged, game));
        }
        Tagged tag = tagged.get(damaged);
        tag.addPlayerDamage(damager, damage);
    }

    public void executeRewards(Player death, double maxHealth) {
        if (tagged.containsKey(death)) {
            Tagged tag = tagged.get(death);
            tag.executeRewards(maxHealth);
            tagged.remove(death);
        }
    }

    public void removeTag(Player p) {
        tagged.remove(p);
    }

    public Tagged getTagged(Player p) {
        return tagged.get(p);
    }

    public boolean hasTag(Player p) {
        return tagged.containsKey(p);
    }

}