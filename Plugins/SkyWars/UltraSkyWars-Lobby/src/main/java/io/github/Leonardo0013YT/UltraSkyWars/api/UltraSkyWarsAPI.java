package io.github.Leonardo0013YT.UltraSkyWars.api;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.party.Party;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class UltraSkyWarsAPI {

    private static UltraSkyWars plugin;

    static {
        plugin = UltraSkyWars.get();
    }

    public static Map<String, GameData> getGameData() {
        return plugin.getGm().getGameData();
    }

    public static void sendRedisMessage(String channel, String msg) {
        plugin.getBm().sendMessage(channel, msg);
    }

    public static boolean isInParty(Player p) {
        if (!plugin.getIjm().isParty()) return false;
        return plugin.getIjm().getParty().getPam().isInParty(p);
    }

    public static boolean isPartyLeader(Player p) {
        if (!plugin.getIjm().isParty()) return false;
        return plugin.getIjm().getParty().getPam().isLeader(p);
    }

    public static Party getPartyByPlayer(Player p) {
        return getPartyByPlayer(p.getUniqueId());
    }

    public static Party getPartyByPlayer(UUID uuid) {
        if (!plugin.getIjm().isParty()) {
            return null;
        }
        return plugin.getIjm().getParty().getPam().getPartyByPlayer(uuid);
    }

}