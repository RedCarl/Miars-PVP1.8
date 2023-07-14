// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks;

import cn.mcarl.miars.core.MiarsCore;
import gg.noob.plunder.Plunder;
import gg.noob.plunder.data.PlayerData;
import gg.noob.plunder.logs.Log;
import gg.noob.plunder.manager.Manager;
import gg.noob.plunder.manager.ManagerManager;
import gg.noob.plunder.util.Latency;
import gg.noob.plunder.util.ReachData;
import gg.noob.plunder.util.VelocityData;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class Check implements Listener
{
    private String name;
    private String displayName;
    private String type;
    private int maxViolations;
    private boolean enable;
    private boolean ban;
    private boolean kick;
    private boolean alert;
    private long violationsRemoveOneTime;
    private Player player;
    private boolean logData;
    private int lagCount;
    private int lastLogTicks;
    private int lastDisconnectTicks;
    private int lastVLReduceTicks;
    private int vl;
    private boolean lagCheck;
    private long lastBack;
    private long lastStartBack;
    private boolean banned;
    
    public Check(final String name) {
        this(name, name);
    }
    
    public Check(final String name, final String displayName) {
        this.maxViolations = 20;
        this.enable = true;
        this.ban = true;
        this.kick = false;
        this.alert = true;
        this.violationsRemoveOneTime = 2500L;
        this.logData = true;
        this.lagCount = 0;
        this.lastLogTicks = 0;
        this.lastDisconnectTicks = 0;
        this.lastVLReduceTicks = 0;
        this.vl = 0;
        this.lagCheck = true;
        this.lastBack = -1L;
        this.lastStartBack = -1L;
        this.banned = false;
        this.name = name;
        this.displayName = displayName;
    }
    
    public void dumpLogs(final String format, final Object... value) {
        this.dumpLogs(String.format(format, value));
    }

    public void dumpLogs(String message) {
        if (!this.enable) {
            return;
        }
        ManagerManager managerManager = Plunder.getInstance().getManagerManager();
        managerManager.getManagers().forEach(manager -> {
            if (manager.isDebug()) {
                Player viewer = Bukkit.getPlayer(manager.getUuid());
                if (viewer != null && viewer.hasPermission("plunder.alerts")) {
                    viewer.sendMessage(message);
                }
            }
        });
    }
    
    public void handleReceivedPacket(final Player player, final Packet packet) {
    }
    
    public void handleSentPacket(final Player player, final Packet packet) {
    }
    
    public boolean handleReceivedPacketCanceled(final Player player, final Packet packet) {
        return false;
    }
    
    public boolean handleSentPacketCanceled(final Player player, final Packet packet) {
        return false;
    }
    
    public boolean handleReceivedPacketMain(final Player player, final Packet packet) {
        this.postToMainThread(() -> this.handleReceivedPacket(player, packet));
        return this.handleReceivedPacketCanceled(player, packet);
    }
    
    public boolean handleSentPacketMain(final Player player, final Packet packet) {
        this.postToMainThread(() -> this.handleSentPacket(player, packet));
        return this.handleSentPacketCanceled(player, packet);
    }
    
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
    }
    
    public void handleRotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
    }
    
    public void handleLocationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
    }
    
    public void handleRotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
    }
    
    public boolean handleLocationUpdateCanceled(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        return false;
    }
    
    public boolean handleRotationUpdateCanceled(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        return false;
    }
    
    public boolean handleLocationUpdateCanceled(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
        return false;
    }
    
    public boolean handleRotationUpdateCanceled(final Player player, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying, final boolean lastGround) {
        return false;
    }
    
    public void handleReachData(final Player player, final ReachData reachData, final long n) {
    }
    
    public void handleReachData(final Player player, final List<ReachData> reachData, final long n) {
    }
    
    public void handleVelocityData(final Player player, final VelocityData velocityData, final long n) {
    }
    
    public void handleAlwaysTransaction(final Player player, final long n) {
    }
    
    public void handleTransaction(final Player player, final long n) {
    }
    
    public void handleTransaction(final Player player, final Vector velocity) {
    }
    
    public void handleTransaction(final Player player, final long n, final short id) {
    }
    
    public void handleTransactionTwice(final Player player, final long n) {
    }
    
    public void handleTransactionTwice(final Player player, final Vector velocity) {
    }
    
    public void handleTransactionTwice(final Player player, final long n, final short id) {
    }
    
    public boolean handleEvent(final Player player, final Event event) {
        return false;
    }
    
    public PlayerData getPlayerData() {
        return Plunder.getInstance().getDataManager().getPlayerData(this.player);
    }
    
    public void disconnect(final String reason) {
        if (this.getPlayer().hasPermission("plunder.bypass")) {
            return;
        }
        final PlayerData data = this.getPlayerData();
        if (data == null) {
            return;
        }
        if (data.getTicks() - this.lastDisconnectTicks < 10) {
            return;
        }
        this.lastDisconnectTicks = data.getTicks();
        this.alertMessage(ChatColor.GRAY + " has been disconnected. Reason: " + ChatColor.WHITE + reason);
        new BukkitRunnable() {
            @Override
            public void run() {
                ((CraftPlayer)Check.this.player).getHandle().playerConnection.networkManager.close(new ChatMessage(reason, new Object[0]));
            }
        }.runTaskLater((Plugin)Plunder.getInstance(), 1L);
    }
    
    public void kick(final String reason) {
        if (this.getPlayer().hasPermission("plunder.bypass")) {
            return;
        }
        final PlayerData data = this.getPlayerData();
        if (data == null) {
            return;
        }
        if (data.getTicks() - this.lastDisconnectTicks < 10) {
            return;
        }
        this.lastDisconnectTicks = data.getTicks();
        this.alertMessage(ChatColor.GRAY + " has been kicked. Reason: " + ChatColor.WHITE + reason);
        new BukkitRunnable() {
            @Override
            public void run() {
                MiarsCore.getBungeeApi().kickPlayer(Check.this.player.getName(), reason);
            }
        }.runTaskLater(Plunder.getInstance(), 1L);
    }
    
    public boolean isStranger() {
        return Plunder.getInstance().getManagerManager().getManagerByUUID(this.player.getUniqueId()).getTrust() < 1000;
    }
    
    public void logCheat(final String format, final Object... value) {
        this.logCheat(String.format(format, value));
    }
    
    public void logCheat() {
        this.logCheat(this.player, null);
    }
    
    public void logCheat(final String message) {
        this.logCheat(this.player, message);
    }
    
    public void logCheat(final Player player) {
        this.logCheat(player, null);
    }
    
    public void logCheat(final Player player, final String moreMessage) {
        if (player == null) {
            return;
        }
        if (!this.enable) {
            return;
        }
        final ManagerManager managerManager = Plunder.getInstance().getManagerManager();
        final Manager playerManager = managerManager.getManagerByUUID(player.getUniqueId());
        if (playerManager == null) {
            return;
        }
        if (playerManager.getTrust() > 5000) {
            return;
        }
        final PlayerData data = Plunder.getInstance().getDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (data.getTicks() < 100 || !data.isReceivedTransaction()) {
            return;
        }
        final Map<UUID, Set<Log>> logs = Plunder.getInstance().getLogs();
        if (MinecraftServer.getServer().currentTPS > 19.97 && MinecraftServer.getServer().tps1.getAverage() > 19.8 && (!this.lagCheck || !data.hasLag()) && data.getTicks() - this.lastLogTicks > 1 && !this.banned) {
            ++this.vl;
            if (this.alert) {
                this.alertMessage(ChatColor.GRAY + " failed " + ChatColor.WHITE + this.displayName + ChatColor.GRAY + " Ping: " + Latency.getPingColor(player.getPing()) + player.getPing() + "ms" + ChatColor.GRAY + " VL: " + ChatColor.WHITE + this.vl + ChatColor.BLACK + "/" + ChatColor.RED + (this.ban ? Integer.valueOf(this.maxViolations) : "\u221e"), moreMessage, true);
            }
            boolean gotBanned = false;
            if (this.ban && !player.hasPermission("plunder.bypass") && !this.banned) {
                if (this.vl >= this.maxViolations) {
                    this.alertMessage(ChatColor.GRAY + " was punish for " + ChatColor.WHITE + this.displayName);
                    this.ban(player);
                    gotBanned = true;
                }
                else if (this.getSameTypeTotalVL() > 32) {
                    this.alertMessage(ChatColor.GRAY + " was punish for too much " + ChatColor.WHITE + this.type.toUpperCase() + ChatColor.GRAY + " violations");
                    this.ban(player);
                    gotBanned = true;
                }
            }
            if (this.logData) {
                final Log log = new Log(player.getUniqueId(), player.getName(), this.name, moreMessage, player.getPing(), this.vl, gotBanned);
                logs.getOrDefault(player.getUniqueId(), new HashSet<Log>()).add(log);
                Plunder.getInstance().getMongoManager().saveLogToAddProcess(log);
            }
            this.lastLogTicks = data.getTicks();
        }
    }
    
    public void alertMessage(final String message) {
        this.alertMessage(message, null, false);
    }

    public void alertMessage(String message, String moreMessage, boolean verbose) {
        ManagerManager managerManager = Plunder.getInstance().getManagerManager();
        managerManager.getManagers().forEach(manager -> {
            Player viewer;
            if ((manager.isAlerts() && (manager.isAlerts() && (this.maxViolations < 10 ? this.vl <= 5 || this.vl % 5 == 0 : this.vl % 10 == 0) && this.ban || manager.isVerbose()) || !verbose) && (viewer = Bukkit.getPlayer((UUID)manager.getUuid())) != null && viewer.hasPermission("plunder.alerts")) {
                if (moreMessage == null) {
                    viewer.sendMessage(message);
                } else {
                    TextComponent component1 = new TextComponent(moreMessage);
                    TextComponent component2 = new TextComponent(message);
                    component2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{component1}));
                    viewer.spigot().sendMessage(component2);
                }
            }
        });
    }

    public List<Check> getSameTypeChecks() {
        PlayerData data = this.getPlayerData();
        return data.getChecks().stream().filter(check -> check.getType().equalsIgnoreCase(this.type)).collect(Collectors.toList());
    }
    
    public int getSameTypeTotalVL() {
        int vl = 0;
        for (final Check check : this.getSameTypeChecks()) {
            if (!check.isBan()) {
                continue;
            }
            vl += check.getVl();
        }
        return vl;
    }
    
    public void postToMainThread(final Runnable runnable) {
        runnable.run();
    }
    
    public void keepBack() {
        this.lastStartBack = System.currentTimeMillis();
    }
    
    public boolean checkKeepBack(final Location to) {
        if (this.getPlayer().hasPermission("plunder.bypass")) {
            return false;
        }
        if ((System.currentTimeMillis() - this.lastBack < 1000L || this.lastBack == -1L) && this.lastStartBack != -1L) {
            if (this.lastBack == -1L) {
                this.lastBack = System.currentTimeMillis();
            }
            return true;
        }
        if (System.currentTimeMillis() - this.lastBack >= 1000L && this.lastBack != -1L) {
            this.lastStartBack = -1L;
            this.lastBack = -1L;
        }
        return false;
    }
    
    public void back() {
        this.back(2);
    }
    
    public void back(final int ticks) {
        if (ticks < 0) {
            throw new IllegalStateException("Ticks lower than 0");
        }
        if (this.enable && !this.player.hasPermission("plunder.bypass") && this.isStranger()) {
            final PlayerData data = this.getPlayerData();
            data.executeBack();
            if (data.backTicks < 6) {
                final PlayerData playerData = data;
                playerData.backTicks += ticks;
            }
            data.backTime = System.currentTimeMillis();
        }
    }
    
    public void fallBack() {
        this.back(2);
    }
    
    public void fallBack(final int ticks) {
        if (ticks < 0) {
            throw new IllegalStateException("Ticks lower than 0");
        }
        if (this.enable && !this.player.hasPermission("plunder.bypass")) {
            final PlayerData data = this.getPlayerData();
            data.executeFallBack();
            if (data.fallBackTicks < 6) {
                final PlayerData playerData = data;
                playerData.fallBackTicks += ticks;
            }
            data.fallBackTime = System.currentTimeMillis();
        }
    }
    
    public void ban() {
        this.ban(this.player);
    }
    
    public void ban(final Player player) {
        if (!player.isOnline()) {
            return;
        }
        final PlayerData data = this.getPlayerData();
        if (this.banned || data.isBanned()) {
            return;
        }
        data.setBanned(this.banned = true);
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatColor.RED + ChatColor.BOLD.toString() + "\u2718 " + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " was banned by " + ChatColor.AQUA + "Plunder " + ChatColor.WHITE + "for " + ChatColor.AQUA + "Unfair Advantage");
        Bukkit.broadcastMessage("");
        final ManagerManager managerManager = Plunder.getInstance().getManagerManager();
        final Manager playerManager = managerManager.getManagerByUUID(player.getUniqueId());
        MinecraftServer.getServer().postToMainThread(() -> Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "ban -s " + player.getName() + " 30d Unfair Advantage"));
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public String getType() {
        return this.type;
    }
    
    public int getMaxViolations() {
        return this.maxViolations;
    }
    
    public boolean isEnable() {
        return this.enable;
    }
    
    public boolean isBan() {
        return this.ban;
    }
    
    public boolean isKick() {
        return this.kick;
    }
    
    public boolean isAlert() {
        return this.alert;
    }
    
    public long getViolationsRemoveOneTime() {
        return this.violationsRemoveOneTime;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public boolean isLogData() {
        return this.logData;
    }
    
    public int getLagCount() {
        return this.lagCount;
    }
    
    public int getLastLogTicks() {
        return this.lastLogTicks;
    }
    
    public int getLastDisconnectTicks() {
        return this.lastDisconnectTicks;
    }
    
    public int getLastVLReduceTicks() {
        return this.lastVLReduceTicks;
    }
    
    public int getVl() {
        return this.vl;
    }
    
    public boolean isLagCheck() {
        return this.lagCheck;
    }
    
    public long getLastBack() {
        return this.lastBack;
    }
    
    public long getLastStartBack() {
        return this.lastStartBack;
    }
    
    public boolean isBanned() {
        return this.banned;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setMaxViolations(final int maxViolations) {
        this.maxViolations = maxViolations;
    }
    
    public void setEnable(final boolean enable) {
        this.enable = enable;
    }
    
    public void setBan(final boolean ban) {
        this.ban = ban;
    }
    
    public void setKick(final boolean kick) {
        this.kick = kick;
    }
    
    public void setAlert(final boolean alert) {
        this.alert = alert;
    }
    
    public void setViolationsRemoveOneTime(final long violationsRemoveOneTime) {
        this.violationsRemoveOneTime = violationsRemoveOneTime;
    }
    
    public void setPlayer(final Player player) {
        this.player = player;
    }
    
    public void setLogData(final boolean logData) {
        this.logData = logData;
    }
    
    public void setLagCount(final int lagCount) {
        this.lagCount = lagCount;
    }
    
    public void setLastLogTicks(final int lastLogTicks) {
        this.lastLogTicks = lastLogTicks;
    }
    
    public void setLastDisconnectTicks(final int lastDisconnectTicks) {
        this.lastDisconnectTicks = lastDisconnectTicks;
    }
    
    public void setLastVLReduceTicks(final int lastVLReduceTicks) {
        this.lastVLReduceTicks = lastVLReduceTicks;
    }
    
    public void setVl(final int vl) {
        this.vl = vl;
    }
    
    public void setLagCheck(final boolean lagCheck) {
        this.lagCheck = lagCheck;
    }
    
    public void setLastBack(final long lastBack) {
        this.lastBack = lastBack;
    }
    
    public void setLastStartBack(final long lastStartBack) {
        this.lastStartBack = lastStartBack;
    }
    
    public void setBanned(final boolean banned) {
        this.banned = banned;
    }
}
