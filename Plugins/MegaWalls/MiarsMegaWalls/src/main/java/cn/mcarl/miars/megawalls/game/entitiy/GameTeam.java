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
import org.bukkit.World;
import org.bukkit.block.Block;
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
    
    private TeamType teamType;
    private List<GamePlayer> gamePlayers = new ArrayList<>();
    private Location spawn;

    public GameTeam(TeamType teamType,
                    List<GamePlayer> gamePlayers,
                    Location spawn,
                    Location witherLocation,
                    TeamWither teamWither,
                    boolean isWitherDead,
                    Location castleMin,
                    Location castleMax,
                    Location doorMin,
                    Location doorMax,
                    String material,
                    boolean collapse
    ) {
        this.teamType = teamType;
        this.gamePlayers = gamePlayers;
        this.spawn = spawn;
        this.witherLocation = witherLocation;
        this.teamWither = teamWither;
        this.isWitherDead = isWitherDead;
        this.castleMin = castleMin;
        this.castleMax = castleMax;
        this.doorMin = doorMin;
        this.doorMax = doorMax;
        this.material = material;
        this.collapse = collapse;
        
        if (doorMin != null && doorMax != null && doorMin.getWorld().getName().equals(doorMax.getWorld().getName())) {
            this.world = doorMin.getWorld();
            this.setMinMax(doorMin, doorMax);
            this.loadBlocks();
        }
    }

    private Location witherLocation;
    private TeamWither teamWither;
    private boolean isWitherDead;
    private Location castleMin;
    private Location castleMax;

    
    
    private Location doorMin;
    private Location doorMax;
    private String material;
    private boolean collapse;
    private World world;
    private final List<Block> blocks = new ArrayList<>();


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
 
    private void setMinMax(Location pos1, Location pos2) {
        this.doorMin = this.getMinimumCorner(pos1, pos2);
        this.doorMax = this.getMaximumCorner(pos1, pos2);
    }

    private Location getMinimumCorner(Location pos1, Location pos2) {
        return new Location(this.world, Math.min(pos1.getBlockX(), pos2.getBlockX()), Math.min(pos1.getBlockY(), pos2.getBlockY()), Math.min(pos1.getBlockZ(), pos2.getBlockZ()));
    }

    private Location getMaximumCorner(Location pos1, Location pos2) {
        return new Location(this.world, Math.max(pos1.getBlockX(), pos2.getBlockX()), Math.max(pos1.getBlockY(), pos2.getBlockY()), Math.max(pos1.getBlockZ(), pos2.getBlockZ()));
    }

    public List<Block> getBlocks() {
        return this.blocks;
    }

    private void loadBlocks() {

        for (int y = this.doorMin.getBlockY(); y <= this.doorMax.getBlockY(); y++) {
            for (int x = this.doorMin.getBlockX(); x <= this.doorMax.getBlockX() ; x++) {
                Location loc = new Location(this.world, x, y, doorMin.getBlockZ());
                if (loc.getBlock().getType() == Material.getMaterial(this.material)) {
                    this.blocks.add(loc.getBlock());
                }
            }

            for (int z = this.doorMin.getBlockZ(); z <= this.doorMax.getBlockZ() ; z++) {
                Location loc = new Location(this.world, doorMin.getX(), y, z);
                if (loc.getBlock().getType() == Material.getMaterial(this.material)) {
                    this.blocks.add(loc.getBlock());
                }
            }

            for (int x = this.doorMax.getBlockX(); x >= this.doorMin.getBlockX() ; x--) {
                Location loc = new Location(this.world, x, y, doorMax.getBlockZ());
                if (loc.getBlock().getType() == Material.getMaterial(this.material)) {
                    this.blocks.add(loc.getBlock());
                }
            }

            for (int z = this.doorMax.getBlockZ(); z >= this.doorMin.getBlockZ() ; z--) {
                Location loc = new Location(this.world, doorMax.getBlockX(), y, z);
                if (loc.getBlock().getType() == Material.getMaterial(this.material)) {
                    this.blocks.add(loc.getBlock());
                }
            }
        }

    }

    public void collapse() {
        if (!this.collapse) {
            this.collapse = true;

            for (Block block : this.blocks) {
                this.world.getBlockAt(block.getLocation()).setType(Material.AIR);
            }
        }
    }
}
