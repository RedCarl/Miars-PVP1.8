package io.github.Leonardo0013YT.UltraSkyWars.managers;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;

public class ItemManager {

    private UltraSkyWars plugin;
    private ItemStack challenges, lobby, team, setup, center, island, votes, kits, leave, spectate, options, play;

    public ItemManager(UltraSkyWars plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        this.challenges = ItemBuilder.item(plugin.getCm().getChallenges(), 1, plugin.getLang().get("items.challenges.nameItem"), plugin.getLang().get("items.challenges.loreItem"));
        this.lobby = ItemBuilder.item(plugin.getCm().getLobby(), 1, plugin.getLang().get("items.lobby.nameItem"), plugin.getLang().get("items.lobby.loreItem"));
        this.team = ItemBuilder.item(plugin.getCm().getTeam(), 1, plugin.getLang().get("items.team.nameItem"), plugin.getLang().get("items.team.loreItem"));
        this.spectate = ItemBuilder.item(plugin.getCm().getSpectate(), 1, plugin.getLang().get("items.spectate.nameItem"), plugin.getLang().get("items.spectate.loreItem"));
        this.options = ItemBuilder.item(plugin.getCm().getOptions(), 1, plugin.getLang().get("items.options.nameItem"), plugin.getLang().get("items.options.loreItem"));
        this.play = ItemBuilder.item(plugin.getCm().getPlay(), 1, plugin.getLang().get("items.play.nameItem"), plugin.getLang().get("items.play.loreItem"));
        this.leave = ItemBuilder.item(plugin.getCm().getLeave(), 1, plugin.getLang().get("items.leave.nameItem"), plugin.getLang().get("items.leave.loreItem"));
        this.kits = ItemBuilder.item(plugin.getCm().getKits(), 1, plugin.getLang().get("items.kits.nameItem"), plugin.getLang().get("items.kits.loreItem"));
        this.votes = ItemBuilder.item(plugin.getCm().getVotes(), 1, plugin.getLang().get("items.votes.nameItem"), plugin.getLang().get("items.votes.loreItem"));
        this.setup = ItemBuilder.item(plugin.getCm().getSetup(), 1, plugin.getLang().get("items.setup.nameItem"), plugin.getLang().get("items.setup.loreItem"));
        this.center = ItemBuilder.item(plugin.getCm().getCenter(), 1, plugin.getLang().get("items.center.nameItem"), plugin.getLang().get("items.center.loreItem"));
        this.island = ItemBuilder.item(plugin.getCm().getIsland(), 1, plugin.getLang().get("items.island.nameItem"), plugin.getLang().get("items.island.loreItem"));

    }

    public ItemStack getChallenges() {
        return challenges;
    }

    public ItemStack getLobby() {
        return lobby;
    }

    public ItemStack getTeam() {
        return team;
    }

    public ItemStack getSpectate() {
        return spectate;
    }

    public ItemStack getOptions() {
        return options;
    }

    public ItemStack getPlay() {
        return play;
    }

    public ItemStack getLeave() {
        return leave;
    }

    public ItemStack getKits() {
        return kits;
    }

    public ItemStack getVotes() {
        return votes;
    }

    public ItemStack getIsland() {
        return island;
    }

    public ItemStack getCenter() {
        return center;
    }

    public ItemStack getSetup() {
        return setup;
    }
}