package cn.mcarl.miars.storage.utils.jsonmessage.compat;

import org.bukkit.entity.Player;

public interface PlayerConnectionCompat {
    void sendPacket(Object packet, Player... players);
}
