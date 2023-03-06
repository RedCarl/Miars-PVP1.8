package cn.mcarl.miars.megawalls.nms;

import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

public interface CustomEntity {
   GamePlayer getGamePlayer();

   void setGamePlayer(GamePlayer var1);

   CraftEntity getBukkitEntity();
}
