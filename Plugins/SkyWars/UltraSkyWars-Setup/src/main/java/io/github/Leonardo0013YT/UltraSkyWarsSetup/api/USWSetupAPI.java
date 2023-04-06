package io.github.Leonardo0013YT.UltraSkyWarsSetup.api;

import io.github.Leonardo0013YT.UltraSkyWarsSetup.UltraSkyWarsSetup;
import org.bukkit.entity.Player;

public class USWSetupAPI {

    public static boolean isLobbySetup(Player p) {
        return UltraSkyWarsSetup.get().getSm().isSetupInventory(p);
    }

}