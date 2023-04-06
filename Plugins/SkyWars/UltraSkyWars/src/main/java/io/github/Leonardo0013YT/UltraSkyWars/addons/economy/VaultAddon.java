package io.github.Leonardo0013YT.UltraSkyWars.addons.economy;

import io.github.Leonardo0013YT.UltraSkyWars.interfaces.EconomyAddon;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultAddon implements EconomyAddon {

    private Economy econ;

    public VaultAddon() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            econ = rsp.getProvider();
        }
    }

    public void setCoins(Player p, double amount) {
        if (econ != null) {
            double now = econ.getBalance(p);
            econ.withdrawPlayer(p, now);
            econ.depositPlayer(p, amount);
        }
    }

    public void addCoins(Player p, double amount) {
        if (econ != null) {
            econ.depositPlayer(p, amount);
        }
    }

    public void removeCoins(Player p, double amount) {
        if (econ != null) {
            econ.withdrawPlayer(p, amount);
        }
    }

    public double getCoins(Player p) {
        if (econ != null) {
            return econ.getBalance(p);
        }
        return 0;
    }

}