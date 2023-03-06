package cn.mcarl.miars.megawalls.game.event;

import cn.mcarl.miars.megawalls.game.entitiy.GameInfo;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private final GameInfo game;

   public GameStartEvent(GameInfo game) {
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
