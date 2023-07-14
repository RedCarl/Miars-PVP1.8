package gg.noob.lib.tab;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import gg.noob.lib.tab.client.ClientVersionUtil;
import gg.noob.lib.tab.entry.TabElement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TabUpdater implements Runnable {

	private final TabHandler handler;
	private final ScheduledThreadPoolExecutor executor;
	private final ScheduledFuture<?> updater;
	public Map<UUID, TabElement> lastUpdate = new HashMap<>();

	/**
	 * Constructor to make a new {@link TabUpdater}
	 *
	 * @param handler the handler to register it to
	 * @param ticks   the amount it should update
	 */
	public TabUpdater(TabHandler handler, long ticks) {
		this.handler = handler;

		this.executor = new ScheduledThreadPoolExecutor(1,
				new ThreadFactoryBuilder().setNameFormat("Tab Thread %s").setDaemon(true).build());
		this.executor.setRemoveOnCancelPolicy(true);

		this.updater = this.executor.scheduleAtFixedRate(this, 20L, ticks, TimeUnit.MILLISECONDS);
	}

	@Override
	public void run() {
		try {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (ClientVersionUtil.getProtocolVersion(player)!=5){
					continue;
				}
				sendUpdate(player);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update the tablist for a player
	 * 
	 * @param player the player to update it for
	 */
	public void sendUpdate(Player player) {
		final TabElement element = this.handler.getHandler().getElement(player);
		final TabAdapter adapter = this.handler.getAdapter(player);
		final TabElement lastElement = this.lastUpdate.get(player.getUniqueId());

		if (adapter != null) {
			TabAdapter tabAdapter = adapter.handleElement(player, element, lastElement);

			if (lastElement != null) {
//				if (
//						!element.getHeader().get(0).equalsIgnoreCase(lastElement.getHeader().get(0)) ||
//						!element.getFooter().get(0).equalsIgnoreCase(lastElement.getFooter().get(0))
//				) {
//					tabAdapter.sendHeaderFooter(player, element.getHeader(), element.getFooter());
//				}
			}

			this.lastUpdate.put(player.getUniqueId(), element);
		}

	}

	/**
	 * Stop tab thread
	 */
	public void cancelExecutors() {
		if (updater != null) {
			updater.cancel(true);
		}

		if (executor != null) {
			executor.shutdown();
		}
	}
}
