package cn.mcarl.miars.core.hooker;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.core.utils.MiarsUtils;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class PAPIExpansion extends PlaceholderExpansion {

	private static final List<String> PLACEHOLDERS = Arrays.asList(
			"%MiarsCore_prefix%",
			"%MiarsCore_prefix_<player>%",
			"%MiarsCore_suffix%",
			"%MiarsCore_suffix_<player>%",
			"%MiarsCore_nameColor%",
			"%MiarsCore_nameColor_<player>%",
			"%MiarsCore_name_<papi>%",
			"%MiarsCore_server_code%",
			"%MiarsCore_online_<server>%"
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
					if (args.length==2){
						String name = args[1].toLowerCase();
						OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
						MPlayer mp = MPlayerDataStorage.getInstance().getMPlayer(offlinePlayer);
						MRank d = MRankDataStorage.getInstance().getMRank(mp.getRank());
						return ColorParser.parse(d.getPrefix());
					}
					return ColorParser.parse(prefix);
				}
			}
			case "suffix" -> {
				String suffix = data.getSuffix();
				if (suffix == null) {
					return "Depository or Item not exists";
				} else {
					if (args.length==2){
						String name = args[1].toLowerCase();
						OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
						MPlayer mp = MPlayerDataStorage.getInstance().getMPlayer(offlinePlayer);
						MRank d = MRankDataStorage.getInstance().getMRank(mp.getRank());
						return ColorParser.parse(d.getSuffix());
					}
					return ColorParser.parse(suffix);
				}
			}
			case "namecolor" -> {
				String nameColor = data.getNameColor();
				if (nameColor == null) {
					return "Depository or Item not exists";
				} else {
					if (args.length==2){
						String name = args[1].toLowerCase();
						OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
						MPlayer mp = MPlayerDataStorage.getInstance().getMPlayer(offlinePlayer);
						MRank d = MRankDataStorage.getInstance().getMRank(mp.getRank());
						return ColorParser.parse(d.getNameColor());
					}
					return ColorParser.parse(nameColor);
				}
			}
			case "version" -> {
				return getVersion();
			}
			case "server" -> {
				switch (args[1].toLowerCase()){
					case "code" -> {
						return MiarsUtils.getServerCode();
					}
				}
				return getVersion();
			}
			case "online" -> {
				if (args.length==2) {
					String server = args[1].toLowerCase();
					return String.valueOf(ServerManager.getInstance().getServerOnline(server));
				}
				return "ERROR";
			}
			case "name" -> {
				if (args.length==2) {
					String papi = "%"+(args[1].toLowerCase().replace("-","_"))+"%";
					String name = PlaceholderAPI.setPlaceholders(player,papi);
					OfflinePlayer p = Bukkit.getOfflinePlayer(name);
					MPlayer var = MPlayerDataStorage.getInstance().getMPlayer(p);
					MRank var1 = MRankDataStorage.getInstance().getMRank(var.getRank());
					return ColorParser.parse(var1.getNameColor()+name);
				}
				return "ERROR";
			}
			default -> {
				return "参数错误";
			}
		}
	}

}