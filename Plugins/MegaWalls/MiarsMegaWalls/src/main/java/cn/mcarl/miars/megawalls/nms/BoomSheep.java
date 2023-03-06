package cn.mcarl.miars.megawalls.nms;

import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.utils.ParticleEffect;
import cn.mcarl.miars.megawalls.utils.PlayerUtils;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import java.util.Iterator;

public class BoomSheep extends EntitySheep implements CustomEntity, Runnable {
   private GamePlayer gamePlayer;

   public BoomSheep(World world) {
      super(world);
      Bukkit.getScheduler().runTaskLater(MiarsMegaWalls.getInstance(), this, 60L);
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
      ((Sheep)this.getBukkitEntity()).setCanPickupItems(false);
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
         }
      }

   }

   @Override
   protected void dropDeathLoot(boolean flag, int i) {
   }

   @Override
   public void run() {
      if (this.getGamePlayer().isOnline() && this.isAlive()) {
         ParticleEffect.EXPLOSION_HUGE.display(0.0F, 0.0F, 0.0F, 1.0F, 1, this.getBukkitEntity().getLocation(), 30.0D);
         this.getBukkitEntity().getWorld().playSound(this.getBukkitEntity().getLocation(), Sound.EXPLODE, 1.0F, 0.0F);
         Iterator var1 = PlayerUtils.getNearbyPlayers(this.getBukkitEntity().getLocation(), 5.0D).iterator();

         while(var1.hasNext()) {
            Player player = (Player)var1.next();
            if (player != null) {
               GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
               if (gamePlayer != null && !gamePlayer.isSpectator() && !GameManager.getInstance().getGamePlayerTeam(this.getGamePlayer()).isInTeam(gamePlayer)) {
                  player.damage(4.0D, this.getGamePlayer().getPlayer());
               }
            }
         }

         this.die();
      }

   }
}
