package cn.mcarl.miars.megawalls.nms;

import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.utils.PlayerUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class ShamanWolf extends EntityWolf implements CustomEntity, Runnable {
   private GamePlayer gamePlayer;
   private int taskId = 0;

   public ShamanWolf(World world) {
      super(world);
      this.taskId = Bukkit.getScheduler().runTaskTimer(MiarsMegaWalls.getInstance(), this, 0L, 5L).getTaskId();
   }

   @Override
   public GamePlayer getGamePlayer() {
      if (this.gamePlayer != null) {
         return this.gamePlayer;
      } else {
         String name = ChatColor.stripColor(this.getCustomName());
         Player player = Bukkit.getPlayer(name);
         if (player == null) {
            return null;
         } else {
            this.gamePlayer = GamePlayer.get(player.getUniqueId());
            return this.gamePlayer;
         }
      }
   }

   @Override
   public void setGamePlayer(GamePlayer gamePlayer) {
      this.gamePlayer = gamePlayer;
      this.setCustomName(gamePlayer.getDisplayName(null));
      this.setCustomNameVisible(true);
      ((Wolf)this.getBukkitEntity()).setCanPickupItems(false);
      ((Wolf)this.getBukkitEntity()).setMaxHealth(14.0D);
      ((Wolf)this.getBukkitEntity()).setHealth(14.0D);
   }

   @Override
   public void setGoalTarget(EntityLiving entityliving) {
      this.setGoalTarget(entityliving, TargetReason.CLOSEST_PLAYER, false);
   }

   @Override
   public void setGoalTarget(EntityLiving entityliving, TargetReason reason, boolean fire) {
      if (entityliving instanceof EntityPlayer) {
         GamePlayer gamePlayer = this.getGamePlayer();
         if (GameManager.getInstance().getGamePlayerTeam(gamePlayer) == null || !GameManager.getInstance().getGamePlayerTeam(gamePlayer).isInTeam(GamePlayer.get(((EntityPlayer)entityliving).getBukkitEntity().getUniqueId()))) {
            super.setGoalTarget(entityliving, reason, fire);
            if (!this.isTamed()) {
               this.setAngry(true);
            }
         }
      }

   }

   public void follow(Player player) {
      if (player != null) {
         this.getNavigation().a(2.0F);
         Location targetLocation = player.getLocation();
         PathEntity path = this.getNavigation().a(targetLocation.getX() + 1.0D, targetLocation.getY(), targetLocation.getZ() + 1.0D);

         try {
            int distance = (int)Bukkit.getPlayer(player.getName()).getLocation().distance(this.getBukkitEntity().getLocation());
            if (distance > 10 && this.valid && player.isOnGround()) {
               this.setLocation(targetLocation.getBlockX(), targetLocation.getBlockY(), targetLocation.getBlockZ(), 0.0F, 0.0F);
            }

            if (path != null && (double)distance > 3.3D) {
               double speed = 1.05D;
               this.getNavigation().a(path, speed);
               this.getNavigation().a(speed);
            }
         } catch (IllegalArgumentException var7) {
            this.setLocation(targetLocation.getBlockX(), targetLocation.getBlockY(), targetLocation.getBlockZ(), 0.0F, 0.0F);
         }
      }

   }

   @Override
   public void die() {
      Bukkit.getScheduler().cancelTask(this.taskId);
      super.die();
   }

   @Override
   public void run() {

      for (Player player : PlayerUtils.getNearbyPlayers(this.getBukkitEntity().getLocation(), 10.0D)) {
         if (player != null) {
            GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
            if (gamePlayer != null && !gamePlayer.isSpectator() && !GameManager.getInstance().getGamePlayerTeam(this.getGamePlayer()).isInTeam(gamePlayer)) {
               this.setGoalTarget(((CraftPlayer) player).getHandle());
            }
         }
      }

   }
}
