package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.*;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.killeffects.UltraKillEffect;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.taunts.Taunt;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.windances.UltraWinDance;
import io.github.Leonardo0013YT.UltraSkyWars.cosmetics.wineffects.UltraWinEffect;
import io.github.Leonardo0013YT.UltraSkyWars.objects.PreviewCosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Cosmetic;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class CosmeticManager {

    private final HashMap<Integer, Glass> glasses = new HashMap<>();
    private final HashMap<Integer, UltraKillEffect> killEffect = new HashMap<>();
    private final HashMap<Integer, Balloon> balloons = new HashMap<>();
    private final HashMap<Integer, Balloon> animatedBalloons = new HashMap<>();
    private final HashMap<Integer, KillSound> killSounds = new HashMap<>();
    private final HashMap<Integer, Parting> partings = new HashMap<>();
    private final HashMap<Integer, Taunt> taunts = new HashMap<>();
    private final HashMap<Integer, Trail> trails = new HashMap<>();
    private final HashMap<Integer, UltraWinDance> winDance = new HashMap<>();
    private final HashMap<Integer, UltraWinEffect> winEffects = new HashMap<>();
    private final HashMap<String, Integer> lastPage = new HashMap<>();
    private final HashMap<String, PreviewCosmetic> previews = new HashMap<>();
    private final HashMap<UUID, String> playerPreview = new HashMap<>();
    private final UltraSkyWars plugin;

    public CosmeticManager(UltraSkyWars plugin) {
        this.plugin = plugin;
    }

    public void remove(Player p) {
        if (playerPreview.containsKey(p.getUniqueId())) {
            PreviewCosmetic pc = getPreviewCosmetic(playerPreview.get(p.getUniqueId()));
            pc.removePreview(p);
            playerPreview.remove(p.getUniqueId());
        }
    }

    public PreviewCosmetic getPreviewCosmetic(String type) {
        return previews.get(type);
    }

    public HashMap<UUID, String> getPlayerPreview() {
        return playerPreview;
    }

    public int getLastPage(String type) {
        return lastPage.getOrDefault(type, 0);
    }

    public void setLastPage(String type, int lastPage) {
        if (getLastPage(type) < lastPage) {
            this.lastPage.put(type, lastPage);
        }
    }

    public void reload() {
        if (plugin.getCm().getPreviewPlayerGlass() != null && plugin.getCm().getPreviewCosmeticGlass() != null) {
            previews.put("glass", new PreviewCosmetic("glass", plugin.getCm().getPreviewPlayerGlass(), plugin.getCm().getPreviewCosmeticGlass()));
        }
        if (plugin.getCm().getPreviewPlayerBalloon() != null && plugin.getCm().getPreviewCosmeticBalloon() != null) {
            previews.put("balloon", new PreviewCosmetic("balloon", plugin.getCm().getPreviewPlayerBalloon(), plugin.getCm().getPreviewCosmeticBalloon()));
        }
        glasses.clear();
        killEffect.clear();
        balloons.clear();
        killSounds.clear();
        taunts.clear();
        trails.clear();
        winDance.clear();
        winEffects.clear();
        partings.clear();
        if (plugin.getCm().isCosmeticsPartings() && plugin.getParting().isSet("partings")) {
            ConfigurationSection conf = plugin.getParting().getConfig().getConfigurationSection("partings");
            for (String c : conf.getKeys(false)) {
                int id = plugin.getParting().getInt("partings." + c + ".id");
                partings.put(id, new Parting(plugin, "partings." + c));
                plugin.sendDebugMessage("§aParting §b" + c + "§a loaded correctly.");
            }
        }
        if (plugin.getWineffect().isSet("wineffects")) {
            ConfigurationSection conf = plugin.getWineffect().getConfig().getConfigurationSection("wineffects");
            for (String c : conf.getKeys(false)) {
                int id = plugin.getWineffect().getInt("wineffects." + c + ".id");
                if (id != 0 && !plugin.getCm().isCosmeticsWinEffects()) continue;
                winEffects.put(id, new UltraWinEffect(plugin, "wineffects." + c));
                plugin.sendDebugMessage("§aWinEffect §b" + c + "§a loaded correctly.");
            }
        }
        if (plugin.getCm().isCosmeticsWinDance() && plugin.getWindance().isSet("windances")) {
            ConfigurationSection conf = plugin.getWindance().getConfig().getConfigurationSection("windances");
            for (String c : conf.getKeys(false)) {
                int id = plugin.getWindance().getInt("windances." + c + ".id");
                winDance.put(id, new UltraWinDance(plugin, "windances." + c));
                plugin.sendDebugMessage("§aWinDance §b" + c + "§a loaded correctly.");
            }
        }
        if (plugin.getCm().isCosmeticsTrails() && plugin.getTrail().isSet("trails")) {
            ConfigurationSection conf = plugin.getTrail().getConfig().getConfigurationSection("trails");
            for (String c : conf.getKeys(false)) {
                int id = plugin.getTrail().getInt("trails." + c + ".id");
                trails.put(id, new Trail(plugin, "trails." + c));
                plugin.sendDebugMessage("§aTrail §b" + c + "§a loaded correctly.");
            }
        }
        if (plugin.getTaunt().isSet("taunts")) {
            ConfigurationSection conf = plugin.getTaunt().getConfig().getConfigurationSection("taunts");
            for (String c : conf.getKeys(false)) {
                int id = plugin.getTaunt().getInt("taunts." + c + ".id");
                if (id != 0 && !plugin.getCm().isCosmeticsTaunts()) continue;
                taunts.put(id, new Taunt(plugin, "taunts." + c));
                plugin.sendDebugMessage("§aTaunt §b" + c + "§a loaded correctly.");
            }
        }
        if (plugin.getCm().isCosmeticsKillSounds() && plugin.getKillsound().isSet("killsounds")) {
            ConfigurationSection conf = plugin.getKillsound().getConfig().getConfigurationSection("killsounds");
            for (String c : conf.getKeys(false)) {
                int id = plugin.getKillsound().getInt("killsounds." + c + ".id");
                killSounds.put(id, new KillSound(plugin, "killsounds." + c));
                plugin.sendDebugMessage("§aKillSound §b" + c + "§a loaded correctly.");
            }
        }
        if (plugin.getCm().isCosmeticsBalloons() && plugin.getBalloon().isSet("balloons")) {
            ConfigurationSection conf = plugin.getBalloon().getConfig().getConfigurationSection("balloons");
            for (String c : conf.getKeys(false)) {
                int id = plugin.getBalloon().getInt("balloons." + c + ".id");
                Balloon balloon = new Balloon(plugin, "balloons." + c);
                if (balloon.getHeads().isEmpty()) continue;
                if (balloon.isAnimated()){
                    animatedBalloons.put(id, balloon);
                } else {
                    balloons.put(id, balloon);
                }
                plugin.sendDebugMessage("§aBalloon §b" + c + "§a loaded correctly.");
            }
        }
        if (plugin.getCm().isCosmeticsKillEffect() && plugin.getKilleffect().isSet("killeffects")) {
            ConfigurationSection conf = plugin.getKilleffect().getConfig().getConfigurationSection("killeffects");
            for (String c : conf.getKeys(false)) {
                int id = plugin.getKilleffect().getInt("killeffects." + c + ".id");
                killEffect.put(id, new UltraKillEffect(plugin, "killeffects." + c));
                plugin.sendDebugMessage("§aKillEffect §b" + c + "§a loaded correctly.");
            }
        }
        if (plugin.getGlass().isSet("glasses")) {
            ConfigurationSection conf = plugin.getGlass().getConfig().getConfigurationSection("glasses");
            for (String c : conf.getKeys(false)) {
                int id = plugin.getGlass().getInt("glasses." + c + ".id");
                if (id != 0 && !plugin.getCm().isCosmeticsGlasses()) continue;
                glasses.put(id, new Glass(plugin, "glasses." + c));
                plugin.sendDebugMessage("§aGlass §b" + c + "§a loaded correctly.");
            }
        }
    }

    public int getPartingsSize() {
        return partings.size();
    }

    public int getNextPartingId() {
        return partings.size();
    }

    public int getNextTauntsId() {
        return taunts.size();
    }

    public int getNextGlassId() {
        return glasses.size();
    }

    public int getNextKillSoundId() {
        return killSounds.size();
    }

    public int getNextBalloonsId() {
        return balloons.size() + animatedBalloons.size();
    }

    public int getNextTrailsId() {
        return trails.size();
    }

    public Glass getGlass(int id) {
        if (id == 999999) {
            return glasses.get(0);
        }
        return glasses.get(id);
    }

    public Balloon getBalloon(int id) {
        if (animatedBalloons.containsKey(id)){
            return animatedBalloons.get(id);
        }
        return balloons.get(id);
    }

    public Glass getGlassByItem(ItemStack item) {
        if (!NBTEditor.contains(item, "ULTRASKYWARS", "GLASS")) {
            return null;
        }
        int id = NBTEditor.getInt(item, "ULTRASKYWARS", "GLASS");
        return glasses.getOrDefault(id, null);
    }

    public UltraKillEffect getKillEffectByItem(ItemStack item) {
        if (!NBTEditor.contains(item, "ULTRASKYWARS", "KILLEFFECT")) {
            return null;
        }
        int id = NBTEditor.getInt(item, "ULTRASKYWARS", "KILLEFFECT");
        return killEffect.getOrDefault(id, null);
    }

    public HashMap<Integer, Parting> getPartings() {
        return partings;
    }

    public Parting getPartingByItem(ItemStack item) {
        if (!NBTEditor.contains(item, "ULTRASKYWARS", "PARTING")) {
            return null;
        }
        int id = NBTEditor.getInt(item, "ULTRASKYWARS", "PARTING");
        return partings.getOrDefault(id, null);
    }


    public KillSound getKillSoundByItem(ItemStack item) {
        if (!NBTEditor.contains(item, "ULTRASKYWARS", "KILLSOUND")) {
            return null;
        }
        int id = NBTEditor.getInt(item, "ULTRASKYWARS", "KILLSOUND");
        return killSounds.getOrDefault(id, null);
    }

    public Balloon getBalloonByItem(ItemStack item) {
        if (!NBTEditor.contains(item, "ULTRASKYWARS", "BALLOON")) {
            return null;
        }
        int id = NBTEditor.getInt(item, "ULTRASKYWARS", "BALLOON");
        if (animatedBalloons.containsKey(id)){
            return animatedBalloons.getOrDefault(id, null);
        }
        return balloons.getOrDefault(id, null);
    }

    public Taunt getTauntByItem(ItemStack item) {
        if (!NBTEditor.contains(item, "ULTRASKYWARS", "TAUNT")) {
            return null;
        }
        int id = NBTEditor.getInt(item, "ULTRASKYWARS", "TAUNT");
        return taunts.getOrDefault(id, null);
    }

    public Trail getTrailByItem(ItemStack item) {
        if (!NBTEditor.contains(item, "ULTRASKYWARS", "TRAIL")) {
            return null;
        }
        int id = NBTEditor.getInt(item, "ULTRASKYWARS", "TRAIL");
        return trails.getOrDefault(id, null);
    }

    public UltraWinDance getWinDanceByItem(ItemStack item) {
        if (!NBTEditor.contains(item, "ULTRASKYWARS", "WINDANCE")) {
            return null;
        }
        int id = NBTEditor.getInt(item, "ULTRASKYWARS", "WINDANCE");
        return winDance.getOrDefault(id, null);
    }

    public UltraWinEffect getWinEffectByItem(ItemStack item) {
        if (!NBTEditor.contains(item, "ULTRASKYWARS", "WINEFFECT")) {
            return null;
        }
        int id = NBTEditor.getInt(item, "ULTRASKYWARS", "WINEFFECT");
        return winEffects.getOrDefault(id, null);
    }

    public int getGlassesSize() {
        return glasses.size();
    }

    public int getKillEffectSize() {
        return killEffect.size();
    }

    public int getKillSoundsSize() {
        return killSounds.size();
    }

    public int getTauntsSize() {
        return taunts.size();
    }

    public int getBalloonSize() {
        return balloons.size() + animatedBalloons.size();
    }

    public int getWinDancesSize() {
        return winDance.size();
    }

    public int getTrailsSize() {
        return trails.size();
    }

    public int getWinEffectsSize() {
        return winEffects.size();
    }

    public HashMap<Integer, Glass> getGlasses() {
        return glasses;
    }

    public HashMap<Integer, UltraKillEffect> getKillEffect() {
        return killEffect;
    }

    public HashMap<Integer, KillSound> getKillSounds() {
        return killSounds;
    }

    public HashMap<Integer, Balloon> getBalloons() {
        HashMap<Integer, Balloon> balloon = new HashMap<>(balloons);
        balloon.putAll(animatedBalloons);
        return balloon;
    }

    public HashMap<Integer, Taunt> getTaunts() {
        return taunts;
    }

    public HashMap<Integer, Trail> getTrails() {
        return trails;
    }

    public HashMap<Integer, UltraWinDance> getWinDance() {
        return winDance;
    }

    public HashMap<Integer, UltraWinEffect> getWinEffects() {
        return winEffects;
    }

    public String getSelected(int id, HashMap<Integer, ? extends Cosmetic> map) {
        if (map.containsKey(id)) {
            return map.get(id).getName();
        }
        return plugin.getLang().get("messages.noSelected");
    }

}