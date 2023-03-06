package cn.mcarl.miars.megawalls.utils;

import java.text.DecimalFormat;

public class StringUtils {
   private static final DecimalFormat coinsFormat = new DecimalFormat("#,###");

   public static String formattedCoins(int coins) {
      return coinsFormat.format(coins);
   }

   public static String upgradeBar(int level, int max) {
      StringBuilder sb = new StringBuilder("&8");

      for(int i = 0; i < max; ++i) {
         if (i >= level) {
            sb.append("▒");
         } else {
            sb.append("█");
         }
      }

      return sb.toString();
   }

   public static String percent(double value) {
      DecimalFormat decimalFormat = new DecimalFormat("0%");
      return decimalFormat.format(value);
   }

   public static String level(int level) {
      return switch (level) {
         case 2 -> "II";
         case 3 -> "III";
         case 4 -> "IV";
         case 5 -> "V";
         default -> "I";
      };
   }

   public static String formatLongTime(long time) {
      int hour = (int)Math.floor((double)(time / 3600000L));
      int min = (int)Math.floor((double)(time / 60000L));
      return hour > 0 ? hour + "小时" : min + "分钟";
   }
}
