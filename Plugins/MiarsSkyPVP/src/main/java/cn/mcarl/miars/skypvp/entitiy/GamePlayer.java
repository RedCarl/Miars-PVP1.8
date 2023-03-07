package cn.mcarl.miars.skypvp.entitiy;

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
}
