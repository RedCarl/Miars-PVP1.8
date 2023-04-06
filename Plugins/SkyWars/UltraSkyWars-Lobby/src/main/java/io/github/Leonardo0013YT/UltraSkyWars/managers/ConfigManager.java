package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.enums.GameType;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XSound;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class ConfigManager {

    private ItemStack head;
    private byte waitingState, startingState, emptyState;
    private byte redPanelData;
    private int soulWellPrice, maxCollectHeads, timeBorderReduction, minBorderReduction, rInvParting, rInvGlasses, rInvWinEffects, rInvBalloons, rInvKillEffects, rInvKits, rInvKillSounds, rInvTrails, rInvWinDances, rInvTaunts, updatePlayersPlaceholder, maxItemsChest, ticksAni3, executesAni3, ticksAni1, upYAni1, cubeletChance, gamesToRestart, itemLobbySlot, winTime, progressBarAmount, maxMultiplier, starting, pregame, coinsKill, coinsWin, coinsAssists, xpKill, xpWin, xpAssists, soulsKill, soulsWin, soulsAssists;
    private XMaterial next, last, deselect, challenges, closeitem, back, lobby, team, spectate, options, play, leave, kits, votes, setup, center, island, soulwell;
    private Material redPanelMaterial;
    private boolean cosmeticsGlasses, cosmeticsKillEffect, cosmeticsBalloons, cosmeticsKillSounds, cosmeticsPartings, cosmeticsTaunts, cosmeticsTrails, cosmeticsWinDance, cosmeticsWinEffects,
            disableLevels, disableAllScoreboards, AnimatedNames, trholograms, broadcastGame, broadcastLevelUp, naturalSpawnEggs, disableKitLevels, savingDataSync, motdStates, autoLapiz, wCMDEnabled, kCMDEnabled, dCMDEnabled, teamOneCage, signsRotation, cubeletsEnabled, mainLobby, leaderheads, MVdWPlaceholderAPI, preLobbySystem, partyandfriends, kitLevelsOrder, slimeworldmanager, bungeeModeEnabled, bungeeModeLobby, bungeeModeGame, itemLobbyEnabled, redPanelInLocked, lobbyScoreboard, placeholdersAPI, tabapi, soulwellEnabled, parties, chatSystem, holograms, holographicdisplays, signsRight, signsLeft, vault, playerpoints, coins, nametagedit;
    private String lobbyWorld, slimeworldmanagerLoader, perm, url, itemLobbyCMD, lobbyServer, gameServer;
    private GameType gameType;
    private List<String> whitelistedCMD, winCommands, killCommands, deathCommands;
    private Location previewPlayerGlass, previewCosmeticGlass, previewPlayerBalloon, previewCosmeticBalloon;
    private UltraSkyWars plugin;

    public ConfigManager(UltraSkyWars plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        this.cosmeticsGlasses = plugin.getConfig().getBoolean("cosmetics.glasses");
        this.cosmeticsKillEffect = plugin.getConfig().getBoolean("cosmetics.killEffect");
        this.cosmeticsBalloons = plugin.getConfig().getBoolean("cosmetics.balloons");
        this.cosmeticsKillSounds = plugin.getConfig().getBoolean("cosmetics.killSounds");
        this.cosmeticsPartings = plugin.getConfig().getBoolean("cosmetics.partings");
        this.cosmeticsTaunts = plugin.getConfig().getBoolean("cosmetics.taunts");
        this.cosmeticsTrails = plugin.getConfig().getBoolean("cosmetics.trails");
        this.cosmeticsWinDance = plugin.getConfig().getBoolean("cosmetics.winDance");
        this.cosmeticsWinEffects = plugin.getConfig().getBoolean("cosmetics.winEffects");
        this.disableLevels = plugin.getConfig().getBoolean("specials.disableLevels");
        this.disableAllScoreboards = plugin.getConfig().getBoolean("specials.disableAllScoreboards");
        this.previewPlayerGlass = Utils.getStringLocation(plugin.getConfig().getString("previews.glass.player"));
        this.previewCosmeticGlass = Utils.getStringLocation(plugin.getConfig().getString("previews.glass.cosmetic"));
        this.previewPlayerBalloon = Utils.getStringLocation(plugin.getConfig().getString("previews.balloon.player"));
        this.previewCosmeticBalloon = Utils.getStringLocation(plugin.getConfig().getString("previews.balloon.cosmetic"));
        for (CustomSound cs : CustomSound.values()) {
            String path = "sounds." + cs.name();
            cs.setSound(XSound.matchXSound(plugin.getSounds().get(path + ".sound")).orElse(XSound.BLOCK_NOTE_BLOCK_HAT).parseSound());
            cs.setVolume((float) plugin.getSounds().getDouble(path + ".volume"));
            cs.setPitch((float) plugin.getSounds().getDouble(path + ".pitch"));
        }
        this.soulWellPrice = plugin.getConfig().getInt("soulWellPrice");
        this.maxCollectHeads = plugin.getConfig().getInt("maxCollectHeads");
        this.timeBorderReduction = plugin.getConfig().getInt("gameDefaults.timeBorderReduction");
        this.minBorderReduction = plugin.getConfig().getInt("gameDefaults.minBorderReduction");
        this.rInvParting = plugin.getConfig().getInt("rowsInventories.parting");
        this.rInvGlasses = plugin.getConfig().getInt("rowsInventories.glasses");
        this.rInvWinEffects = plugin.getConfig().getInt("rowsInventories.winEffects");
        this.rInvBalloons = plugin.getConfig().getInt("rowsInventories.balloons");
        this.rInvKillEffects = plugin.getConfig().getInt("rowsInventories.killEffects");
        this.rInvKits = plugin.getConfig().getInt("rowsInventories.kits");
        this.rInvKillSounds = plugin.getConfig().getInt("rowsInventories.killSounds");
        this.rInvTrails = plugin.getConfig().getInt("rowsInventories.trails");
        this.rInvWinDances = plugin.getConfig().getInt("rowsInventories.winDances");
        this.rInvTaunts = plugin.getConfig().getInt("rowsInventories.taunts");
        this.disableKitLevels = plugin.getConfig().getBoolean("disableKitLevels");
        this.savingDataSync = plugin.getConfig().getBoolean("savingDataSync");
        this.lobbyWorld = plugin.getConfig().getString("mainLobbyWorld");
        this.motdStates = plugin.getConfig().getBoolean("motdStates");
        this.autoLapiz = plugin.getConfig().getBoolean("autoLapiz");
        this.wCMDEnabled = plugin.getConfig().getBoolean("win-commands.enabled");
        this.kCMDEnabled = plugin.getConfig().getBoolean("kill-commands.enabled");
        this.dCMDEnabled = plugin.getConfig().getBoolean("death-commands.enabled");
        this.winCommands = plugin.getConfig().getStringList("win-commands.cmds");
        this.killCommands = plugin.getConfig().getStringList("kill-commands.cmds");
        this.deathCommands = plugin.getConfig().getStringList("death-commands.cmds");
        this.teamOneCage = plugin.getConfig().getBoolean("teamOneCage");
        this.signsRotation = plugin.getConfig().getBoolean("signsRotation");
        this.preLobbySystem = plugin.getConfig().getBoolean("preLobbySystem");
        this.kitLevelsOrder = plugin.getConfig().getBoolean("kitLevelsOrder");
        this.whitelistedCMD = plugin.getConfig().getStringList("whitelistedCMD");
        this.updatePlayersPlaceholder = plugin.getConfig().getInt("updatePlayersPlaceholder") * 1000;
        this.gamesToRestart = plugin.getConfig().getInt("bungee.gamesToRestart");
        this.gameServer = plugin.getConfig().getString("bungee.gameServer");
        this.lobbyServer = plugin.getConfig().getString("bungee.lobbyServer");
        this.gameType = GameType.valueOf(plugin.getConfig().getString("bungee.gameType"));
        this.bungeeModeEnabled = plugin.getConfig().getBoolean("bungee.enabled");
        this.bungeeModeLobby = plugin.getConfig().getBoolean("bungee.lobby");
        this.bungeeModeGame = plugin.getConfig().getBoolean("bungee.game");
        this.itemLobbyEnabled = plugin.getConfig().getBoolean("items.lobby.enabled");
        this.itemLobbySlot = plugin.getConfig().getInt("items.lobby.slot");
        this.itemLobbyCMD = plugin.getConfig().getString("items.lobby.cmd");
        this.winTime = plugin.getConfig().getInt("gameDefaults.winTime");
        this.redPanelData = (byte) plugin.getConfig().getInt("redPanel.data");
        this.redPanelMaterial = Material.valueOf(plugin.getConfig().getString("redPanel.material"));
        this.redPanelInLocked = plugin.getConfig().getBoolean("redPanelInLocked");
        this.progressBarAmount = plugin.getConfig().getInt("progressBarAmount");
        this.soulwellEnabled = plugin.getConfig().getBoolean("rewards.soulwell");
        this.lobbyScoreboard = plugin.getConfig().getBoolean("lobbyScoreboard");
        this.mainLobby = plugin.getConfig().getBoolean("chat.mainLobby");
        this.chatSystem = plugin.getConfig().getBoolean("chat.games");
        this.broadcastLevelUp = plugin.getConfig().getBoolean("chat.broadcastLevelUp");
        this.broadcastGame = plugin.getConfig().getBoolean("chat.broadcastGame");
        this.slimeworldmanagerLoader = plugin.getConfig().getString("addons.slimeworldmanager.loader");
        this.slimeworldmanager = plugin.getConfig().getBoolean("addons.slimeworldmanager.enabled");
        this.AnimatedNames = plugin.getConfig().getBoolean("addons.AnimatedNames");
        this.trholograms = plugin.getConfig().getBoolean("addons.trholograms");
        this.partyandfriends = plugin.getConfig().getBoolean("addons.partyandfriends");
        this.leaderheads = plugin.getConfig().getBoolean("addons.leaderheads");
        this.parties = plugin.getConfig().getBoolean("addons.parties");
        this.nametagedit = plugin.getConfig().getBoolean("addons.nametagedit");
        this.holograms = plugin.getConfig().getBoolean("addons.holograms");
        this.holographicdisplays = plugin.getConfig().getBoolean("addons.holographicdisplays");
        this.perm = plugin.getConfig().getString("permCMD");
        this.MVdWPlaceholderAPI = plugin.getConfig().getBoolean("addons.MVdWPlaceholderAPI");
        this.placeholdersAPI = plugin.getConfig().getBoolean("addons.placeholdersAPI");
        this.tabapi = plugin.getConfig().getBoolean("addons.tabapi");
        this.vault = plugin.getConfig().getBoolean("addons.vault");
        this.playerpoints = plugin.getConfig().getBoolean("addons.playerpoints");
        this.coins = plugin.getConfig().getBoolean("addons.coins");
        this.signsRight = plugin.getConfig().getBoolean("signs.right");
        this.signsLeft = plugin.getConfig().getBoolean("signs.left");
        this.waitingState = (byte) plugin.getConfig().getInt("states.waiting");
        this.startingState = (byte) plugin.getConfig().getInt("states.starting");
        this.emptyState = (byte) plugin.getConfig().getInt("states.empty");
        this.naturalSpawnEggs = plugin.getConfig().getBoolean("gameDefaults.naturalSpawnEggs");
        this.maxItemsChest = plugin.getConfig().getInt("gameDefaults.maxItemsChest");
        this.maxMultiplier = plugin.getConfig().getInt("gameDefaults.maxMultiplier");
        this.starting = plugin.getConfig().getInt("gameDefaults.starting");
        this.pregame = plugin.getConfig().getInt("gameDefaults.pregame");
        this.coinsKill = plugin.getConfig().getInt("gameDefaults.coins.kill");
        this.coinsWin = plugin.getConfig().getInt("gameDefaults.coins.win");
        this.coinsAssists = plugin.getConfig().getInt("gameDefaults.coins.assists");
        this.xpKill = plugin.getConfig().getInt("gameDefaults.xp.kill");
        this.xpWin = plugin.getConfig().getInt("gameDefaults.xp.win");
        this.xpAssists = plugin.getConfig().getInt("gameDefaults.xp.assists");
        this.soulsKill = plugin.getConfig().getInt("gameDefaults.souls.kill");
        this.soulsWin = plugin.getConfig().getInt("gameDefaults.souls.win");
        this.soulsAssists = plugin.getConfig().getInt("gameDefaults.souls.assists");
        this.lobby = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.lobby")).orElse(XMaterial.DIAMOND);
        this.soulwell = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.soulwell")).orElse(XMaterial.WHITE_STAINED_GLASS);
        this.team = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.team")).orElse(XMaterial.NETHER_STAR);
        this.spectate = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.spectate")).orElse(XMaterial.COMPASS);
        this.options = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.options")).orElse(XMaterial.COMPARATOR);
        this.play = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.play")).orElse(XMaterial.PAPER);
        this.leave = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.leave")).orElse(XMaterial.RED_BED);
        this.kits = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.kits")).orElse(XMaterial.BOW);
        this.votes = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.votes")).orElse(XMaterial.MAP);
        this.setup = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.setup")).orElse(XMaterial.DIAMOND);
        this.center = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.center")).orElse(XMaterial.BLAZE_ROD);
        this.island = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.island")).orElse(XMaterial.BLAZE_POWDER);
        this.closeitem = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.closeitem")).orElse(XMaterial.BOOK);
        this.back = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.back")).orElse(XMaterial.ARROW);
        this.challenges = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.challenges")).orElse(XMaterial.BLAZE_POWDER);
        this.deselect = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.deselect")).orElse(XMaterial.BARRIER);
        this.next = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.next")).orElse(XMaterial.ARROW);
        this.last = XMaterial.matchXMaterial(plugin.getConfig().getString("materials.last")).orElse(XMaterial.ARROW);
    }

    public void reloadInjections() {
        if (plugin.getIjm() == null) return;
        if (plugin.getIjm().isCubeletsInjection()) {
            this.ticksAni3 = plugin.getIjm().getCubelets().getCubelets().getInt("animations.flames.ticks");
            this.executesAni3 = plugin.getIjm().getCubelets().getCubelets().getInt("animations.flames.executes");
            this.url = plugin.getIjm().getCubelets().getCubelets().get(null, "animations.head.url");
            this.head = ItemBuilder.createSkull("§7", "§7", plugin.getIjm().getCubelets().getCubelets().get(null, "animations.head.url"));
            this.upYAni1 = plugin.getIjm().getCubelets().getCubelets().getInt("animations.fireworks.upY");
            this.ticksAni1 = plugin.getIjm().getCubelets().getCubelets().getInt("animations.fireworks.ticks");
            this.cubeletChance = plugin.getIjm().getCubelets().getCubelets().getInt("gameDefaults.cubeletsChance");
            this.cubeletsEnabled = plugin.getIjm().getCubelets().getCubelets().getBoolean("cubelets");
        }
    }

    public boolean isBungeeModeLobby() {
        return true;
    }

    public boolean isSetupLobby(Player p) {
        return false;
    }

}