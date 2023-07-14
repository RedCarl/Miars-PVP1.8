package cn.mcarl.miars.practice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayer {

    private UUID uuid;
    private String name;
    private double health;
    private double foodLevel;

    private Collection<PotionEffect> activePotionEffects;

    public static GamePlayer get(Player player){
        return                                     new GamePlayer(
                player.getUniqueId(),
                player.getName(),
                player.getHealth(),
                player.getFoodLevel(),
                player.getActivePotionEffects()
        );
    }

    public void sendTitle(String s,String ss){
        getPlayer().sendTitle(s,ss);
    }
    public void clearInv(){
        Player p = getPlayer();
        if (p!=null){
            p.getInventory().clear();
            p.getInventory().setItem(39,null);
            p.getInventory().setItem(38,null);
            p.getInventory().setItem(37,null);
            p.getInventory().setItem(36,null);
        }
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(this.uuid);
    }
}
