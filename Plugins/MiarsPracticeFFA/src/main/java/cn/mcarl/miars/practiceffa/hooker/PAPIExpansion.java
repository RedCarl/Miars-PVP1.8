package cn.mcarl.miars.practiceffa.hooker;

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
			"%practice_ffa_kills%",
			"%practice_ffa_death%",
			"%practice_ffa_level%"
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

		FPlayer fPlayer = FPlayerDataStorage.getInstance().getFPlayer(player);

		switch (args[0].toLowerCase()) {
			case "ffa" -> {
				String prefix = data.getPrefix();
				if (prefix == null) {
					return "Depository or Item not exists";
				} else {
					if (args.length==2){
						switch (args[1].toLowerCase()){
							case "kills" -> {
								return String.valueOf(fPlayer.getKillsCount());
							}
							case "death" -> {
								return String.valueOf(fPlayer.getDeathCount());
							}
						}
					}
					return "参数错误";
				}
			}
			default -> {
				return "参数错误";
			}
		}
	}

}