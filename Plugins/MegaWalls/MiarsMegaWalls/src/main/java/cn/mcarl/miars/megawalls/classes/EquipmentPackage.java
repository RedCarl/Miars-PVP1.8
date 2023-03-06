package cn.mcarl.miars.megawalls.classes;

import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class EquipmentPackage implements Upgradeable {
   private Classes classes;

   public EquipmentPackage(Classes classes) {
      this.classes = classes;
   }

   @Override
   public String getName() {
      return "职业套装";
   }

   @Override
   public int maxedLevel() {
      return 5;
   }

   @Override
   public double getAttribute(int level) {
      return 0.0D;
   }

   @Override
   public void upgrade(GamePlayer gamePlayer) {
   }

   @Override
   public int getPlayerLevel(GamePlayer gamePlayer) {
      return 0;
   }

   @Override
   public Material getIconType() {
      return this.getClasses().getIconType();
   }

   @Override
   public byte getIconData() {
      return this.getClasses().getIconData();
   }

   @Override
   public int getCost(int level) {
      return switch (level) {
         case 2 -> 500;
         case 3 -> 2000;
         case 4 -> 7000;
         case 5 -> 15000;
         default -> 999999;
      };
   }

   public abstract List<ItemStack> getEquipments(int var1);

   public Classes getClasses() {
      return this.classes;
   }
}
