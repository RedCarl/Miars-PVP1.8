package cn.mcarl.miars.megawalls.game.event;

import cn.mcarl.miars.megawalls.game.entitiy.GameInfo;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameOverEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final GameInfo game;

   public GameOverEvent(GameInfo game) {
      this.game = game;
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
}
