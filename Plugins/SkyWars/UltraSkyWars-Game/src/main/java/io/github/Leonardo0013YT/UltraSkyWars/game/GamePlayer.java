package io.github.Leonardo0013YT.UltraSkyWars.game;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.OrderType;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GamePlayer {

    private Player p;
    private int kills, coins, xp, souls;
    private ItemStack[] inv, armor;
    private Collection<PotionEffect> effects;
    private Set<String> challenges = new HashSet<>();
    private Scoreboard scoreboard;
    private boolean allowFly, flying, reset, dead;
    private float walkSpeed, flySpeed;
    private int food;
    private double maxHealth, health;
    private GameMode mode;
    private OrderType orderType;

    public GamePlayer(Player p, Game game, boolean mod) {
        UltraSkyWars.get().getAdm().addPlayerNameTag(p);
        this.p = p;
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
                p.getInventory().setItem(0, plugin.getIm().getKits());
                if (game.getGameType().equals("SOLO")) {
                    if (game.isVotes()) {
                        p.getInventory().setItem(1, plugin.getIm().getVotes());
                    }
                }
                if (game.getGameType().equals("TEAM")) {
                    if (game.isVotes()) {
                        p.getInventory().setItem(1, plugin.getIm().getVotes());
                        p.getInventory().setItem(2, plugin.getIm().getTeam());
                    } else {
                        p.getInventory().setItem(1, plugin.getIm().getTeam());
                    }
                }
                if (game.getGameType().equals("RANKED")) {
                    if (game.isVotes() && !plugin.getCm().isDisableVotesRanked()) {
                        p.getInventory().setItem(1, plugin.getIm().getVotes());
                    }
                }
                if (UltraSkyWars.get().getIjm().isChallenges()) {
                    p.getInventory().setItem(7, plugin.getIm().getChallenges());
                }
                p.getInventory().setItem(8, plugin.getIm().getLeave());
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
        p.getActivePotionEffects().forEach(e -> p.removePotionEffect(e.getType()));
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.getInventory().setContents(inv);
        p.getInventory().setArmorContents(armor);
        effects.forEach(e -> p.addPotionEffect(e));
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
        for (Player on : Bukkit.getOnlinePlayers()) {
            if (!UltraSkyWars.get().getGm().isPlayerInGame(on)) {
                on.showPlayer(p);
                p.showPlayer(on);
            }
        }
        UltraSkyWars.get().getAdm().resetPlayerNameTag(p);
        reset = true;
    }

    public void clear() {
        if (p == null) return;
        if (p.getScoreboard() != null){
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
        return p;
    }
}