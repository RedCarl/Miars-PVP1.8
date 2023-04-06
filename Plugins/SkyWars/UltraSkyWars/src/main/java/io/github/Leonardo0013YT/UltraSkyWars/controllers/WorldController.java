package io.github.Leonardo0013YT.UltraSkyWars.controllers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.WorldEditUtils_Old;
import io.github.Leonardo0013YT.UltraSkyWars.calls.CallBackAPI;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.WorldEdit;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.io.*;
import java.util.*;

public class WorldController {

    private final String clear;
    private WorldEdit edit;

    public WorldController() {
        UltraSkyWars plugin = UltraSkyWars.get();
        clear = plugin.getConfig().getString("schemaToClearLobby");
        edit = new WorldEditUtils_Old(plugin);
    }

    public void clearLobby(Location lobby) {
        edit.paste(lobby, clear, true, (b) -> {
        });
    }

    public void deleteWorld(World w, CallBackAPI<World> done) {
        UltraSkyWars plugin = UltraSkyWars.get();
        String name = w.getName();
        w.getPlayers().forEach(p -> {
            if (plugin.getMainLobby() != null) {
                plugin.getGm().handleTeleport(plugin.getMainLobby(), "MainLobby", false, 0, 0, 0, p);
            } else {
                World wo = Bukkit.getWorlds().get(0);
                if (wo != null) {
                    p.teleport(wo.getSpawnLocation());
                } else {
                    p.kickPlayer(plugin.getLang().get("messages.noMapReset"));
                }
            }
        });
        if (!w.getPlayers().isEmpty()) {
            w.getPlayers().forEach(p -> p.sendMessage(plugin.getLang().get("messages.noMapReset")));
        }
        Bukkit.unloadWorld(w, false);
        done.done(resetWorld(name));
    }

    public void deleteWorld(String name) {
        File path = new File(Bukkit.getWorldContainer(), name);
        deleteDirectory(path);
    }

    public World resetWorld(String name) {
        File target = new File(Bukkit.getWorldContainer(), name);
        deleteDirectory(target);
        File source = new File(UltraSkyWars.get().getDataFolder(), "maps/" + name);
        copyWorld(source, target);
        return createEmptyWorld(name);
    }

    public World createEmptyWorld(String name) {
        WorldCreator wc = new WorldCreator(name);
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.FLAT);
        wc.generator(getChunkGenerator());
        World w = wc.createWorld();
        w.setDifficulty(Difficulty.NORMAL);
        w.setSpawnFlags(true, true);
        w.setPVP(true);
        w.setStorm(false);
        w.setThundering(false);
        w.setWeatherDuration(Integer.MAX_VALUE);
        w.setTicksPerAnimalSpawns(1);
        w.setTicksPerMonsterSpawns(1);
        w.setAutoSave(false);
        w.setGameRuleValue("mobGriefing", "true");
        w.setGameRuleValue("doFireTick", "false");
        w.setGameRuleValue("showDeathMessages", "false");
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setGameRuleValue("randomTickSpeed", "false");
        w.setSpawnLocation(0, 75, 0);
        w.getWorldBorder().reset();
        return w;
    }

    public void backUpWorld(World w, String name) {
        World tp = Bukkit.getWorlds().get(0);
        for (Player on : w.getPlayers()) {
            on.teleport(tp.getSpawnLocation());
        }
        w.save();
        Bukkit.unloadWorld(w, true);
        File source = new File(Bukkit.getWorldContainer(), w.getName());
        File target = new File(UltraSkyWars.get().getDataFolder(), "maps/" + name);
        copyWorld(source, target);
    }

    public void copyWorld(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock", "Village.dat", "villages.dat"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        target.mkdirs();
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
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException ignored) {
        }
    }

    public void deleteDirectory(File source) {
        ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock", "Village.dat", "villages.dat"));
        if (!source.exists()) return;
        for (File f : source.listFiles()) {
            if (f == null) continue;
            if (!ignore.contains(f.getName())) {
                if (f.isDirectory()) {
                    deleteDirectory(f);
                } else {
                    f.delete();
                }
            }
        }
    }

    public ChunkGenerator getChunkGenerator() {
        return new ChunkGenerator() {
            @Override
            public List<BlockPopulator> getDefaultPopulators(World world) {
                return Collections.emptyList();
            }

            @Override
            public boolean canSpawn(World world, int x, int z) {
                return true;
            }

            @Override
            public byte[] generate(World world, Random random, int x, int z) {
                return new byte[32768];
            }

            @Override
            public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
                ChunkData chunk = createChunkData(world);
                for (int X = 0; X < 16; X++) {
                    for (int Z = 0; Z < 16; Z++) {
                        for (int Y = 0; Y < 16; Y++) {
                            chunk.setBlock(X, Y, Z, Material.AIR);
                        }
                    }
                }
                return chunk;
            }

            @Override
            public Location getFixedSpawnLocation(World world, Random random) {
                return new Location(world, 0.0D, 75, 0.0D);
            }
        };
    }

    public WorldEdit getEdit() {
        return edit;
    }
}