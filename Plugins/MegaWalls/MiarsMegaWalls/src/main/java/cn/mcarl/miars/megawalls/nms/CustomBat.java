package cn.mcarl.miars.megawalls.nms;


import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.utils.ParticleEffect;
import cn.mcarl.miars.megawalls.utils.PlayerUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomBat extends EntityBat implements CustomEntity, Runnable {
   private GamePlayer gamePlayer;
   private int taskId = 0;

   public CustomBat(World world) {
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
   }

   @Override
   public void setGoalTarget(EntityLiving entityliving) {
      this.setGoalTarget(entityliving, TargetReason.CLOSEST_PLAYER, false);
   }

   @Override
   public void setGoalTarget(EntityLiving entityliving, TargetReason reason, boolean fire) {
      if (entityliving instanceof EntityPlayer) {
         GamePlayer gamePlayer = this.getGamePlayer();
         this.follow(gamePlayer.getPlayer());
         if (GameManager.getInstance().getGamePlayerTeam(gamePlayer) == null || !GameManager.getInstance().getGamePlayerTeam(gamePlayer).isInTeam(GamePlayer.get(((EntityPlayer)entityliving).getBukkitEntity().getUniqueId()))) {
            super.setGoalTarget(entityliving, reason, fire);
         }
      }

   }

   @Override
   public void die() {
      Bukkit.getScheduler().cancelTask(this.taskId);
      super.die();
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
   public void run() {
      Iterator var1 = PlayerUtils.getNearbyPlayers(this.getBukkitEntity().getLocation(), 10.0D).iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         if (player != null) {
            GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
            if (gamePlayer != null && !gamePlayer.isSpectator() && !GameManager.getInstance().getGamePlayerTeam(this.gamePlayer).isInTeam(gamePlayer)) {
               this.setGoalTarget(((CraftPlayer)player).getHandle());
            }
         }
      }

      EntityLiving target = this.getGoalTarget();
      if (target != null && target.getBukkitEntity().getLocation().distance(this.getBukkitEntity().getLocation()) <= 3.0D) {
         ParticleEffect.EXPLOSION_HUGE.display(0.0F, 0.0F, 0.0F, 1.0F, 1, this.getBukkitEntity().getLocation().add(0.0D, 1.0D, 0.0D), 30.0D);
         Iterator var5 = this.getNearbyPlayers(this.getBukkitEntity().getLocation(), this.gamePlayer.getPlayer(), 3).iterator();

         while(var5.hasNext()) {
            Player player1 = (Player)var5.next();
            player1.damage(2.0D, this.gamePlayer.getPlayer());
         }

         this.die();
      }

   }

   private List<Player> getNearbyPlayers(Location location, Player player, int radius) {
      List<Player> players = new ArrayList<>();
      GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());

      for (Player other : PlayerUtils.getNearbyPlayers(location, (double) radius)) {
         GamePlayer gameOther = GamePlayer.get(other.getUniqueId());
         if (!gameOther.isSpectator() && !GameManager.getInstance().getGamePlayerTeam(gamePlayer).isInTeam(gameOther) && other.getLocation().distance(location) <= (double) radius) {
            players.add(other);
         }
      }

      return players;
   }

   @Override
   protected Item getLoot() {
      return null;
   }
}
