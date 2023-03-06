package cn.mcarl.miars.megawalls.classes;

import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import org.bukkit.Material;

import java.util.List;

public interface Upgradeable {
   String getName();

   int maxedLevel();

   double getAttribute(int var1);

   List<String> getInfo(int var1);

   void upgrade(GamePlayer var1);

   int getPlayerLevel(GamePlayer var1);

   Material getIconType();

   byte getIconData();

   int getCost(int var1);
}
