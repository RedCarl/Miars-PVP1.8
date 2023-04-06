package io.github.Leonardo0013YT.UltraSkyWars.listeners.bungeemode;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameKillEvent;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.data.USWPlayerLoadEvent;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.trails.Trail;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.PerkType;
import io.github.Leonardo0013YT.UltraSkyWars.enums.StatType;
import io.github.Leonardo0013YT.UltraSkyWars.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.events.ZombieEvent;
import io.github.Leonardo0013YT.UltraSkyWars.game.*;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Perk;
import io.github.Leonardo0013YT.UltraSkyWars.team.Team;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class GameListener implements Listener {

    private final ArrayList<Player> views = new ArrayList<>();
    private final HashSet<UUID> staffs = new HashSet<>();
    private final UltraSkyWars plugin;

    public GameListener(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        plugin.getDb().loadPlayer(p);
        Game game = plugin.getGm().getBungee();
        if (game != null) {
            e.setJoinMessage(null);
            if (staffs.contains(p.getUniqueId())){
                plugin.getGm().getPlayerGame().put(p.getUniqueId(), game.getId());
                game.getCached().add(p);
                game.setSpect(p);
                return;
            }
            if (game.getGameType().equals("TEAM")) {
                p.teleport(game.getLobby());
            } else {
                game.preJoinGame(p);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        staffs.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e){
        staffs.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent e) {
        verify(e.blockList());
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        verify(e.blockList());
    }

    private void verify(List<Block> blocks) {
        if (plugin.getCm().isChestHolograms()) {
            for (Block b : blocks) {
                if (b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST) || b.getType().equals(Material.ENDER_CHEST)) {
                    if (b.hasMetadata("TEAM_ID_CHEST")) {
                        Game g = plugin.getGm().getGames().get(b.getMetadata("TEAM_ID_CHEST").get(0).asInt());
                        if (g.getChests().containsKey(b.getLocation())) {
                            UltraGameChest egc = g.getChestByLocation(b.getLocation());
                            egc.delete();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void loadPlayer(USWPlayerLoadEvent e) {
        Player p = e.getPlayer();
        if (p == null || !p.isOnline()) return;
        Game game = plugin.getGm().getBungee();
        if (game != null) {
            if (game.isState(State.FINISH) || game.isState(State.RESTARTING) || game.isState(State.GAME) || game.isState(State.PREGAME)) {
                if (staffs.contains(p.getUniqueId())){
                    return;
                }
                plugin.sendToServer(p, plugin.getCm().getLobbyServer());
                /*plugin.getGm().getPlayerGame().put(p.getUniqueId(), game.getId());
                game.getCached().add(p);
                game.remove(p);
                p.getInventory().clear();
                game.setSpect(p);*/
            } else {
                plugin.getLvl().checkUpgrade(p);
                plugin.getGm().addPlayerGame(p, game.getId());
                plugin.getSb().update(p, null);
            }
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        Game game = plugin.getGm().getBungee();
        if (game == null) {
            if (p.isOp()) {
                e.allow();
                p.sendMessage(plugin.getLang().get(p, "setup.needOneGame"));
            } else {
                e.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.getLang().get(p, "setup.noHasGame"));
            }
            return;
        }
        if (game.getCached().size() >= game.getMax()) {
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, plugin.getLang().get(p, "messages.fullGame"));
            e.setResult(PlayerLoginEvent.Result.KICK_FULL);
            return;
        }
        if (game.isState(State.FINISH) || game.isState(State.RESTARTING) || game.isState(State.GAME) || game.isState(State.PREGAME) || (game.isState(State.STARTING) && game.getStarting() < 3)) {
            if (p.hasPermission("ultraskywars.mod")) {
                staffs.add(p.getUniqueId());
                return;
            }
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.getLang().get(p, "messages.alreadyStart"));
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        } else {
            e.allow();
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof ArmorStand){
            if (e.getRightClicked().hasMetadata("CHEST_ARMOR")){
                e.setCancelled(true);
            }
        }
        if (e.getRightClicked() instanceof Player) {
            Player p = e.getPlayer();
            Player es = (Player) e.getRightClicked();
            Game game = plugin.getGm().getGameByPlayer(p);
            if (game == null) {
                return;
            }
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (game.getSpectators().contains(p)) {
                if (game.getSpectators().contains(es) || views.contains(p)) {
                    return;
                }
                if (sw.isFirstPerson()) {
                    p.setGameMode(GameMode.SPECTATOR);
                    p.setSpectatorTarget(e.getRightClicked());
                    views.add(p);
                    plugin.getVc().getReflection().sendTitle(plugin.getLang().get(p, "titles.first.title"), plugin.getLang().get(p, "titles.first.subtitle"), 0, 40, 0, p);
                }
            }
        }
    }

    @EventHandler
    public void onShit(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        if (views.contains(p)) {
            p.setGameMode(GameMode.SURVIVAL);
            p.teleport(p.getLocation().clone().add(0, 0.5, 0));
            p.setAllowFlight(true);
            p.setFlying(true);
            views.remove(p);
        }
    }

    @EventHandler
    public void onCMD(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game == null) {
            return;
        }
        if (p.hasPermission("ultraskywars.mod")) {
            return;
        }
        for (String cmd : plugin.getCm().getWhitelistedCMD()) {
            if (e.getMessage().startsWith(cmd)) {
                return;
            }
        }
        e.setCancelled(true);
        p.sendMessage(plugin.getLang().get(p, "messages.noIngame"));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game == null) {
            return;
        }
        e.setRespawnLocation(game.getSpectator());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game == null) {
            return;
        }
        e.setDeathMessage(null);
        game.remove(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                p.spigot().respawn();
                game.setSpect(p);
            }
        }.runTaskLater(plugin, 3L);
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        if (sw != null) {
            if (plugin.getCm().isDCMDEnabled()) {
                plugin.getCm().getDeathCommands().forEach(c -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.replaceAll("<player>", p.getName())));
            }
            sw.addStat(StatType.DEATHS, game.getGameType(), 1);
            if (plugin.getIjm().isCubeletsInjection() && plugin.getCm().isCubeletsEnabled()) {
                plugin.getIjm().getCubelets().getCbm().executeCubelet(p);
            }
            GamePlayer gp = game.getGamePlayer().get(p.getUniqueId());
            if (gp != null && !gp.getChallenges().isEmpty()) {
                p.sendMessage(plugin.getLang().get("messages.challenges.failedOnChallenge").replace("<player>", p.getName()).replace("<challenges>", getChallenges(gp)));
            }
        }
        for (Player on : game.getCached()) {
            plugin.getVc().getNMS().sendActionBar(on, plugin.getLang().get(on, "action.kill").replaceAll("<players>", "" + (game.getPlayers().size())));
        }
        if (plugin.getTgm().hasTag(p)) {
            Player k = plugin.getTgm().getTagged(p).getLast();
            if (!game.getPlayers().contains(k)) {
                k = null;
            }
            if (k != null) {
                Bukkit.getPluginManager().callEvent(new USWGameKillEvent(k, p, game));
                Team tk = game.getTeamPlayer(k);
                if (tk != null) {
                    tk.addKill();
                }
                SWPlayer sk = plugin.getDb().getSWPlayer(k);
                GamePlayer gp = game.getGamePlayer().get(k.getUniqueId());
                if (gp != null) {
                    gp.addKills(1);
                    gp.addCoins(plugin.getCm().getCoinsKill());
                    gp.addSouls(plugin.getCm().getSoulsKill());
                    gp.addXP(plugin.getCm().getXpKill());
                }
                if (sk != null) {
                    if (plugin.getIjm().isPerksInjection()) {
                        int bulldozer = plugin.getIjm().getPerks().getPerks().getInt("perks.bulldozer.id");
                        if (sk.getPerksData().containsKey(bulldozer)) {
                            Perk perk = plugin.getIjm().getPerks().getPem().getPerk(PerkType.BULLDOZER);
                            if (perk.isReduced(sk)) {
                                k.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, 1));
                            }
                        }
                    }
                    if (plugin.getCm().isKCMDEnabled()) {
                        Player finalK = k;
                        plugin.getCm().getKillCommands().forEach(c -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.replaceAll("<player>", finalK.getName())));
                    }
                    if (game.getGameType().equals("RANKED")) {
                        if (plugin.getIjm().isEloRankInjection()) {
                            int elo;
                            if (sw != null) {
                                elo = Math.max(plugin.getIjm().getEloRank().getErm().getEloDiference(sk.getElo(), sw.getElo()), 1);
                            } else {
                                elo = 1;
                            }
                            sk.addElo(elo);
                        }
                    }
                    sk.addStat(StatType.KILLS, game.getGameType(), 1);
                    plugin.getCos().executeKillEffect(game, k, p, p.getLocation(), sk.getKillEffect());
                    plugin.getCos().executeKillSound(k, p, sk.getKillSound());
                    if (sw != null) {
                        plugin.getCos().executeParting(p, sw.getParting());
                    }
                }
                k.sendMessage(plugin.getLang().get(k, "messages.winXPAndCoins").replaceAll("<coins>", String.valueOf(plugin.getCm().getCoinsKill())).replaceAll("<xp>", String.valueOf(plugin.getCm().getXpKill())).replaceAll("<souls>", String.valueOf(plugin.getCm().getSoulsKill())));
                if (p.getLastDamageCause() == null || p.getLastDamageCause().getCause() == null) {
                    EntityDamageEvent.DamageCause cause = EntityDamageEvent.DamageCause.CONTACT;
                    if (sk != null) {
                        plugin.getCos().executeTaunt(p, cause, game, sk.getTaunt());
                    }
                } else {
                    EntityDamageEvent.DamageCause cause = p.getLastDamageCause().getCause();
                    if (sk != null) {
                        plugin.getCos().executeTaunt(p, cause, game, sk.getTaunt());
                    }
                }
                plugin.getTgm().executeRewards(p, p.getMaxHealth());
                return;
            }
        }
        if (sw != null) {
            plugin.getCos().executeTaunt(p, game, sw.getTaunt());
        }
    }

    @EventHandler
    public void onBurn(EntityCombustEvent e) {
        if (e.getEntity() instanceof Giant) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBurn(EntityCombustByBlockEvent e) {
        if (e.getEntity() instanceof Giant) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBurn(EntityCombustByEntityEvent e) {
        if (e.getEntity() instanceof Giant) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game == null) {
            return;
        }
        e.getRecipients().clear();
        if (game.getSpectators().contains(p)) {
            if (!plugin.getCm().isSpectatorChat()) {
                e.setCancelled(true);
                p.sendMessage("messages.spectatorChatDisabled");
                return;
            }
            if (plugin.getCm().isSpectatorChatGlobal()) {
                e.getRecipients().addAll(game.getCached());
            } else {
                e.getRecipients().addAll(game.getSpectators());
            }
            e.setFormat(plugin.getLang().get(p, "chat.spectator").replaceAll("<player>", p.getName()).replaceAll("<msg>", e.getMessage()).replaceAll("%", "%%"));
            return;
        }
        if (!plugin.getCm().isChatSystem()) {
            return;
        }
        if (game.isState(State.WAITING) || game.isState(State.STARTING)) {
            e.getRecipients().addAll(game.getCached());
            String msg;
            if (game instanceof UltraRankedGame) {
                msg = formatRanked(p, "lobbyRanked", plugin.getIjm().isEloRankInjection(), e.getMessage());
            } else {
                msg = formatNormal(p, "lobby", e.getMessage());
            }
            e.setFormat(msg.replaceAll("%", "%%"));
            return;
        }
        if (game.isState(State.PREGAME) || game.isState(State.GAME) || game.isState(State.FINISH) || game.isState(State.RESTARTING)) {
            if (game instanceof UltraGame) {
                e.getRecipients().addAll(game.getCached());
                e.setFormat(formatNormal(p, "game", e.getMessage()).replaceAll("%", "%%"));
            } else if (game instanceof UltraTeamGame) {
                Team team = game.getTeamPlayer(p);
                if (team == null) {
                    return;
                }
                String msg;
                if (e.getMessage().startsWith(plugin.getCm().getTeamModeChar())) {
                    msg = formatTeam(p, "global", team, e.getMessage());
                    e.getRecipients().addAll(game.getCached());
                } else {
                    msg = formatTeam(p, "team", team, e.getMessage());
                    e.getRecipients().addAll(team.getMembers());
                }
                e.setFormat(msg.replaceAll("%", "%%"));
            } else {
                e.getRecipients().addAll(game.getCached());
                e.setFormat(formatRanked(p, "ranked", plugin.getIjm().isEloRankInjection(), e.getMessage()).replaceAll("%", "%%"));
            }
        }
    }

    private String formatNormal(Player p, String type, String msg) {
        return plugin.getAdm().parsePlaceholders(p, plugin.getLang().get(p, "chat." + type).replaceAll("<player>", p.getName()).replaceAll("<msg>", msg));
    }

    private String formatTeam(Player p, String global, Team team, String msg) {
        return plugin.getAdm().parsePlaceholders(p, plugin.getLang().get(p, "chat." + global).replace("<team>", plugin.getLang().get(p, "team").replaceAll("<#>", String.valueOf(team.getId() + 1))).replaceAll("<player>", p.getName()).replaceAll("<msg>", msg.replaceFirst(plugin.getCm().getTeamModeChar(), "")));
    }

    private String formatRanked(Player p, String type, boolean rank, String msg) {
        String format = plugin.getLang().get(p, "chat." + type);
        return (rank) ? plugin.getAdm().parsePlaceholders(p, format.replaceAll("<rank>", plugin.getIjm().getEloRank().getErm().getEloRankChat(p)).replaceAll("<player>", p.getName()).replaceAll("<msg>", msg)) : plugin.getAdm().parsePlaceholders(p, format.replaceAll("<player>", p.getName()).replaceAll("<msg>", msg));
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Game g = plugin.getGm().getGameByPlayer(p);
        if (g == null) {
            return;
        }
        if (g.isState(State.WAITING) || g.isState(State.STARTING) || g.isState(State.PREGAME)) {
            e.setCancelled(true);
            return;
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        if (sw == null) return;
        sw.addStat(StatType.BREAK, g.getGameType(), 1);
        Block b = e.getBlock();
        if (b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST) || b.getType().equals(Material.ENDER_CHEST)) {
            if (plugin.getCm().isChestHolograms()) {
                if (g.getChests().containsKey(b.getLocation())) {
                    UltraGameChest egc = g.getChestByLocation(b.getLocation());
                    egc.delete();
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Game g = plugin.getGm().getGameByPlayer(p);
        if (g == null) {
            return;
        }
        if (g.isState(State.WAITING) || g.isState(State.STARTING) || g.isState(State.PREGAME)) {
            e.setCancelled(true);
            return;
        }
        SWPlayer sw = plugin.getDb().getSWPlayer(p);
        sw.addStat(StatType.PLACED, g.getGameType(), 1);
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            Game g = plugin.getGm().getGameByPlayer(p);
            if (g == null) {
                return;
            }
            if (g.isState(State.WAITING) || g.isState(State.STARTING) || g.isState(State.PREGAME)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Game g = plugin.getGm().getGameByPlayer(p);
        if (g == null) {
            return;
        }
        Location t = e.getTo();
        if (g.isState(State.WAITING) || g.isState(State.STARTING) || g.isState(State.PREGAME) || g.isState(State.FINISH) || g.isState(State.RESTARTING)) {
            if (t.getY() <= 10) {
                p.teleport(g.getLobby());
            }
        }
        if (g.isState(State.GAME)) {
            if (t.getBlockY() <= 0) {
                if (!g.getSpectators().contains(p)) {
                    g.remove(p);
                    p.setHealth(0);
                } else {
                    p.teleport(g.getLobby());
                }
            }
            Location f = e.getFrom();
            if (t.getBlockX() != f.getBlockX() || t.getBlockY() != f.getBlockY() || t.getBlockZ() != f.getBlockZ()){
                SWPlayer sw = plugin.getDb().getSWPlayer(p);
                if (sw != null){
                    sw.addStat(StatType.WALKED, g.getGameType(), 1);
                }
            }
        }
        if (g.isState(State.FINISH)) {
            if (p.getVehicle() != null) {
                Entity ent = p.getVehicle();
                if (ent == null) return;
                if (ent.getType().equals(EntityType.ENDER_DRAGON) || ent.getType().equals(EntityType.WITHER) || ent.getType().equals(EntityType.HORSE)) {
                    Vector vec = p.getLocation().getDirection();
                    ent.setVelocity(vec.multiply(0.5));
                    plugin.getVc().getReflection().moveDragon(ent, ent.getLocation().getX(), ent.getLocation().getY(), ent.getLocation().getZ(), p.getLocation().getYaw() - 180, p.getLocation().getPitch());
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        Game g = plugin.getGm().getGameByPlayer(p);
        if (e.getItemDrop() == null || e.getItemDrop().getItemStack() == null) {
            return;
        }
        ItemStack item = e.getItemDrop().getItemStack();
        if (item.equals(plugin.getIm().getLobby())) {
            e.setCancelled(true);
        }
        if (g == null) {
            return;
        }
        if (g.isState(State.WAITING) || g.isState(State.STARTING) || g.isState(State.PREGAME)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.PHYSICAL)) {
            return;
        }
        Game game = plugin.getGm().getGameByPlayer(p);
        if (game != null && game.isState(State.FINISH)) {
            if (p.getVehicle() != null) {
                Entity ent = p.getVehicle();
                if (ent == null) return;
                if (ent.getType().equals(EntityType.ENDER_DRAGON)) {
                    p.launchProjectile(Fireball.class, p.getEyeLocation().getDirection());
                }
                if (ent.getType().equals(EntityType.WITHER)) {
                    p.launchProjectile(WitherSkull.class, p.getEyeLocation().getDirection());
                }
                if (ent.getType().equals(EntityType.HORSE)) {
                    p.launchProjectile(Snowball.class, p.getEyeLocation().getDirection());
                }
            }
        }
        if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)) {
            return;
        }
        ItemStack item = p.getItemInHand();
        if (game != null) {
            if (plugin.getCm().isEnderPearlsCountdown() && item.getType().equals(Material.ENDER_PEARL)) {
                int s = game.isEnderPearlDisabled();
                if (s > 0){
                    p.sendMessage(plugin.getLang().get("messages.noEnderCountdown").replace("<time>", String.valueOf(s)));
                    e.setCancelled(true);
                    p.updateInventory();
                    return;
                }
            }
        }
        if (item.equals(plugin.getIm().getVotes())) {
            e.setCancelled(true);
            plugin.getUim().openContentInventory(p, plugin.getUim().getMenus("votes"));
            changeSlot(p);
        }
        if (item.equals(plugin.getIm().getKits())) {
            e.setCancelled(true);
            Game g = plugin.getGm().getGameByPlayer(p);
            if (g == null) {
                return;
            }
            plugin.getUim().getPages().put(p.getUniqueId(), 1);
            plugin.getUim().createKitSelectorMenu(p, g.getGameType(), true);
            changeSlot(p);
        }
        if (item.equals(plugin.getIm().getLeave())) {
            e.setCancelled(true);
            plugin.getGm().removePlayerAllGame(p);
            if (plugin.getCm().isBungeeModeEnabled() && plugin.getCm().isBungeeModeGame()) {
                plugin.sendToServer(p, plugin.getCm().getLobbyServer());
            }
            p.sendMessage(plugin.getLang().get(p, "messages.leaveGame"));
            changeSlot(p);
        }
        if (item.equals(plugin.getIm().getChallenges())) {
            e.setCancelled(true);
            if (plugin.getIjm().isChallenges()) {
                plugin.getIjm().getChallenges().getChm().createChallengeMenu(p);
            }
            changeSlot(p);
        }
        if (item.equals(plugin.getIm().getSpectate())) {
            e.setCancelled(true);
            Game g = plugin.getGm().getGameByPlayer(p);
            if (g == null) {
                return;
            }
            GamePlayer gp = g.getGamePlayer().get(p.getUniqueId());
            plugin.getUim().openPlayersInventory(p, plugin.getUim().getMenus("players"), g, gp.getOrderType(), new String[]{"<order>", plugin.getLang().get(p, "order." + gp.getOrderType().name())});
            changeSlot(p);
        }
        if (item.equals(plugin.getIm().getOptions())) {
            e.setCancelled(true);
            plugin.getUim().openContentInventory(p, plugin.getUim().getMenus("options"));
        }
        if (item.equals(plugin.getIm().getTeam())) {
            e.setCancelled(true);
            Game g = plugin.getGm().getGameByPlayer(p);
            if (g == null) {
                return;
            }
            plugin.getGem().createTeamSelector(p, g);
            changeSlot(p);
        }
        if (item.equals(plugin.getIm().getPlay())) {
            e.setCancelled(true);
            Game g = plugin.getGm().getGameByPlayer(p);
            if (g == null) {
                return;
            }
            if (plugin.getCm().isOnPaper()) {
                boolean added = plugin.getGm().addRandomGame(p, g.getGameType());
                if (!added) {
                    plugin.sendToServer(p, plugin.getCm().getLobbyServer());
                    return;
                }
                return;
            }
            plugin.getUim().getPages().put(p.getUniqueId(), 1);
            plugin.getGem().createSelectorMenu(p, g.getName(), g.getGameType().toLowerCase());
            changeSlot(p);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity ent = e.getEntity();
        if (ent.hasMetadata("Balloon") || ent.hasMetadata("NO_TARGET")) {
            e.setCancelled(true);
            return;
        }
        if (ent instanceof Player) {
            Player p = (Player) ent;
            Game g = plugin.getGm().getGameByPlayer(p);
            if (g == null) {
                return;
            }
            if (g.isState(State.WAITING) || g.isState(State.STARTING) || g.isState(State.PREGAME) || g.isState(State.FINISH)) {
                e.setCancelled(true);
            }
            if (g.isState(State.GAME)) {
                boolean passed = g.getStarted() + 2000L < System.currentTimeMillis();
                if (!passed) {
                    if (g.getNoDamaged().contains(p)) {
                        e.setCancelled(true);
                        g.getNoDamaged().remove(p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        if (e.getItem().getType().equals(Material.POTION)) {
            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> p.setItemInHand(new ItemStack(Material.AIR)), 1L);
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Giant || e.getDamager() instanceof Giant) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getDamager() instanceof Player) {
                Player d = (Player) e.getDamager();
                Game g = plugin.getGm().getBungee();
                if (g == null) return;
                if (executeDamage(e, p, d, g)) return;
                double damage = e.getFinalDamage();
                plugin.getTgm().setTag(d, p, damage, g);
            }
            if (e.getDamager() instanceof Projectile) {
                Projectile proj = (Projectile) e.getDamager();
                if (proj.getShooter() instanceof Player) {
                    Player d = (Player) proj.getShooter();
                    Game g = plugin.getGm().getGameByPlayer(p);
                    if (g == null) return;
                    if (executeDamage(e, p, d, g)) return;
                    SWPlayer sw = plugin.getDb().getSWPlayer(d);
                    sw.addStat(StatType.SUCCESS_SHOTS, g.getGameType(), 1);
                    double damage = e.getFinalDamage();
                    if (plugin.getCm().isShowBowDamage() && proj instanceof Arrow) {
                        double h = p.getHealth() - damage;
                        if (h >= 0) {
                            d.sendMessage(plugin.getLang().get("messages.healthShow").replace("<player>", p.getName()).replace("<health>", Utils.formatDouble(h)));
                        }
                    }
                    plugin.getTgm().setTag(d, p, damage, g);
                }
            }
        }
    }

    private boolean executeDamage(EntityDamageByEntityEvent e, Player p, Player d, Game g) {
        if (g.isState(State.WAITING) || g.isState(State.STARTING) || g.isState(State.PREGAME) || g.isState(State.FINISH)) {
            e.setCancelled(true);
        }
        if (g.getSpectators().contains(p)) {
            e.setCancelled(true);
            return true;
        }
        Team tp = g.getTeamPlayer(p);
        Team td = g.getTeamPlayer(d);
        if (tp == null || td == null) {
            return true;
        }
        if (tp.getMembers().contains(d) || td.getMembers().contains(p)) {
            e.setCancelled(true);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player p = (Player) e.getEntity().getShooter();
            Game g = plugin.getGm().getGameByPlayer(p);
            if (g == null) {
                return;
            }
            Projectile proj = e.getEntity();
            SWPlayer sw = plugin.getDb().getSWPlayer(p);
            if (sw == null) return;
            if (plugin.getCm().isEnderPearlsCountdown() && proj instanceof EnderPearl) {
                int s = g.isEnderPearlDisabled();
                if (s > 0){
                    p.sendMessage(plugin.getLang().get("messages.noEnderCountdown").replace("<time>", String.valueOf(s)));
                    e.setCancelled(true);
                    p.updateInventory();
                    return;
                }
            }
            sw.addStat(StatType.SHOTS, g.getGameType(), 1);
            proj.setMetadata("TYPE", new FixedMetadataValue(plugin, g.getProjectileType().name()));
            Trail trail = plugin.getCos().getTrails().get(sw.getTrail());
            if (trail == null) {
                return;
            }
            plugin.getCos().spawnTrail(proj, trail);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Zombie) {
            Zombie z = (Zombie) e.getEntity();
            if (z.hasMetadata("GAMEEVENT")) {
                Game game = plugin.getGm().getGames().get(z.getMetadata("GAMEEVENT").get(0).asInt());
                if (game != null && game.getNowEvent() instanceof ZombieEvent) {
                    ZombieEvent ze = (ZombieEvent) game.getNowEvent();
                    ze.getZombies().remove(z);
                }
            }
        }
    }

    public String getChallenges(GamePlayer gp) {
        StringBuilder sb = new StringBuilder();
        for (String s : gp.getChallenges()) {
            sb.append(", ").append(plugin.getLang().get("messages.challenges." + s));
        }
        return sb.toString().replaceFirst(", ", "");
    }

    private void changeSlot(Player p) {
        if (!plugin.getVc().is1_13to16()) return;
        for (int i = 0; i < 9; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null || item.getType().equals(Material.AIR)) {
                p.getInventory().setHeldItemSlot(i);
                break;
            }
        }
    }

}