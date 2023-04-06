package cn.mcarl.miars.storage.entity.ffa;

import cn.mcarl.miars.storage.storage.data.practice.FPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.RankScoreDataStorage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FPlayer {

    // 玩家UUID
    private UUID uuid;

    // 玩家名称
    private String name;

    // 击杀人数
    private Long killsCount;

    // 死亡数量
    private Long deathCount;

    public Long getRankScore(Integer season){
        return RankScoreDataStorage.getInstance().getRankScore(
                this.uuid,
                season
        ).getScore();
    }

    public void addKillsCount(){
        this.killsCount++;
        FPlayerDataStorage.getInstance().putFPlayer(this);
    }
    public void addDeathCount(){
        this.deathCount++;
        FPlayerDataStorage.getInstance().putFPlayer(this);
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }
}
