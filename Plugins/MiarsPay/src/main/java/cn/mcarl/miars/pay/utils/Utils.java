
package cn.mcarl.miars.pay.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils
{
    public static ItemStack createItem(final Material material, final String name, final List<String> lorries) {
        return createItem(material, name, lorries, new HashMap<>(0));
    }
    
    public static ItemStack createItem(final Material material, final String name, final List<String> lorries, final Map<String, Object> replace) {
        final ItemStack itemStack = new ItemStack(material);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(parse(name, replace));
        itemMeta.setLore((List)parse(lorries, replace));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static List<String> parse(final List<String> list, final Map<String, Object> replace) {
        return parse(list, replace, null);
    }
    
    public static List<String> parse(final List<String> list, final Map<String, Object> replace, final Player player) {
        final List<String> newList = new ArrayList<String>();
        list.forEach(e -> newList.add(parse(e, replace, player)));
        return newList;
    }
    
    public static String parse(final String string, final Map<String, Object> replace) {
        return parse(string, replace, null);
    }
    
    public static String parse(String string, final Map<String, Object> replace, final Player player) {
        if (!replace.isEmpty()) {
            for (final Map.Entry<String, Object> entry : replace.entrySet()) {
                string = string.replace(entry.getKey(), entry.getValue().toString());
            }
        }
        if (player != null) {
            string = PlaceholderAPI.setPlaceholders(player, string);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
