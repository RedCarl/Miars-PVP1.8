package cn.mcarl.miars.megawalls.game.entitiy;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.megawalls.game.entitiy.enums.TeamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
    public void tp(Location location){
        Bukkit.getPlayer(this.uuid).teleport(location);
    }
    public void clearInv(){
        Bukkit.getPlayer(this.uuid).getInventory().clear();
    }
    public boolean isSpectator(){
        return Bukkit.getPlayer(this.uuid).getGameMode() == GameMode.SPECTATOR;
    }

    public void sendTitle(String s,String s1){
        Bukkit.getPlayer(this.uuid).sendTitle(ColorParser.parse(s),ColorParser.parse(s1));
    }

    public boolean isOnline(){
        return Bukkit.getPlayer(this.uuid).isOnline();
    }

    public void givePotionEffect(PotionEffect potionEffect){
        Bukkit.getPlayer(this.uuid).addPotionEffect(potionEffect);
    }
}
