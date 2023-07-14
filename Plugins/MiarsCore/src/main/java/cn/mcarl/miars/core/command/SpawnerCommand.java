package cn.mcarl.miars.core.command;

import cn.mcarl.miars.storage.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpawnerCommand implements CommandExecutor, TabCompleter {

    String[] mobs = {
            "ZOMBIE",
            "SKELETON",
            "HORSE",
            "SHEEP",
            "ENDERMITE",
            "SLIME",
            "PIG",
            "VILLAGER",
            "GHAST",
            "BAT",
            "CREEPER",
            "CHICKEN",
            "RABBIT",
            "PIG_ZOMBIE",
            "MAGMA_CUBE",
            "GUARDIAN",
            "ENDERMAN",
            "SILVERFISH",
            "SPIDER",
            "SQUID",
            "SNOWMAN",
            "WOLF",
            "MUSHROOM_COW",
            "COW",
            "CAVE_SPIDER",
            "GIANT",
            "WITCH",
            "IRON_GOLEM",
            "OCELOT"


    };
    @Override
    public boolean onCommand(
            @NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {

        if (sender instanceof Player player){
            if (player.hasPermission("miars.admin")){
                if (args.length>=2){
                    Player p = Bukkit.getPlayerExact(args[0]);

                    String spawnerName = args[1];

                    int amount = 1;

                    if (args.length==3){
                        amount = Integer.parseInt(args[2]);
                    }

                    ItemStack item = getSpawnerItem(amount,EntityType.valueOf(spawnerName.toUpperCase()));

                    p.getInventory().addItem(item);
                }
            }
        }

        return false;
    }

    public static ItemStack getSpawnerItem(int amount, EntityType type) {
        ItemStack item = new ItemStack(Material.MOB_SPAWNER, amount);
        List<String> lore = new ArrayList<>();

        String loreString = type.toString();
        loreString = loreString.substring(0, 1).toUpperCase() + loreString.substring(1).toLowerCase();
        loreString = loreString+" Spawner";
        lore.add(loreString);

        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String alias,
            @NotNull String[] args
    ) {
        List<String> allCompletes = new ArrayList<>();

        if (sender instanceof Player player) {
            // 玩家指令部分
            if (player.hasPermission("miars.admin")) {

                if (args.length==1){
                    for (Player p:Bukkit.getOnlinePlayers()) {
                        allCompletes.add(p.getName());
                    }
                }

                if (args.length==2){
                    allCompletes.addAll(Arrays.asList(mobs));
                }

                if (args.length==3){
                    allCompletes.add("1");
                }

            }
        }

        return allCompletes.stream()
                .filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
                .limit(10).collect(Collectors.toList());
    }
}