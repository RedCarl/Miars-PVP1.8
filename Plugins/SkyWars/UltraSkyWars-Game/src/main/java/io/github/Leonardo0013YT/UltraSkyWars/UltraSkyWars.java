package io.github.Leonardo0013YT.UltraSkyWars;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import io.github.Leonardo0013YT.UltraSkyWars.cmds.LeaveCMD;
import io.github.Leonardo0013YT.UltraSkyWars.cmds.SkyWarsCMD;
import io.github.Leonardo0013YT.UltraSkyWars.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.controllers.ChestController;
import io.github.Leonardo0013YT.UltraSkyWars.controllers.VersionController;
import io.github.Leonardo0013YT.UltraSkyWars.controllers.WorldController;
import io.github.Leonardo0013YT.UltraSkyWars.data.MongoDBDatabase;
import io.github.Leonardo0013YT.UltraSkyWars.data.MySQLDatabase;
import io.github.Leonardo0013YT.UltraSkyWars.enums.GameType;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Database;
import io.github.Leonardo0013YT.UltraSkyWars.listeners.*;
import io.github.Leonardo0013YT.UltraSkyWars.listeners.bungeemode.GameListener;
import io.github.Leonardo0013YT.UltraSkyWars.listeners.bungeemode.GameMenuListener;
import io.github.Leonardo0013YT.UltraSkyWars.managers.*;
import io.github.Leonardo0013YT.UltraSkyWars.menus.GameMenu;
import io.github.Leonardo0013YT.UltraSkyWars.menus.UltraInventoryMenu;
import io.github.Leonardo0013YT.UltraSkyWars.placeholders.Placeholders;
import io.github.Leonardo0013YT.UltraSkyWars.utils.MetricsLite;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Randomizer;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Getter
public class UltraSkyWars extends JavaPlugin {

    private static UltraSkyWars instance;
    private static Gson gson;
    private Settings sounds, chestType, migrators, join, arenas, lang, menus, chests, balloon, glass, killeffect, killsound, kits, parting, taunt, trail, windance, wineffect, levels, rewards;
    private ItemManager im;
    private ConfigManager cm;
    private UltraInventoryMenu uim;
    private WorldController wc;
    private ChestController cc;
    private VersionController vc;
    private GameManager gm;
    private KitManager km;
    private Location mainLobby, topKills, topWins, topDeaths, topCoins, topElo;
    private Database db;
    private TaggedManager tgm;
    private ScoreboardManager sb;
    private MultiplierManager mm;
    private AddonManager adm;
    private BungeeManager bm;
    private ChestManager ctm;
    private CosmeticManager cos;
    private ShopManager shm;
    private LevelManager lvl;
    private GameMenu gem;
    private TaskManager tsm;
    private Randomizer randomizer;
    private InjectionManager ijm;
    private ArrayList<Entity> entities = new ArrayList<>();
    private boolean debugMode, stop = false;

    public static UltraSkyWars get() {
        return instance;
    }

    public static Gson getGson() {
        return gson;
    }

    @Override
    public void onEnable() {
        instance = this;
        gson = new Gson();
        if (!getServer().getPluginManager().isPluginEnabled("WorldEdit") || !getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit")) {
            Bukkit.getConsoleSender().sendMessage("§b§lUltraSkyWars Plugin §cNeed §eWorldEdit §c& §eFastAsyncWorldEdit§c.");
            Bukkit.getScheduler().cancelTasks(this);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getConfig().options().copyDefaults(true);
        vc = new VersionController(this);
        saveConfig();
        loadMainLobby();
        updateDirectories();
        debugMode = getConfig().getBoolean("debugMode");
        getServer().getPluginManager().registerEvents(new GameListener(this), this);
        getServer().getPluginManager().registerEvents(new GameMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new SpecialListener(this), this);
        getServer().getPluginManager().registerEvents(new SpectatorListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
        getServer().getPluginManager().registerEvents(new TeleportFixThree(this), this);
        getServer().getPluginManager().registerEvents(new GeneralListener(this), this);
        db = new MySQLDatabase(this);
        balloon = new Settings("cosmetics/balloon", false);
        glass = new Settings("cosmetics/glass", false);
        killeffect = new Settings("cosmetics/killeffect", true);
        killsound = new Settings("cosmetics/killsound", false);
        parting = new Settings("cosmetics/parting", false);
        taunt = new Settings("cosmetics/taunt", false);
        trail = new Settings("cosmetics/trail", false);
        windance = new Settings("cosmetics/windance", true);
        wineffect = new Settings("cosmetics/wineffect", true);
        chestType = new Settings("chestType", false);
        migrators = new Settings("migrators", true);
        join = new Settings("customjoin", false);
        arenas = new Settings("arenas", false);
        lang = new Settings("lang", true, true);
        menus = new Settings("menus", false);
        chests = new Settings("chests", false);
        kits = new Settings("kits", false);
        levels = new Settings("levels", false);
        rewards = new Settings("rewards", false);
        sounds = new Settings("sounds", true);
        cm = new ConfigManager(this);
        bm = new BungeeManager(this);
        im = new ItemManager(this);
        adm = new AddonManager(this);
        cos = new CosmeticManager(this);
        cos.reload();
        ctm = new ChestManager(this);
        uim = new UltraInventoryMenu(this);
        uim.loadMenus();
        ijm = new InjectionManager(this);
        adm.reload();
        wc = new WorldController(this);
        cc = new ChestController(this);
        gm = new GameManager(this);
        randomizer = new Randomizer();
        gem = new GameMenu(this);
        if (getCm().isBungeeModeEnabled()) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            if (getCm().isBungeeModeGame()) {
                if (gm.getBungee() == null) {
                    getConfig().set("bungee.setup", true);
                    saveConfig();
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (getCm().isBungeeModeLobby()) {
                        getBm().sendMessage("usw:callback", GameType.SOLO.name());
                        getBm().sendMessage("usw:callback", GameType.TEAM.name());
                        getBm().sendMessage("usw:callback", GameType.RANKED.name());
                    } else {
                        if (gm.getBungee() != null) {
                            getBm().sendUpdateGame(gm.getBungee());
                        }
                        getBm().sendMessage("usw:callback", getCm().getGameType().name());
                    }
                }
            }.runTaskLaterAsynchronously(this, 60);
        }
        km = new KitManager(this);
        km.loadKits();
        sb = new ScoreboardManager(this);
        tgm = new TaggedManager(this);
        mm = new MultiplierManager(this);
        lvl = new LevelManager(this);
        shm = new ShopManager(this);
        gm.reload();
        getCommand("sw").setExecutor(new SkyWarsCMD(this));
        if (getConfig().getBoolean("leaveCMD")) {
            getCommand("leave").setExecutor(new LeaveCMD(this));
        }
        if (cm.isMotdStates()) {
            getServer().getPluginManager().registerEvents(new MotdListener(this), this);
        }
        if (cm.isAutoLapiz()) {
            getServer().getPluginManager().registerEvents(new LapisListener(), this);
        }
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholders().register();
        }
        tsm = new TaskManager(this);
        saveSchematics();
        new BukkitRunnable() {
            @Override
            public void run() {
                ijm.loadInjections();
            }
        }.runTaskLater(this, 30);
        getDb().loadMultipliers(b -> {
        });
        new MetricsLite(this, 9620);
    }

    public void loadMainLobby() {
        if (getConfig().getString("mainLobby") != null) {
            mainLobby = Utils.getStringLocation(getConfig().getString("mainLobby"));
        }
        if (getConfig().getString("topKills") != null) {
            topKills = Utils.getStringLocation(getConfig().getString("topKills"));
        }
        if (getConfig().getString("topWins") != null) {
            topWins = Utils.getStringLocation(getConfig().getString("topWins"));
        }
        if (getConfig().getString("topDeaths") != null) {
            topDeaths = Utils.getStringLocation(getConfig().getString("topDeaths"));
        }
        if (getConfig().getString("topCoins") != null) {
            topCoins = Utils.getStringLocation(getConfig().getString("topCoins"));
        }
        if (getConfig().getString("topElo") != null) {
            topElo = Utils.getStringLocation(getConfig().getString("topElo"));
        }
    }

    @Override
    public void onDisable() {
        stop = true;
        getServer().getOnlinePlayers().forEach(on -> getGm().removePlayerAllGame(on));
        for (String s : getGm().getWorlds()) {
            getWc().deleteWorld(s);
        }
        if (!getDb().getPlayers().keySet().isEmpty()) {
            Collection<UUID> sws = new ArrayList<>(getDb().getPlayers().keySet());
            for (UUID sw : sws) {
                getDb().savePlayerSync(sw);
            }
        }
        if (getCm().isBungeeModeEnabled() && getCm().isBungeeModeGame()) {
            getBm().sendMessage("usw:remove", getCm().getGameServer());
        }
        bm.close();
        File f = new File(Bukkit.getWorldContainer() + "/plugins/FastAsyncWorldEdit/clipboard");
        getWc().deleteDirectory(f);
        db.close();
    }

    public void sendToServer(Player p, String server) {
        if (getAdm().isBalancerRegistered()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(getCm().getPlayerBalancerSection());
            p.sendPluginMessage(this, "playerbalancer:main", out.toByteArray());
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        p.sendPluginMessage(UltraSkyWars.get(), "BungeeCord", out.toByteArray());
    }

    public void saveSchematics() {
        saveResource("clearGlass.schematic", true);
        saveResource("glass.schematic", true);
        File f = new File(Bukkit.getWorldContainer() + "/plugins/WorldEdit/schematics");
        if (!f.exists()) {
            f.mkdirs();
        }
        copy(getDataFolder().getAbsolutePath() + "/clearGlass.schematic", Bukkit.getWorldContainer() + "/plugins/WorldEdit/schematics/clearGlass.schematic");
        copy(getDataFolder().getAbsolutePath() + "/glass.schematic", Bukkit.getWorldContainer() + "/plugins/WorldEdit/schematics/glass.schematic");
        try {
            Files.delete(Paths.get(getDataFolder().getAbsolutePath() + "/clearGlass.schematic"));
            Files.delete(Paths.get(getDataFolder().getAbsolutePath() + "/glass.schematic"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDebugMessage(String... s) {
        if (debugMode) {
            for (String st : s) {
                Bukkit.getConsoleSender().sendMessage("§b[USW Debug] §e" + st);
            }
        }
    }

    public void copy(String origin, String destiny) {
        try (
                InputStream in = new BufferedInputStream(new FileInputStream(origin));
                OutputStream out = new BufferedOutputStream(new FileOutputStream(destiny))) {
            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        reloadConfig();
        arenas.reload();
        lang.reload();
        chests.reload();
        balloon.reload();
        glass.reload();
        rewards.reload();
        killeffect.reload();
        killsound.reload();
        kits.reload();
        menus.reload();
        levels.reload();
        parting.reload();
        taunt.reload();
        trail.reload();
        windance.reload();
        join.reload();
        wineffect.reload();
        adm.remove();
        uim.loadMenus();
        cos.reload();
        ctm.reload();
        im.reload();
        adm.reload();
        km.loadKits();
        cm.reload();
        ijm.reload();
        if (getCm().isBungeeModeLobby()) {
            getBm().sendMessage("usw:callback", GameType.SOLO.name());
            getBm().sendMessage("usw:callback", GameType.TEAM.name());
            getBm().sendMessage("usw:callback", GameType.RANKED.name());
        }
    }

    public void reconnect() {
        if (getCm().isBungeeModeLobby()) {
            getBm().sendMessage("usw:callback", GameType.SOLO.name());
            getBm().sendMessage("usw:callback", GameType.TEAM.name());
            getBm().sendMessage("usw:callback", GameType.RANKED.name());
        }
    }

    public void reloadLang() {
        lang.reload();
    }

    public void sendLogMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage("§c§lUltraSkyWars §8| " + msg);
    }

    public void sendLogMessage(String... msg) {
        for (String m : msg) {
            Bukkit.getConsoleSender().sendMessage("§c§lUltraSkyWars §8| §e" + m);
        }
    }

    public void updateDirectories() {
        try {
            File cosmetics = new File(getDataFolder(), "cosmetics");
            if (!cosmetics.exists()) {
                cosmetics.mkdirs();
                File balloons = new File(getDataFolder(), "balloon.yml");
                if (balloons.exists()) {
                    File to = new File(cosmetics, "balloon.yml");
                    Files.copy(balloons.toPath(), to.toPath());
                    balloons.delete();
                }
                File glass = new File(getDataFolder(), "glass.yml");
                if (glass.exists()) {
                    File to = new File(cosmetics, "glass.yml");
                    Files.copy(glass.toPath(), to.toPath());
                    glass.delete();
                }
                File killeffect = new File(getDataFolder(), "killeffect.yml");
                if (killeffect.exists()) {
                    File to = new File(cosmetics, "killeffect.yml");
                    Files.copy(killeffect.toPath(), to.toPath());
                    killeffect.delete();
                }
                File killsound = new File(getDataFolder(), "killsound.yml");
                if (killsound.exists()) {
                    File to = new File(cosmetics, "killsound.yml");
                    Files.copy(killsound.toPath(), to.toPath());
                    killsound.delete();
                }
                File parting = new File(getDataFolder(), "parting.yml");
                if (parting.exists()) {
                    File to = new File(cosmetics, "parting.yml");
                    Files.copy(parting.toPath(), to.toPath());
                    parting.delete();
                }
                File taunt = new File(getDataFolder(), "taunt.yml");
                if (taunt.exists()) {
                    File to = new File(cosmetics, "taunt.yml");
                    Files.copy(taunt.toPath(), to.toPath());
                    taunt.delete();
                }
                File trail = new File(getDataFolder(), "trail.yml");
                if (trail.exists()) {
                    File to = new File(cosmetics, "trail.yml");
                    Files.copy(trail.toPath(), to.toPath());
                    trail.delete();
                }
                File windance = new File(getDataFolder(), "windance.yml");
                if (windance.exists()) {
                    File to = new File(cosmetics, "windance.yml");
                    Files.copy(windance.toPath(), to.toPath());
                    windance.delete();
                }
                File wineffect = new File(getDataFolder(), "wineffect.yml");
                if (wineffect.exists()) {
                    File to = new File(cosmetics, "wineffect.yml");
                    Files.copy(wineffect.toPath(), to.toPath());
                    wineffect.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isBungeeMode(){
        return true;
    }

}