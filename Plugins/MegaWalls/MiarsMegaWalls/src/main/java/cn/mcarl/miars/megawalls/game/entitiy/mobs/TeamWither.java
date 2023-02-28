package cn.mcarl.miars.megawalls.game.entitiy.mobs;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.game.entitiy.GameInfo;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.entitiy.GameTeam;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.game.manager.GamePlayerManager;
import cn.mcarl.miars.megawalls.utils.PlayerUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.HashMap;
import java.util.Iterator;

public class TeamWither extends EntityWither {
    private final GameInfo game = GameManager.getInstance().getGameInfo();
    private int bp;
    private GameTeam gameTeam = null;
    private boolean warning = false;
    private boolean deepRed = true;
    private final HashMap damagerList = new HashMap();
    public TeamWither(World world) {
        super(world);
    }

    public void setTeam(GameTeam gameTeam) {
        if (this.gameTeam == null) {
            this.gameTeam = gameTeam;
        }
        this.setCustomName(ColorParser.parse(gameTeam.getTeamType().getTeamName() + "队 凋零"));
        this.setCustomNameVisible(true);
    }

    public GameTeam getGameTeam() {
        return this.gameTeam;
    }

    @Override
    public EntityLiving getGoalTarget() {
        for (Player player : PlayerUtils.getNearbyPlayers(this.getBukkitEntity().getLocation(), 10.0D)) {
            if (!CitizensAPI.getNPCRegistry().isNPC(player)) {
                GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(player);
                if (gamePlayer != null && !gamePlayer.isSpectator() && !this.getGameTeam().isInTeam(gamePlayer)) {
                    return ((CraftPlayer) player).getHandle();
                }
            }
        }

        return null;
    }

    @Override
    public void move(double d0, double d1, double d2) {
    }

    @Override
    protected void dropDeathLoot(boolean flag, int i) {
    }

    @Override
    protected void E() {
        int i;
        int j1;
        if (this.cl() > 0) {
            i = this.cl() - 1;
            if (i <= 0) {
                ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 7.0F, false);
                this.world.getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    this.world.createExplosion(this, this.locX, this.locY + (double)this.getHeadHeight(), this.locZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
                }

                j1 = this.world.getServer().getViewDistance() * 16;
                Iterator<EntityPlayer> var4 = MinecraftServer.getServer().getPlayerList().players.iterator();

                label65:
                while(true) {
                    EntityPlayer player;
                    double deltaX;
                    double deltaZ;
                    double distanceSquared;
                    do {
                        if (!var4.hasNext()) {
                            break label65;
                        }

                        player = var4.next();
                        deltaX = this.locX - player.locX;
                        deltaZ = this.locZ - player.locZ;
                        distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                    } while(this.world.spigotConfig.witherSpawnSoundRadius > 0 && distanceSquared > (double)(this.world.spigotConfig.witherSpawnSoundRadius * this.world.spigotConfig.witherSpawnSoundRadius));

                    if (distanceSquared > (double)(j1 * j1)) {
                        double deltaLength = Math.sqrt(distanceSquared);
                        double relativeX = player.locX + deltaX / deltaLength * (double)j1;
                        double relativeZ = player.locZ + deltaZ / deltaLength * (double)j1;
                        player.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1013, new BlockPosition((int)relativeX, (int)this.locY, (int)relativeZ), 0, true));
                    } else {
                        player.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1013, new BlockPosition((int)this.locX, (int)this.locY, (int)this.locZ), 0, true));
                    }
                }
            }

            this.r(i);
            if (this.ticksLived % 10 == 0) {
                this.heal(10.0F, EntityRegainHealthEvent.RegainReason.WITHER_SPAWN);
            }
        } else {
            if (this.getGoalTarget() != null) {
                this.b(0, this.getGoalTarget().getId());
            } else {
                this.b(0, 0);
            }

            if (this.bp > 0) {
                --this.bp;
                if (this.bp == 0 && this.world.getGameRules().getBoolean("mobGriefing")) {
                    i = MathHelper.floor(this.locY);
                    int j = MathHelper.floor(this.locX);
                    j1 = MathHelper.floor(this.locZ);
                    boolean flag = false;
                    int k1 = -1;

                    while(true) {
                        if (k1 > 1) {
                            if (flag) {
                                this.world.a(null, 1012, new BlockPosition(this), 0);
                            }
                            break;
                        }

                        for(int l1 = -1; l1 <= 1; ++l1) {
                            for(int i2 = 0; i2 <= 3; ++i2) {
                                int j2 = j + k1;
                                int k2 = i + i2;
                                int l2 = j1 + l1;
                                BlockPosition blockposition = new BlockPosition(j2, k2, l2);
                                Block block = this.world.getType(blockposition).getBlock();
                                if (block.getMaterial() != Material.AIR && a(block) && !CraftEventFactory.callEntityChangeBlockEvent(this, j2, k2, l2, Blocks.AIR, 0).isCancelled()) {
                                    flag = this.world.setAir(blockposition, true) || flag;
                                }
                            }
                        }

                        ++k1;
                    }
                }
            }

            if (this.ticksLived % 20 == 0) {
                this.heal(1.0F, EntityRegainHealthEvent.RegainReason.REGEN);
            }
        }
    }

    @Override
    public void die() {

        this.getGameTeam().setWitherDead(true);

        super.die();
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (!this.game.getGameWall().isCollapse()) {
            return true;
        } else if (damagesource.equals(DamageSource.GENERIC)) {
            return super.damageEntity(damagesource, f);
        } else {
            if (damagesource.getEntity() != null && damagesource.getEntity().getBukkitEntity() != null && damagesource.getEntity().getBukkitEntity() instanceof CraftPlayer) {
                Player player = (Player)damagesource.getEntity().getBukkitEntity();
                if (CitizensAPI.getNPCRegistry().isNPC(player)) {
                    return true;
                }

                GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(player);
                if (gamePlayer != null && !gamePlayer.isSpectator() && !this.getGameTeam().isInTeam(gamePlayer)) {
                    if (!this.warning) {
                        this.warning = true;
                        this.deepRed = !this.deepRed;

                        Bukkit.getScheduler().runTaskLater(MiarsMegaWalls.getInstance(), () -> {

                        }, 11L);
                        Bukkit.getScheduler().runTaskLater(MiarsMegaWalls.getInstance(), () -> {

                        }, 22L);
                        Bukkit.getScheduler().runTaskLater(MiarsMegaWalls.getInstance(), () -> TeamWither.this.warning = false, 60L);
                    }

                    this.damagerList.put(gamePlayer, (Float) this.damagerList.getOrDefault(gamePlayer, 0.0F) + f / 5.0F);
                    return super.damageEntity(damagesource, f / 5.0F);
                }
            }

            return true;
        }
    }
}
