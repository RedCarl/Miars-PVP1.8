package cn.mcarl.miars.skypvp.hooker;

import cn.mcarl.miars.skypvp.entitiy.GamePlayer;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.FPlayerDataStorage;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class PAPIExpansion extends PlaceholderExpansion {

	private static final List<String> PLACEHOLDERS = Arrays.asList(
			"%MiarsSkyPVP_kills%",
			"%MiarsSkyPVP_death%",
			"%MiarsSkyPVP_level%",
			"%MiarsSkyPVP_lucky%",
			"%MiarsSkyPVP_kd%"
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

		GamePlayer gamePlayer = GamePlayer.get(player);

		switch (args[0].toLowerCase()) {
			case "kills" -> {
				return String.valueOf(gamePlayer.getSPlayer().getKillsCount());
			}
			case "death" -> {
				return String.valueOf(gamePlayer.getSPlayer().getDeathCount());
			}
			case "level" -> {
				return String.valueOf(gamePlayer.getLevel());
			}
			case "lucky" -> {
				return String.valueOf(gamePlayer.getSPlayer().getLucky());
			}
			case "kd" -> {
				return String.valueOf(gamePlayer.getKd());
			}
			default -> {
				return "参数错误";
			}
		}
	}

}