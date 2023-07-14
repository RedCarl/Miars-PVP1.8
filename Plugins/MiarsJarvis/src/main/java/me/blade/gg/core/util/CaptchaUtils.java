package me.blade.gg.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaptchaUtils {
  private static List<String> allStr = new ArrayList<>();
  
  static {
    allStr.add("a");
    allStr.add("b");
    allStr.add("c");
    allStr.add("d");
    allStr.add("e");
    allStr.add("f");
    allStr.add("g");
    allStr.add("h");
    allStr.add("i");
    allStr.add("j");
    allStr.add("k");
    allStr.add("l");
    allStr.add("m");
    allStr.add("n");
    allStr.add("o");
    allStr.add("p");
    allStr.add("q");
    allStr.add("r");
    allStr.add("s");
    allStr.add("t");
    allStr.add("u");
    allStr.add("v");
    allStr.add("w");
    allStr.add("x");
    allStr.add("y");
    allStr.add("z");
    allStr.add("_");
    allStr.add("0");
    allStr.add("1");
    allStr.add("2");
    allStr.add("3");
    allStr.add("4");
    allStr.add("5");
    allStr.add("6");
    allStr.add("7");
    allStr.add("8");
    allStr.add("9");
  }
  
  public static String summon() {
    String str = "";
    for (int i = 0; i < 5; i++) {
      str = str + allStr.get((new Random()).nextInt(allStr.size()));
    }
    return str;
  }
}
