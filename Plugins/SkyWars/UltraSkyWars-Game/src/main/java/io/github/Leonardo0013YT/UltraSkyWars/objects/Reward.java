package io.github.Leonardo0013YT.UltraSkyWars.objects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.RewardType;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
public class Reward {

    private String name;
    private RewardType rarity;
    private List<String> rewards;
    private double chance;
    private ItemStack icon;
    private int id;

    public Reward(UltraSkyWars plugin, String path, int id) {
        this.id = id;
        this.rarity = RewardType.valueOf(plugin.getRewards().get(path + ".type"));
        this.rewards = plugin.getRewards().getList(path + ".cmds");
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
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("<player>", p.getName()));
        }
    }

}