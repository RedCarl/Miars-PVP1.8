package gg.noob.lib.tab;

import gg.noob.lib.tab.entry.TabElementHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
public class TabHandler {

	// Prevent shit logics
	private Map<UUID, TabAdapter> players = new HashMap<>();
	
	private final Optional<TabAdapter> adapter;
	private final TabElementHandler handler;
	private final long ticks;
	
	private final TabUpdater updater;
	
	/**
	 * Constructor to make a new {@link TabHandler}
	 * 
	 * @param adapter the adapter to send the tab with
	 * @param handler the handler to get the elements from
	 * @param plugin  the plugin to register the thread and listener
	 * @param ticks   the amount it should update
	 */
	public TabHandler(TabAdapter adapter, TabElementHandler handler, JavaPlugin plugin, long ticks) {
		this.adapter = Optional.of(adapter);
		this.handler = handler;
		this.ticks = ticks;
		this.updater = new TabUpdater(this, ticks);

		Bukkit.getPluginManager().registerEvents(new TabListener(this), plugin);
	}
	
	/**
	 * Get the {@link TabAdapter} to registered it
	 * @return the current adapter instance
	 */
	public TabAdapter getAdapter() {
		return this.adapter.orElseThrow(() -> new IllegalStateException("TabAdapter instance is null"));
	}
	
	/**
	 * Get the {@link TabAdapter} of a player
	 * 
	 * @param player the player to get the {@link TabAdapter} object from
	 * @return the player tab adapter instance
	 */
	public TabAdapter getAdapter(Player player) {
		final UUID uuid = player.getUniqueId();
		
		if (this.players.containsKey(uuid)) {
			return this.players.get(uuid);
		}
		
		return this.players.put(uuid, getAdapter());
	}
	
	/**
	 * Remove a player from the {@link TabAdapter}
	 * 
	 * @param player the player to remove the {@link TabAdapter} object from
	 * @return the player tab adapter instance
	 */
	public TabAdapter removeAdapter(Player player) {
		return this.players.remove(player.getUniqueId());
	}
}
