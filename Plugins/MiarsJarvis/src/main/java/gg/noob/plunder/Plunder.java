package gg.noob.plunder;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import gg.noob.lib.util.LinkedList;
import gg.noob.lib.util.MongoUtils;
import gg.noob.plunder.checks.Check;
import gg.noob.plunder.command.*;
import gg.noob.plunder.data.DataManager;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.data.listener.DataGeneralListener;
import gg.noob.plunder.data.listener.DataListener;
import gg.noob.plunder.listener.RegularListener;
import gg.noob.plunder.logs.Log;
import gg.noob.plunder.logs.command.LogsCommand;
import gg.noob.plunder.manager.ManagerManager;
import gg.noob.plunder.util.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Plunder extends JavaPlugin {
    private static Plunder instance;
    private static BukkitConfig bukkitConfig;
    private MongoManager mongoManager;
    private Global global;
    private Map<UUID, Map.Entry<Long, Vector>> lastVelocity;
    private Map<UUID, Map.Entry<Long, Location>> lastGround;
    private Map<UUID, Set<Log>> logs;
    private ExecutorService thread;
    private DataManager dataManager;
    private ManagerManager managerManager;
    private Set<Check> checks;
    private List<Runnable> processRunnables;
    private List<Runnable> processMongoRunnables;
    private ExecutorService processThread;
    private ExecutorService mongoThread;
    private List<Runnable> processPacketRunnables;
    public List<Runnable> checkRunnable;
    public Deque<Runnable> onTickEnd;
    public ServerInjector serverInjector;
    public static HashMap<String, Integer> timerLeft;
    public static HashMap<String, BukkitRunnable> cooldownTask;

    @Override
    public void onEnable() {

        instance = this;
        this.global = new Global();
        bukkitConfig = new BukkitConfig();

        MongoUtils.init();

        this.mongoManager = new MongoManager();
        this.lastVelocity = new HashMap<>();
        this.logs = new HashMap<>();
        this.lastGround = new HashMap<>();
        this.checks = new HashSet<>();
        this.dataManager = new DataManager();
        this.managerManager = new ManagerManager();
        this.processRunnables = new ArrayList<>();
        this.processMongoRunnables = new ArrayList<>();
        this.checkRunnable = new ArrayList<>();
        this.onTickEnd = new LinkedList<>();
        this.mongoThread = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Plunder Mongo Thread").build());
        this.processThread = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Plunder Process Thread").build());
        this.thread = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("Plunder Async Thread - %d").build());
        this.processPacketRunnables = new ArrayList<>();
        this.serverInjector = new ServerInjector();
        try {
            this.serverInjector.inject();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getPluginManager().registerEvents(new RegularListener(), this);
        Bukkit.getPluginManager().registerEvents(new DataGeneralListener(), this);
        Bukkit.getPluginManager().registerEvents(new DataListener(), this);
        Bukkit.getPluginManager().registerEvents(new Latency(), this);
        this.getCommand("alerts").setExecutor(new AlertsCommand());
        this.getCommand("verbose").setExecutor(new VerboseCommand());
        this.getCommand("debug").setExecutor(new DebugCommand());
        this.getCommand("maxCPS").setExecutor(new MaxCPSCommand());
        this.getCommand("bot").setExecutor(new BotCommand());
        this.getCommand("logs").setExecutor(new LogsCommand());
        this.getCommand("plunder").setExecutor(new PlunderCommand());
        this.getCommand("playerInfo").setExecutor(new PlayerInfoCommand());
        ClassUtils.getClassesInPackage(Plunder.getInstance(), "gg.noob.plunder.checks").forEach(checkClass -> {
            if (Check.class.isAssignableFrom(checkClass)) {
                if (checkClass.equals(Check.class)) {
                    return;
                }
                try {
                    this.checks.add((Check)checkClass.newInstance());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.setUpUpdate();
        for (Player p : Bukkit.getOnlinePlayers()) {
            Channel channel = ((CraftPlayer)p).getHandle().playerConnection.networkManager.channel;
            ChannelPipeline pipeline = channel.pipeline();
            String handlerName = "plunder_packet_processor";
            channel.eventLoop().submit(() -> {
                if (pipeline.get(handlerName) != null) {
                    pipeline.remove(handlerName);
                }
                return null;
            });
        }
        this.global.sendLog("&aSuccessfully loaded &6&lPlunder&a!");
    }

    @Override
    public void onDisable() {
        instance = null;
        try {
            this.serverInjector.eject();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setUpUpdate() {
        new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    ArrayList<UUID> needRemoveUuids = new ArrayList<>();
                    for (UUID uuid2 : Plunder.this.lastVelocity.keySet()) {
                        Player player = Plunder.this.getServer().getPlayer(uuid2);
                        if (player == null || !player.isOnline()) {
                            needRemoveUuids.add(uuid2);
                            continue;
                        }
                        PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
                        if (data == null) {
                            needRemoveUuids.add(uuid2);
                            continue;
                        }
                        Vector velocity = Plunder.this.getLastVelocity().get(uuid2).getValue();
                        Long time = Plunder.this.getLastVelocity().get(uuid2).getKey();
                        if (time + 500L > System.currentTimeMillis()) {
                            continue;
                        }
                        double velY = velocity.getY() * velocity.getY();
                        double Y = player.getVelocity().getY() * player.getVelocity().getY();
                        if (Y < 0.02 && player.isOnGround()) {
                            needRemoveUuids.add(uuid2);
                            data.setVelocityGroundTicks(0);
                            continue;
                        }
                        if (!(Y > velY * 3.0)) {
                            continue;
                        }
                        needRemoveUuids.add(uuid2);
                    }
                    needRemoveUuids.forEach(uuid -> {
                        Plunder.this.lastVelocity.remove(uuid);
                    });
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskTimer(this, 1L, 1L);
        new BukkitRunnable(){
            boolean stop = false;
            @Override
            public void run() {
                if (this.stop) {
                    return;
                }
                this.stop = true;
                try {
                    Plunder.this.processThread.execute(() -> {
                        for (Runnable runnable : Plunder.this.processRunnables) {
                            runnable.run();
                        }
                        Plunder.this.processRunnables.clear();
                    });
                }
                catch (Exception exception) {
                    // empty catch block
                }
                this.stop = false;
            }
        }.runTaskTimerAsynchronously(this, 1L, 1L);
        new BukkitRunnable(){
            boolean stop = false;
            @Override
            public void run() {
                if (this.stop) {
                    return;
                }
                this.stop = true;
                try {
                    Plunder.this.mongoThread.execute(() -> {
                        for (Runnable runnable : Plunder.this.processMongoRunnables) {
                            runnable.run();
                        }
                        Plunder.this.processMongoRunnables.clear();
                    });
                }
                catch (Exception exception) {
                    // empty catch block
                }
                this.stop = false;
            }
        }.runTaskTimerAsynchronously(this, 1L, 1L);
        new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    for (PlayerData data : Plunder.this.dataManager.getPlayerDataSet()) {
                        for (Check check : data.getChecks()) {
                            int vl = check.getVl();
                            if ((long)(data.getTicks() - check.getLastVLReduceTicks()) > check.getViolationsRemoveOneTime() && vl > 0) {
                                --vl;
                                check.setLastVLReduceTicks(data.getTicks());
                            }
                            check.setVl(vl);
                        }
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskTimer(this, 1L, 1L);
    }


    public void addProcessMongoRunnable(Runnable runnable) {
        this.mongoThread.execute(() -> this.processMongoRunnables.add(runnable));
    }

    public static Plunder getInstance() {
        return instance;
    }

    public static BukkitConfig getBukkitConfig() {
        return bukkitConfig;
    }

    public MongoManager getMongoManager() {
        return this.mongoManager;
    }

    public Map<UUID, Map.Entry<Long, Vector>> getLastVelocity() {
        return this.lastVelocity;
    }

    public Map<UUID, Map.Entry<Long, Location>> getLastGround() {
        return this.lastGround;
    }

    public Map<UUID, Set<Log>> getLogs() {
        return this.logs;
    }

    public DataManager getDataManager() {
        return this.dataManager;
    }

    public ManagerManager getManagerManager() {
        return this.managerManager;
    }

    public Set<Check> getChecks() {
        return this.checks;
    }

    static {
        timerLeft = new HashMap<>();
        cooldownTask = new HashMap<>();
    }
}

