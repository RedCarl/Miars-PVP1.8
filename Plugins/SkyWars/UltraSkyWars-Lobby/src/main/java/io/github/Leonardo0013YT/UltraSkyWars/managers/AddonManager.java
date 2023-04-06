package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.addons.PlaceholderAPIAddon;
import io.github.Leonardo0013YT.UltraSkyWars.addons.VaultAddon;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.HologramAddon;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.NametagAddon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AddonManager {

    private UltraSkyWars plugin;
    private VaultAddon vault;
    private HologramAddon ha;
    private NametagAddon tag;
    private PlaceholderAPIAddon placeholder;
    public AddonManager(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        if (plugin.getCm().isLeaderheads()) {
            plugin.getConfig().set("addons.leaderheads", false);
            plugin.saveConfig();
            plugin.getCm().reload();
        }
        if (plugin.getCm().isPlaceholdersAPI()) {
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                placeholder = new PlaceholderAPIAddon();
                plugin.sendLogMessage("Hooked into §aPlaceholderAPI§e!");
            } else {
                plugin.getConfig().set("addons.placeholdersAPI", false);
                plugin.saveConfig();
                plugin.getCm().reload();
            }
        }
        if (plugin.getCm().isVault()) {
            if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                vault = new VaultAddon();
                plugin.sendLogMessage("Hooked into §aVault§e!");
            } else {
                plugin.getConfig().set("addons.vault", false);
                plugin.saveConfig();
                plugin.getCm().reload();
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
//                if (plugin.getCm().isHolograms()) {
//                    if (Bukkit.getPluginManager().isPluginEnabled("Holograms")) {
//                        remove();
//                        ha = new HologramsAddon();
//                        plugin.sendLogMessage("Hooked into §aHolograms§e!");
//                    } else {
//                        plugin.getConfig().set("addons.holograms", false);
//                        plugin.saveConfig();
//                        plugin.getCm().reload();
//                    }
//                    reloadHologram();
//                }
//                if (plugin.getCm().isHolographicdisplays()) {
//                    if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
//                        remove();
//                        ha = new HolographicDisplaysAddon();
//                        plugin.sendLogMessage("Hooked into §aHolographicDisplays§e!");
//                    } else {
//                        plugin.getConfig().set("addons.holographicdisplays", false);
//                        plugin.saveConfig();
//                        plugin.getCm().reload();
//                    }
//                    reloadHologram();
//                }
            }
        }.runTaskLater(plugin, 80);
    }

    public void reloadHologram() {
        if (plugin.getIjm().isSoulWellInjection()) {
            plugin.getIjm().getSoulwell().getSwm().reload();
        }
        if (plugin.getIjm().isCubeletsInjection()) {
            plugin.getIjm().getCubelets().getCbm().reload();
        }
    }

    public void addCoins(Player p, double amount) {
//        if (plugin.getCm().isVault()) {
//            vault.addCoins(p, amount);
//        } else if (plugin.getCm().isPlayerpoints()) {
//            points.addCoins(p, amount);
//        } else if (plugin.getCm().isCoins()) {
//            coins.addCoins(p, amount);
//        } else {
//            SWPlayer sw = plugin.getDb().getSWPlayer(p);
//            if (sw == null) return;
//            sw.addCoins((int) amount);
//        }
//        Utils.updateSB(p);
    }

    public void setCoins(Player p, double amount) {
//        if (plugin.getCm().isVault()) {
//            vault.setCoins(p, amount);
//        } else if (plugin.getCm().isPlayerpoints()) {
//            points.setCoins(p, amount);
//        } else if (plugin.getCm().isCoins()) {
//            coins.setCoins(p, amount);
//        } else {
//            SWPlayer sw = plugin.getDb().getSWPlayer(p);
//            if (sw == null) return;
//            sw.setCoins((int) amount);
//        }
//        Utils.updateSB(p);
    }

    public void removeCoins(Player p, double amount) {
//        if (plugin.getCm().isVault()) {
//            vault.removeCoins(p, amount);
//        } else if (plugin.getCm().isPlayerpoints()) {
//            points.removeCoins(p, amount);
//        } else if (plugin.getCm().isCoins()) {
//            coins.removeCoins(p, amount);
//        } else {
//            SWPlayer sw = plugin.getDb().getSWPlayer(p);
//            if (sw == null) return;
//            sw.removeCoins((int) amount);
//        }
//        Utils.updateSB(p);
    }

    public double getCoins(Player p) {
//        if (plugin.getCm().isVault()) {
//            return vault.getCoins(p);
//        } else if (plugin.getCm().isPlayerpoints()) {
//            return points.getCoins(p);
//        } else if (plugin.getCm().isCoins()) {
//            return coins.getCoins(p);
//        } else {
//            SWPlayer sw = plugin.getDb().getSWPlayer(p);
//            if (sw == null) {
//                return 0;
//            }
//            return sw.getCoins();
//        }
        return 0;
    }

    public void createHologram(Location spawn, List<String> lines) {
        if (ha != null) {
            ha.createHologram(spawn, lines);
        }
    }

    public void createHologram(Location spawn, List<String> lines, ItemStack item) {
        if (ha != null) {
            ha.createHologram(spawn, lines, item);
        }
    }

    public void remove() {
        if (ha != null) {
            ha.remove();
        }
    }

    public void deleteHologram(Location spawn) {
        if (ha != null) {
            ha.deleteHologram(spawn);
        }
    }

    public boolean hasHologram(Location spawn) {
        if (ha != null) {
            return ha.hasHologram(spawn);
        }
        return false;
    }

    public void addPlayerNameTag(Player p) {
        if (tag != null) {
            tag.addPlayerNameTag(p);
        }
    }

    public void resetPlayerNameTag(Player p) {
        if (tag != null) {
            tag.resetPlayerNameTag(p);
        }
    }

    public String getPlayerPrefix(Player p) {
        if (tag != null) {
            return tag.getPrefix(p);
        }
        return "";
    }

    public String getPlayerSuffix(Player p) {
        if (tag != null) {
            return tag.getSuffix(p);
        }
        return "";
    }

    public String parsePlaceholders(Player p, String value) {
        if (plugin.getCm().isPlaceholdersAPI()) {
            return placeholder.parsePlaceholders(p, value);
        }
        return value;
    }
    public boolean hasHologramPlugin() {
        return !(ha == null);
    }

}