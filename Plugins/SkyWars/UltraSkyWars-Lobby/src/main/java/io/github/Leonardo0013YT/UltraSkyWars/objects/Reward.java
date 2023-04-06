package io.github.Leonardo0013YT.UltraSkyWars.objects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.RewardType;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

@Getter
public class Reward {

    private String name;
    private RewardType rarity;
    private List<String> rewards, alreadyCommands;
    private double chance;
    private boolean useAlreadyCMD;
    private ItemStack icon;
    private UltraSkyWars plugin;
    private int id;

    public Reward(UltraSkyWars plugin, String path, int id) {
        this.plugin = plugin;
        this.id = id;
        this.rarity = RewardType.valueOf(plugin.getRewards().get(path + ".type"));
        this.rewards = plugin.getRewards().getList(path + ".cmds");
        this.useAlreadyCMD = plugin.getRewards().getBooleanOrDefault(path + ".useAlreadyCMD", false);
        this.alreadyCommands = plugin.getRewards().getListOrDefault(path + ".alreadyCommands", Collections.singletonList("eco give <player> 100"));
        this.name = plugin.getRewards().get(path + ".name");
        this.chance = plugin.getRewards().getConfig().getDouble(path + ".chance");
        if (plugin.getRewards().getConfig().get(path + ".icon") instanceof ItemStack) {
            icon = plugin.getRewards().getConfig().getItemStack(path + ".icon");
        } else {
            ItemStack head = NBTEditor.getHead(plugin.getRewards().getConfig().getString(path + ".icon"));
            ItemMeta headM = head.getItemMeta();
            headM.setDisplayName(plugin.getRewards().get(path + ".iconName"));
            headM.setLore(plugin.getRewards().getList(path + ".iconLore"));
            head.setItemMeta(headM);
            icon = head;
        }
        if (icon.getAmount() < 1) {
            icon.setAmount(1);
        }
        this.icon = NBTEditor.set(icon, id, "REWARD", "ID");
    }

    public void execute(Player p) {
        for (String cmd : rewards) {
            if (useAlreadyCMD && cmd.startsWith("/sw ")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getAdm().parsePlaceholders(p, cmd.replaceAll("<player>", p.getName())) + " " + id);
            } else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getAdm().parsePlaceholders(p, cmd.replaceAll("<player>", p.getName())));
            }
        }
    }

    public void executeSecond(Player p) {
        p.sendMessage(plugin.getLang().get("messages.alreadyReward"));
        for (String cmd : alreadyCommands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getAdm().parsePlaceholders(p, cmd.replaceAll("<player>", p.getName())));
        }
    }

}