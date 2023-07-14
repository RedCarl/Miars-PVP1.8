package dev.fls.tablist;

import dev.fls.tablist.page.OptionalPart;
import dev.fls.tablist.page.parts.Footer;
import dev.fls.tablist.page.parts.Header;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TabHeaderAndFooter {

    private static JavaPlugin plugin;

    public TabHeaderAndFooter(JavaPlugin pl) {
        plugin = pl;
    }

    private final Header header = new Header();
    private final Footer footer = new Footer();
    private final List<UUID> displayedTo = new ArrayList<>();

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public void show(Player player) {
        displayedTo.add(player.getUniqueId());
        OptionalPart.sendPacket(player, header, footer);
    }

    public void hide(Player player) {
        displayedTo.remove(player.getUniqueId());
        OptionalPart.sendPacket(player, new Header(), new Footer());
    }
}
