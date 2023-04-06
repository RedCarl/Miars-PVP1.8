package io.github.Leonardo0013YT.UltraSkyWars.interfaces;

public interface Purchasable {

    String getPermission();

    String getAutoGivePermission();

    int getPrice();

    boolean isBuy();

    boolean needPermToBuy();

}