package cn.mcarl.miars.megawalls.nms;

import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.utils.PlayerUtils;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import java.util.Iterator;

public class CustomCreeper extends EntityCreeper implements CustomEntity, Runnable {
   private GamePlayer gamePlayer;
   private int taskId = 0;

   public CustomCreeper(World world) {
      super(world);
      this.taskId = Bukkit.getScheduler().runTaskTimer(MiarsMegaWalls.getInstance(), this, 0L, 5L).getTaskId();
   }

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
      ((Creeper)this.getBukkitEntity()).setCanPickupItems(false);
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
      Iterator var1 = PlayerUtils.getNearbyPlayers(this.getBukkitEntity().getLocation(), 10.0D).iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();
         if (player != null) {
            GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
            if (gamePlayer != null && !gamePlayer.isSpectator() && !GameManager.getInstance().getGamePlayerTeam(this.getGamePlayer()).isInTeam(gamePlayer)) {
               this.setGoalTarget(((CraftPlayer)player).getHandle());
            }
         }
      }

   }

   @Override
   public void die() {
      Bukkit.getScheduler().cancelTask(this.taskId);
      super.die();
   }
}
