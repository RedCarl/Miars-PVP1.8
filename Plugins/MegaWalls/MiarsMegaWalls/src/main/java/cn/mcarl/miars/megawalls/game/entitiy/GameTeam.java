package cn.mcarl.miars.megawalls.game.entitiy;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.entity.MNPCs;
import cn.mcarl.miars.megawalls.conf.PluginConfig;
import cn.mcarl.miars.megawalls.game.entitiy.enums.TeamType;
import cn.mcarl.miars.megawalls.game.entitiy.mobs.TeamWither;
import cn.mcarl.miars.megawalls.utils.EntityTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.WitherTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wither;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameTeam {

    /**
     * 队伍的颜色
     */
    private TeamType teamType;

    /**
     * 队伍玩家
     */
    private List<GamePlayer> gamePlayers = new ArrayList<>();

    /**
     * 重生点
     */
    private Location respawn;

    /**
     * 出生点
     */
    private Location spawn;

    /**
     * 凋零生成的位置
     */
    private Location witherLocation;

    /**
     * 凋零实体
     */
    private TeamWither teamWither;

    /**
     * 凋零是否死亡
     */
    private boolean isWitherDead;

    public void spawnWither(GameTeam gameTeam){
        if (!this.witherLocation.getChunk().isLoaded()) {
            this.witherLocation.getChunk().load();
        }

        this.teamWither = new TeamWither(((CraftWorld) Bukkit.getWorld("world")).getHandle());
        this.teamWither.setTeam(gameTeam);
        ((Wither)this.teamWither.getBukkitEntity()).setMaxHealth(PluginConfig.WITHER_HP.get());
        ((Wither)this.teamWither.getBukkitEntity()).setHealth(PluginConfig.WITHER_HP.get());
        EntityTypes.spawnEntity(this.teamWither, this.witherLocation);
    }

    public boolean isInTeam(GamePlayer gamePlayer) {

        return gamePlayer.getTeamType() == this.teamType;
    }

}
