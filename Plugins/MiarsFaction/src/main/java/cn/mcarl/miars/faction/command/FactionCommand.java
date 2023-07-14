package cn.mcarl.miars.faction.command;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.faction.entity.ChunkData;
import cn.mcarl.miars.faction.entity.FactionsEntity;
import cn.mcarl.miars.faction.manager.FactionsManager;
import cn.mcarl.miars.faction.mobs.CustomEnderDragon;
import cn.mcarl.miars.faction.utils.Map;
import com.boydti.fawe.bukkit.chat.FancyMessage;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FactionCommand implements TabCompleter, CommandExecutor {

    private boolean helpConsole(CommandSender sender) {
        return true;
    }

    private boolean helpPlayer(Player player) {
        return true;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player player){
            if (args.length < 1) {
                return helpPlayer(player);
            }

            switch (args[0]){
                case "addChunk" -> {
                    if (args.length==3){
                        String chunk = args[1];
                        String code = args[2];

                        if (!"wdk9oiMJkv/wd[sc'a]/q".equals(code)){return false;}
                        FactionsEntity factionsEntity = FactionsManager.getInstance().getFactionsByPlayer(player.getUniqueId());

                        ChunkData chunkData = ChunkData.StringToChunkData(chunk);

                        if (FactionsManager.getInstance().checkFactionsChunk(player,chunkData) && factionsEntity.getChunks().add(chunkData)) {
                            FactionsManager.getInstance().saveFactions(factionsEntity);
                            player.sendMessage(ColorParser.parse("&a&l占领! &7您占领了该区块("+chunkData.getX()+","+chunkData.getZ()+")，派系维护费正在增加..."));
                            player.playSound(player.getLocation(), Sound.EXPLODE,1,1);
                        }
                    }
                }
                case "map" -> {
                    for (FancyMessage fancyMessage : new Map().getMap(player, player.getLocation().getYaw())) {
                        fancyMessage.send(player);
                    }
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String alias,
            @NotNull String[] args
    ) {
        List<String> allCompletes = new ArrayList<>();
        if (sender instanceof Player) {
            allCompletes.add("map");
        }
        return allCompletes.stream()
                .filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
                .limit(10).collect(Collectors.toList());
    }
}
