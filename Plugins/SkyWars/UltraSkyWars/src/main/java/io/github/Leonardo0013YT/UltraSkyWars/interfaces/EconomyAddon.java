package io.github.Leonardo0013YT.UltraSkyWars.interfaces;

import org.bukkit.entity.Player;

public interface EconomyAddon {

    void setCoins(Player p, double amount);

    void addCoins(Player p, double amount);

    void removeCoins(Player p, double amount);

    double getCoins(Player p);

}