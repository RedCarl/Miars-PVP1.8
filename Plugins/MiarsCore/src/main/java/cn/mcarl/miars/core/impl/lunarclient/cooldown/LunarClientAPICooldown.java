package cn.mcarl.miars.core.impl.lunarclient.cooldown;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class LunarClientAPICooldown {

    private final Map<String, cn.mcarl.miars.core.impl.lunarclient.cooldown.LCCooldown> registeredCooldowns = new HashMap<>();

    /**
     * Used to register a persisting cooldown.
     * Ideally 1 {@link cn.mcarl.miars.core.impl.lunarclient.cooldown.LCCooldown} will be created for each task.
     * Create the {@link cn.mcarl.miars.core.impl.lunarclient.cooldown.LCCooldown} once, register it and then send it to each player.
     *
     * @param cooldown The cooldown to register
     */
    public void registerCooldown(cn.mcarl.miars.core.impl.lunarclient.cooldown.LCCooldown cooldown) {
        registeredCooldowns.put(cooldown.getName().toLowerCase(), cooldown);
    }

    /**
     * Used to unregister a persisting cooldown.
     * If a cooldown will no longer be used or in shutdown, unregistering is a good idea.
     *
     * @param cooldown The cooldown to register
     */
    public void unregisterCooldown(cn.mcarl.miars.core.impl.lunarclient.cooldown.LCCooldown cooldown) {
        registeredCooldowns.remove(cooldown.getName().toLowerCase());
    }

    /**
     * Sends a cooldown to a Lunar Client player that has previously been registered.
     * This could be used instead of passing around a {@link cn.mcarl.miars.core.impl.lunarclient.cooldown.LCCooldown} instance.
     *
     * @param player       The player to send a cooldown to
     * @param cooldownName The name of the {@link LCCooldown} that is sent.
     */
    public void sendCooldown(Player player, String cooldownName) {
        String cooldownId = cooldownName.toLowerCase();
        if (!registeredCooldowns.containsKey(cooldownId)) {
            throw new IllegalStateException("Attempted to send a cooldown that isn't registered [" + cooldownName + "]");
        }
        registeredCooldowns.get(cooldownId).send(player);
    }

    public void clearCooldown(Player player, String cooldownName) {
        String cooldownId = cooldownName.toLowerCase();
        if (!registeredCooldowns.containsKey(cooldownId)) {
            throw new IllegalStateException("Attempted to send a cooldown that isn't registered [" + cooldownName + "]");
        }
        registeredCooldowns.get(cooldownId).clear(player);
    }
}
