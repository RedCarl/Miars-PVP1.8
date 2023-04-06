package io.github.Leonardo0013YT.UltraSkyWars;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import io.github.Leonardo0013YT.UltraSkyWars.cmds.SkyWarsCMD;
import io.github.Leonardo0013YT.UltraSkyWars.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.controllers.VersionController;
import io.github.Leonardo0013YT.UltraSkyWars.controllers.WorldController;
import io.github.Leonardo0013YT.UltraSkyWars.data.MongoDBDatabase;
import io.github.Leonardo0013YT.UltraSkyWars.data.MySQLDatabase;
import io.github.Leonardo0013YT.UltraSkyWars.enums.GameType;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Database;
import io.github.Leonardo0013YT.UltraSkyWars.listeners.GeneralListener;
import io.github.Leonardo0013YT.UltraSkyWars.listeners.lobby.LobbyListener;
import io.github.Leonardo0013YT.UltraSkyWars.listeners.lobby.LobbyMenuListener;
import io.github.Leonardo0013YT.UltraSkyWars.managers.*;
import io.github.Leonardo0013YT.UltraSkyWars.menus.GameMenu;
import io.github.Leonardo0013YT.UltraSkyWars.menus.UltraInventoryMenu;
import io.github.Leonardo0013YT.UltraSkyWars.placeholders.Placeholders;
import io.github.Leonardo0013YT.UltraSkyWars.utils.MetricsLite;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Getter
public class UltraSkyWars extends JavaPlugin {

    private static UltraSkyWars instance;
    private static Gson gson;
    private Settings sounds, chestType, migrators, join, arenas, lang, menus, chests, balloon, glass, killeffect, killsound, kits, parting, taunt, trail, windance, wineffect, levels, rewards;
    private ItemManager im;
    private CosmeticManager cos;
    private ConfigManager cm;
    private UltraInventoryMenu uim;
    private GameManager gm;
    private KitManager km;
    private Location mainLobby, topKills, topWins, topDeaths, topCoins, topElo;
    private Database db;
    private ScoreboardManager sb;
    private TopManager top;
    private MultiplierManager mm;
    private AddonManager adm;
    private BungeeManager bm;
    private ShopManager shm;
    private LevelManager lvl;
    private GameMenu gem;
    private TaskManager tsm;
    private InjectionManager ijm;
    private VersionController vc;
    private WorldController wc;
    private ArrayList<Entity> entities = new ArrayList<>();
    private boolean debugMode;
    private String version;

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
        version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        getConfig().options().copyDefaults(true);
        saveConfig();
        loadMainLobby();
        updateDirectories();
        debugMode = getConfig().getBoolean("debugMode");
        vc = new VersionController(this);
        if (getConfig().getBoolean("mongodb.enabled")) {
            db = new MongoDBDatabase(this);
        } else {
            db = new MySQLDatabase(this);
        }
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
        ijm = new InjectionManager(this);
        cm = new ConfigManager(this);
        bm = new BungeeManager(this);
        im = new ItemManager(this);
        wc = new WorldController(this);
        adm = new AddonManager(this);
        adm.reload();
        cos = new CosmeticManager(this);
        cos.reload();
        uim = new UltraInventoryMenu(this);
        gm = new GameManager(this);
        gem = new GameMenu(this);
        if (getCm().isBungeeModeEnabled()) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (getCm().isBungeeModeLobby()) {
                        getBm().sendMessage("usw:callback", GameType.SOLO.name());
                        getBm().sendMessage("usw:callback", GameType.TEAM.name());
                        getBm().sendMessage("usw:callback", GameType.RANKED.name());
                    } else {
                        getBm().sendMessage("usw:callback", getCm().getGameType().name());
                    }
                }
            }.runTaskLaterAsynchronously(this, 60);
        }
        km = new KitManager(this);
        km.loadKits();
        sb = new ScoreboardManager(this);
        top = new TopManager(this);
        mm = new MultiplierManager(this);
        lvl = new LevelManager(this);
        shm = new ShopManager(this);
        gm.reload();
        getCommand("sw").setExecutor(new SkyWarsCMD(this));
        getServer().getPluginManager().registerEvents(new LobbyListener(this), this);
        getServer().getPluginManager().registerEvents(new LobbyMenuListener(this), this);
        getServer().getPluginManager().registerEvents(new GeneralListener(this), this);
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholders().register();
        }
        tsm = new TaskManager(this);
        new BukkitRunnable() {
            @Override
            public void run() {
                ijm.loadInjections();
            }
        }.runTaskLater(this, 30);
        getDb().loadTopKills();
        getDb().loadTopWins();
        getDb().loadTopDeaths();
        getDb().loadTopCoins();
        getDb().loadTopElo();
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
        if (top != null) {
            getTop().remove();
        }
        if (!getDb().getPlayers().keySet().isEmpty()) {
            Collection<UUID> sws = new ArrayList<>(getDb().getPlayers().keySet());
            for (UUID sw : sws) {
                getDb().savePlayerSync(sw);
            }
        }
        bm.close();
        db.close();
    }

    public boolean is1_13to16() {
        return getVersion().equals("v1_13_R2") || getVersion().equals("v1_14_R1") || getVersion().equals("v1_15_R1") || getVersion().equals("v1_16_R1") || getVersion().equals("v1_16_R2") || getVersion().equals("v1_16_R3");
    }

    public void sendToServer(Player p, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        p.sendPluginMessage(UltraSkyWars.get(), "BungeeCord", out.toByteArray());
    }

    public void sendDebugMessage(String... s) {
        if (debugMode) {
            for (String st : s) {
                Bukkit.getConsoleSender().sendMessage("§b[USW Debug] §e" + st);
            }
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
        im.reload();
        adm.reload();
        top.createTops();
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