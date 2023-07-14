package gg.noob.lib.tab;

import gg.noob.lib.tab.client.ClientVersionUtil;
import gg.noob.lib.tab.entry.TabElement;
import gg.noob.lib.tab.entry.TabEntry;
import gg.noob.lib.tab.skin.SkinType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class TabAdapter {

	public List<Player> initialized = new ArrayList<Player>();

	/**
     * Handle an element being send to a player
     *
     * @param player  the player
     * @param element the element to send
     */
	public TabAdapter handleElement(Player player, TabElement element, TabElement lastElement) {
		for (int column = 0; column < 4; column++) {
			for (int row = 0; row < 20; row++) {
				final TabEntry entry = element.getEntry(column, row);
				final int index = getIndexOf(column, row, 20);

				if (lastElement != null) {
					if (lastElement.getEntry(column, row).getText().equalsIgnoreCase(entry.getText())) {
						continue;
					}
				}

				sendEntryData(player, entry.getColumn(), entry.getRow(), entry.getText(), entry.getPing());

				if (entry.getSkinData() != null && entry.getSkinData().length > 1) {
					updateSkin(ClientVersionUtil.getProtocolVersion(player) >= 47 ? entry.getSkinData() : SkinType.DARK_GRAY.getSkinData(), index, player); // Replace
				}
			}
		}

		return this;
	}
	
	/**
     * Split the text to display on the tablist
     *
     * @param text the text to split
     * @return the split text
     */
    public String[] splitText(String text) {
    	String prefix = text;
		String suffix = "";

		if (text.length() > 16) {
			prefix = text.substring(0, 16);

			if (prefix.charAt(15) == ChatColor.COLOR_CHAR) {
				prefix = prefix.substring(0, 15);
				suffix = text.substring(15, text.length());
			} else if (prefix.charAt(14) == ChatColor.COLOR_CHAR) {
				prefix = prefix.substring(0, 14);
				suffix = text.substring(14, text.length());
			} else {
				suffix = ChatColor.getLastColors(prefix) + text.substring(16, text.length());
			}
		}

		if (suffix.length() > 16) {
			suffix = suffix.substring(0, 16);
		}
		
		return new String[] { prefix, suffix };
    }
	
	/**
     * Send an entry's data to a player
     *
     * @param player 	the player
     * @param column	the column axis of the entry
     * @param row		the row axis of the entry
     * @param text  	the text to display on the entry's position
     * @param ping 	 	the ping to display on the entry's position
     * @return the current adapter instance
     */
	public abstract TabAdapter sendEntryData(Player player, int column, int row, String text, int ping);
	
	/**
     * Update the skin on the tablist for a player
     *
     * @param skinData the data of the new skin
     * @param index    the index of the profile
     * @param player   the player to update the skin for
     */
	public abstract void updateSkin(String[] skinData, int index, Player player);
	
	/**
     * Add fake players to the player's tablist
     *
     * @param player the player to send the fake players to
     * @return the current adapter instance
     */
	public abstract TabAdapter addFakePlayers(Player player);

	/**
	 * Remove fake players to the player's tablist
	 *
	 * @param player the player to remove the fake players from
	 * @return the current adapter instance
	 */
	public abstract TabAdapter removeFakePlayers(Player player);

    /**
     * Send the header and footer to a player
     *
     * @param player the player to send the header and footer to
     * @param header the header to send
     * @param footer the footer to send
     * @return the current adapter instance
     */
    public abstract TabAdapter sendHeaderFooter(Player player, List<String> header, List<String> footer);
    
    /**
     * Setup the scoreboard for the player
     *
     * @param player the player to setup the scoreboard packet for
     * @return the current adapter instance
     */
    public abstract TabAdapter setupScoreboard(Player player, boolean toggle);
	
	/**
     * Get the index of the tab entry (e. 0-60, 0-80)
     * 
     * @param column the column axis
     * @param row the row axis
     * @param dif the dif multiplier
     *
     * @return the index entry
     */
    public int getIndexOf(int column, int row, int dif) {
    	return row + column * 20;
    }

    /**
     * Get the color entry to get the tab team names
     * 
     * @param index the index to transform color
     * @return the color entry
     */
	public String getEntry(int index) {
		return ChatColor.BOLD.toString() + ChatColor.GREEN.toString() + ChatColor.UNDERLINE.toString() +
				ChatColor.YELLOW.toString() +
				(index >= 10 ? ChatColor.COLOR_CHAR + String.valueOf(index / 10) +
						ChatColor.COLOR_CHAR + String.valueOf(index % 10)
						: ChatColor.BLACK.toString() +
						ChatColor.COLOR_CHAR + String.valueOf(index)) + ChatColor.RESET;
	}
}
