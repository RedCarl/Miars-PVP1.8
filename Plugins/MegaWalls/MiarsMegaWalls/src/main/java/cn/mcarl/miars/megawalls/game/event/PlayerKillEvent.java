package cn.mcarl.miars.megawalls.game.event;

import cn.mcarl.miars.megawalls.game.entitiy.GameInfo;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKillEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private GameInfo game;
   private GamePlayer killer;
   private GamePlayer player;
   private boolean finalKill;

   public PlayerKillEvent(GameInfo game, GamePlayer killer, GamePlayer player) {
      this(game, killer, player, false);
   }

   public PlayerKillEvent(GameInfo game, GamePlayer killer, GamePlayer player, boolean finalKill) {
      this.game = null;
      this.killer = null;
      this.player = null;
      this.finalKill = false;
      this.game = game;
      this.killer = killer;
      this.player = player;
      this.finalKill = finalKill;
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

   public GamePlayer getKiller() {
      return this.killer;
   }

   public GamePlayer getPlayer() {
      return this.player;
   }

   public boolean isFinalKill() {
      return this.finalKill;
   }
}
