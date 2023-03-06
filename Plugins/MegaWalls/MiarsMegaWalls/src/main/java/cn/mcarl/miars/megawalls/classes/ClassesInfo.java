package cn.mcarl.miars.megawalls.classes;

public class ClassesInfo {
   public static enum Difficulty {
      ONE("&a●&7●●●", 1),
      TWO("&e●●&7●●", 2),
      THREE("&c●●●&7●", 3),
      FOUR("&4●●●●", 4);

      private String text;
      private int priority;

      private Difficulty(String text, int priority) {
         this.text = text;
         this.priority = priority;
      }

      public String getText() {
         return this.text;
      }

      public int getPriority() {
         return this.priority;
      }
   }

   public static enum Orientation {
      WARRIOR("&2战士","&7暂无介绍..."),
      HURT("&c伤害","&7暂无介绍..."),
      TANK("&1坦克","&7暂无介绍..."),
      ASSIST("&d辅助","&7暂无介绍..."),
      AGILITY("&b敏捷","&7暂无介绍..."),
      CHARGER("&4冲锋者","&7暂无介绍..."),
      REMOTE("&3远程","&7暂无介绍..."),
      CONTROL("&6控场","&7暂无介绍...");

      private String text;
      private String info;

      private Orientation(String text,String info) {
         this.text = text;
         this.info = info;
      }

      public String getText() {
         return this.text;
      }
      public String getInfo() {
         return this.info;
      }
   }
}
