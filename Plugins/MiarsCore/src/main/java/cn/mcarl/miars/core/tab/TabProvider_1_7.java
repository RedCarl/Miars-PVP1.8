package cn.mcarl.miars.core.tab;

import gg.noob.lib.tab.entry.TabElement;
import gg.noob.lib.tab.entry.TabElementHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TabProvider_1_7 implements TabElementHandler {

    /**
     * Get the tab element of a player
     *
     * @param player the player
     * @return the element
     */
    @Override
    public TabElement getElement(Player player) {
        final TabElement element = new TabElement();

        element.add(0, 0, ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "kazer.cc");
        element.add(1, 0, ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Practice");
        element.add(2, 0, ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "kazer.cc");
        element.add(0, 1, "&7Online: " + Bukkit.getOnlinePlayers().size());
        element.add(1, 1, "&7In Queue: " + 0);
        element.add(2, 1, "&7Playing: " + 0);

        return element;
    }

}
