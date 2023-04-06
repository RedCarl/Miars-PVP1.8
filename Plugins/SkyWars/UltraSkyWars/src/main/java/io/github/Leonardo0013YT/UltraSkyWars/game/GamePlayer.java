package io.github.Leonardo0013YT.UltraSkyWars.game;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.data.USWGamePlayerLoadEvent;
import io.github.Leonardo0013YT.UltraSkyWars.enums.OrderType;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GamePlayer {

    private final ItemStack[] inv, armor;
    private final Collection<PotionEffect> effects;
    private final Set<String> challenges = new HashSet<>();
    private final Scoreboard scoreboard;
    private final boolean allowFly, flying;
    private final float walkSpeed, flySpeed;
    private final int food;
    private final double maxHealth, health;
    private final GameMode mode;
    private UUID player;
    private int kills, coins, xp, souls;
    private boolean reset, dead;
    private OrderType orderType;

    public GamePlayer(Player p, Game game, boolean mod) {
        UltraSkyWars.get().getAdm().addPlayerNameTag(p);
        this.player = p.getUniqueId();
        this.inv = p.getInventory().getContents();
        this.armor = p.getInventory().getArmorContents();
        this.effects = p.getActivePotionEffects();
        this.allowFly = p.getAllowFlight();
        this.flying = p.isFlying();
        this.walkSpeed = p.getWalkSpeed();
        this.flySpeed = p.getFlySpeed();
        this.food = p.getFoodLevel();
        this.health = p.getHealth();
        this.maxHealth = p.getMaxHealth();
        this.mode = p.getGameMode();
        this.scoreboard = p.getScoreboard();
        this.kills = 0;
        this.coins = 0;
        this.xp = 0;
        this.souls = 0;
        this.reset = false;
        this.orderType = OrderType.NONE;
        this.dead = false;
        clear();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (mod) return;
                UltraSkyWars plugin = UltraSkyWars.get();
                if (!game.getGameType().equals("TNT_MADNESS")) {
                    p.getInventory().setItem(plugin.getCm().getKitsSlot(), plugin.getIm().getKits());
                }
                if (game.getGameType().equals("SOLO")) {
                    if (game.isVotes()) {
                        p.getInventory().setItem(plugin.getCm().getVotesSlot(), plugin.getIm().getVotes());
                    }
                }
                if (game.getGameType().equals("TEAM")) {
                    if (game.isVotes()) {
                        p.getInventory().setItem(plugin.getCm().getVotesSlot(), plugin.getIm().getVotes());
                    }
                    p.getInventory().setItem(plugin.getCm().getTeamSlot(), plugin.getIm().getTeam());
                }
                if (game.getGameType().equals("RANKED")) {
                    if (game.isVotes() && !plugin.getCm().isDisableVotesRanked()) {
                        p.getInventory().setItem(plugin.getCm().getVotesSlot(), plugin.getIm().getVotes());
                    }
                }
                if (UltraSkyWars.get().getIjm().isChallenges() && !game.getGameType().equals("TNT_MADNESS")) {
                    p.getInventory().setItem(plugin.getCm().getChallengesSlot(), plugin.getIm().getChallenges());
                }
                p.getInventory().setItem(plugin.getCm().getLeaveSlot(), plugin.getIm().getLeave());
                plugin.getServer().getPluginManager().callEvent(new USWGamePlayerLoadEvent(p, game));
            }
        }.runTaskLater(UltraSkyWars.get(), 5);
    }

    public Set<String> getChallenges() {
        return challenges;
    }

    public boolean hasChallenge(String challenge) {
        return challenges.contains(challenge);
    }

    public void addChallenge(String challenge) {
        challenges.add(challenge);
    }

    public void removeChallenge(String challenge) {
        challenges.remove(challenge);
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getKills() {
        return kills;
    }

    public void addKills(int kills) {
        this.kills += kills;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public int getXP() {
        return xp;
    }

    public void addXP(int xp) {
        this.xp += xp;
    }

    public int getSouls() {
        return souls;
    }

    public void addSouls(int souls) {
        this.souls += souls;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public void reset() {
        if (reset) return;
        Player p = Bukkit.getPlayer(player);
        p.getActivePotionEffects().forEach(e -> p.removePotionEffect(e.getType()));
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.getInventory().setContents(inv);
        p.getInventory().setArmorContents(armor);
        effects.forEach(p::addPotionEffect);
        p.setAllowFlight(allowFly);
        p.setFlying(flying);
        p.setWalkSpeed(walkSpeed);
        p.setFlySpeed(flySpeed);
        p.setFoodLevel(food);
        p.setMaxHealth(maxHealth);
        p.setHealth(health);
        p.setFireTicks(0);
        p.setLevel(0);
        p.setExp(0);
        p.setGameMode(mode);
        p.setScoreboard(scoreboard);
        p.updateInventory();
        p.removePotionEffect(PotionEffectType.ABSORPTION);
        p.removePotionEffect(PotionEffectType.REGENERATION);
        for (Player on : Bukkit.getOnlinePlayers()) {
            if (!UltraSkyWars.get().getGm().isPlayerInGame(on)) {
                on.showPlayer(p);
                p.showPlayer(on);
            }
        }
        UltraSkyWars.get().getAdm().resetPlayerNameTag(p);
        if (UltraSkyWars.get().getVc().getVersion().equals("v1_8_R3")) {
            p.spigot().setCollidesWithEntities(true);
        }
        reset = true;
    }

    public void clear() {
        Player p = Bukkit.getPlayer(player);
        if (p == null) return;
        if (p.getScoreboard() != null) {
            p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        }
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.getActivePotionEffects().forEach(t -> p.removePotionEffect(t.getType()));
        p.setFlying(false);
        p.setAllowFlight(false);
        p.setFireTicks(0);
        p.setWalkSpeed(0.2f);
        p.setFlySpeed(0.1f);
        p.setFoodLevel(20);
        p.setMaxHealth(20.0D);
        p.setHealth(20.0D);
        p.setGameMode(GameMode.SURVIVAL);
        p.updateInventory();
    }

    public Player getP() {
        return Bukkit.getPlayer(player);
    }
}