package io.github.Leonardo0013YT.UltraSkyWars;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class UltraSkyWarsWP extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        final UltraSkyWars plugin = UltraSkyWars.get();
        if (getConfig().getBoolean("enabled")){
            plugin.sendLogMessage("§aWorldParser detected... §eStarting parse in §f10§e seconds.");
            new BukkitRunnable(){
                public void run() {
                    int i = 0;
                    int reset = plugin.getGm().getWorlds().size() + 2;
                    for (final String name : plugin.getGm().getWorlds()){
                        final World w = Bukkit.getWorld(name);
                        new BukkitRunnable(){
                            public void run() {
                                if (w != null){
                                    w.save();
                                    backUpWorld(w);
                                    plugin.sendLogMessage("§2§lCOMPLETED!! §aThe world §e" + name + "§a has been parsed.");
                                } else {
                                    plugin.sendLogMessage("§4§lERROR!! §cThe world§e " + name + "§c couldn't be parsed.");
                                }
                            }
                        }.runTaskLater(plugin, 20 * i);
                        i++;
                    }
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            plugin.sendLogMessage("§2§lPARSER COMPLETED!! §aThe parse has been finished.");
                            Bukkit.shutdown();
                        }
                    }.runTaskLater(plugin, 20 * reset);
                }
            }.runTaskLater(plugin, 20 * 10);
            getConfig().set("enabled", false);
            saveConfig();
        }
    }

    public void backUpWorld(World w) {
        World tp = Bukkit.getWorlds().get(0);
        for (Player on : w.getPlayers()) {
            on.teleport(tp.getSpawnLocation());
        }
        w.save();
        Bukkit.unloadWorld(w, true);
        File source = new File(Bukkit.getWorldContainer(), w.getName());
        File target = new File(Bukkit.getWorldContainer(), "/plugins/UltraSkyWars/maps/" + w.getName());
        copyWorld(source, target);
    }

    public void copyWorld(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock", "Village.dat", "villages.dat"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists()) {
                        target.mkdirs();
                    }
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorld(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                }
            }
        } catch (IOException ignored) {
        }
    }

    @Override
    public void onDisable() {

    }
}