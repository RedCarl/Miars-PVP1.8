package cn.mcarl.miars.megawalls.game.entitiy;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.megawalls.game.entitiy.enums.TeamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayer {
    private UUID uuid;
    private String name;
    private TeamType teamType;

    public void sendMessage(String message){
        Bukkit.getPlayer(this.uuid).sendMessage(ColorParser.parse(message));
    }
    public void sendMessageAll(String message){
        Bukkit.broadcastMessage(ColorParser.parse(message));
    }
}
