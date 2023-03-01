package cn.mcarl.miars.megawalls.game.entitiy;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.conf.PluginConfig;
import cn.mcarl.miars.megawalls.game.entitiy.enums.TeamType;
import cn.mcarl.miars.megawalls.game.entitiy.mobs.TeamWither;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.game.manager.GamePlayerManager;
import cn.mcarl.miars.megawalls.utils.EntityTypes;
import cn.mcarl.miars.megawalls.utils.PlayerUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraft.server.v1_8_R3.DamageSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

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
     * 出生点
     */
    private Location spawn;

    /**
     * 凋灵生成的位置
     */
    private Location witherLocation;

    /**
     * 凋灵实体
     */
    private TeamWither teamWither;

    /**
     * 凋灵是否死亡
     */
    private boolean isWitherDead;



    /**
     * 出生点门最小点
     */
    private Location doorMin;


    /**
     * 出生点门最大点
     */
    private Location doorMax;

    /**
     * 门的材质
     */
    private String material;

    /**
     * 城堡最小点
     */
    private Location castleMin;


    /**
     * 城堡最大点
     */
    private Location castleMax;


    public void spawnWither(GameTeam gameTeam){
        if (this.gamePlayers.size()==0){
            this.isWitherDead=true;
            return;
        }

        if (!this.witherLocation.getChunk().isLoaded()) {
            this.witherLocation.getChunk().load();
        }

        this.teamWither = new TeamWither(((CraftWorld) Bukkit.getWorld("world")).getHandle());
        this.teamWither.setTeam(gameTeam);
        ((Wither)this.teamWither.getBukkitEntity()).setMaxHealth(PluginConfig.WITHER_HP.get());
        ((Wither)this.teamWither.getBukkitEntity()).setHealth(PluginConfig.WITHER_HP.get());
        EntityTypes.spawnEntity(this.teamWither, this.witherLocation);

        new BukkitRunnable() {
            private int second = 0;
            @Override
            public void run() {

                GameInfo game = GameManager.getInstance().getGameInfo();
                if (!isWitherDead) {
                    if (this.second == PluginConfig.WITHER_TIME.get()) {

                        if (teamWither.getHealth() >= 3.0F && game.getGameWall().isCollapse()) {
                            teamWither.damageEntity(DamageSource.GENERIC, PluginConfig.WITHER_LOSS.get());
                        }

                        for (Player other : PlayerUtils.getNearbyPlayers(witherLocation, 10.0D)) {
                            GamePlayer gameOther = GamePlayerManager.getInstance().getGamePlayer(other);
                            if (gameOther != null && !gameOther.isSpectator() && isInTeam(gameOther)) {
                                if (game.isWitherFury() && game.getGameWall().isCollapse()){
                                    other.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 80, 2));
                                    other.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, 2));
                                    other.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1));
                                }

                            }
                        }

                        this.second = 0;
                    }

                    if (GameManager.getInstance().getGameInfo().isWitherFury()) {
                        getTeamWither().setCustomName(ColorParser.parse(getTeamType().getTeamName() + "队 凋灵 &l(暴怒中)"));
                    } else {
                        getTeamWither().setCustomName(ColorParser.parse(getTeamType().getTeamName() + "队 凋灵"));
                    }
                }else {
                    cancel();
                }

                ++this.second;
            }
        }.runTaskTimer(MiarsMegaWalls.getInstance(),0,20);

    }

    public boolean isInTeam(GamePlayer gamePlayer) {

        return gamePlayer.getTeamType() == this.teamType;
    }

    public List<GamePlayer> getSurvivePlayers(){
        List<GamePlayer> list = new ArrayList<>();
        for (GamePlayer gamePlayer:this.gamePlayers) {
            if (!gamePlayer.isSpectator() && gamePlayer.isOnline()){
                list.add(gamePlayer);
            }
        }
        return list;
    }

}
