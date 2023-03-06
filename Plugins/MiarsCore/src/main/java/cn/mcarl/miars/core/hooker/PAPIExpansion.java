package cn.mcarl.miars.core.hooker;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class PAPIExpansion extends PlaceholderExpansion {

	private static final List<String> PLACEHOLDERS = Arrays.asList(
			"%MiarsCore_prefix%",
			"%MiarsCore_suffix%",
			"%MiarsCore_nameColor%"
	);

	private final JavaPlugin plugin;

	public PAPIExpansion(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public @NotNull List<String> getPlaceholders() {
		return PLACEHOLDERS;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public @NotNull String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	@Override
	public @NotNull String getIdentifier() {
		return plugin.getDescription().getName();
	}

	@Override
	public @NotNull String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public String onPlaceholderRequest(Player player, @NotNull String identifier) {
		if (player == null) {
			return "加载中...";
		}
		String[] args = identifier.split("_");

		MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(player);

		MRank data = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());

		switch (args[0].toLowerCase()) {
			case "prefix" -> {
				String prefix = data.getPrefix();
				if (prefix == null) {
					return "Depository or Item not exists";
				} else {
					return ColorParser.parse(prefix);
				}
			}
			case "suffix" -> {
				String suffix = data.getSuffix();
				if (suffix == null) {
					return "Depository or Item not exists";
				} else {
					return ColorParser.parse(suffix);
				}
			}
			case "namecolor" -> {
				String nameColor = data.getNameColor();
				if (nameColor == null) {
					return "Depository or Item not exists";
				} else {
					return ColorParser.parse(nameColor);
				}
			}
			case "version" -> {
				return getVersion();
			}
			default -> {
				return "参数错误";
			}
		}
	}

}