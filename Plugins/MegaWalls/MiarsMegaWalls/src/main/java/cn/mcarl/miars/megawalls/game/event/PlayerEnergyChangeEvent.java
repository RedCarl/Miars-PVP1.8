package cn.mcarl.miars.megawalls.game.event;

import cn.mcarl.miars.megawalls.game.entitiy.GameInfo;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerEnergyChangeEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final GameInfo game;
   private final GamePlayer player;
   private final ChangeReason changeReason;
   private int amount;

   public PlayerEnergyChangeEvent(GameInfo game, GamePlayer player, ChangeReason changeReason, int amount) {
      this.game = game;
      this.player = player;
      this.changeReason = changeReason;
      this.amount = amount;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   @Override
   public HandlerList getHandlers() {
      return handlers;
   }

   public GameInfo getGame() {
      return this.game;
   }

   public GamePlayer getPlayer() {
      return this.player;
   }

   public ChangeReason getChangeReason() {
      return this.changeReason;
   }

   public int getAmount() {
      return this.amount;
   }

   public void setAmount(int amount) {
      this.amount = amount;
   }

   public static enum ChangeReason {
      MELLEE,
      BOW,
      MAGIC;
   }
}
