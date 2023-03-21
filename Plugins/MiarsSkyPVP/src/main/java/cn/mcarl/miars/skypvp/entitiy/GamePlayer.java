package cn.mcarl.miars.skypvp.entitiy;

import cn.mcarl.miars.core.utils.MiarsUtil;
import cn.mcarl.miars.skypvp.utils.PlayerUtils;
import cn.mcarl.miars.storage.entity.skypvp.SPlayer;
import cn.mcarl.miars.storage.storage.data.skypvp.SkyPVPDataStorage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayer {
    private UUID uuid;
    private SPlayer sPlayer;
    public GamePlayer(Player player){
        this.uuid = player.getUniqueId();
        this.sPlayer = SkyPVPDataStorage.getInstance().getSPlayer(player);
    }
    public static GamePlayer get(Player player){
        return new GamePlayer(player);
    }
    public double getKb(){
        return PlayerUtils.getPlayerKD(this.sPlayer);
    }
    public void addKillsCount(){
        this.sPlayer.setKillsCount(this.getSPlayer().getKillsCount()+1);
        SkyPVPDataStorage.getInstance().putSPlayer(sPlayer);
    }
    public void addDeathCount(){
        this.sPlayer.setDeathCount(this.getSPlayer().getDeathCount()+1);
        SkyPVPDataStorage.getInstance().putSPlayer(sPlayer);
    }
    public void addExp(Long i){
        this.sPlayer.setExp(this.getSPlayer().getExp()+i);
        SkyPVPDataStorage.getInstance().putSPlayer(sPlayer);
    }
    public void addCoin(Long i){
        this.sPlayer.setCoin(this.getSPlayer().getCoin()+i);
        SkyPVPDataStorage.getInstance().putSPlayer(sPlayer);
    }
    public Integer getLevel(){
        int level = 0;

        long exp = this.sPlayer.getExp();
        int next = 0;

        while (exp>=next){
            exp=exp - next;
            level++;
            next = level*200;
        }

        return level;
    }
    public String getNextLevel(){
        int level = 0;

        long exp = this.sPlayer.getExp();
        int next = 0;

        while (exp>=next){
            exp=exp - next;
            level++;
            next = level*200;
        }

        int o = MiarsUtil.accuracy(exp,next,0)/10;

        StringBuilder s = new StringBuilder("&8[");

        if (o!=0){
            s.append("&b");
        }
        s.append("■".repeat(Math.max(0, o)));

        if (o!=10){
            s.append("&7");
        }
        s.append("■".repeat(Math.max(0, 10 - o)));
        s.append("&8]");


        return String.valueOf(s);
    }

    public String getLevelString(){
        return "&e["+getLevel()+"]";
    }
}
