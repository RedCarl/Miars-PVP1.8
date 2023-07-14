package cn.mcarl.miars.faction.utils;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.faction.entity.ChunkData;
import cn.mcarl.miars.faction.entity.FactionsEntity;
import cn.mcarl.miars.faction.manager.FactionsManager;
import com.boydti.fawe.bukkit.chat.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * @Author: carl0
 * @DATE: 2022/6/24 23:17
 */
public class Map {
    /**
     * The map is relative to a coord and a faction north is in the direction of decreasing x east is in the direction
     * of decreasing z
     */
    public ArrayList<FancyMessage> getMap(Player player, double inDegrees) {
        ArrayList<FancyMessage> ret = new ArrayList<>();

        // Get the compass
        ArrayList<String> asciiCompass = AsciiCompass.getAsciiCompass(inDegrees, ChatColor.DARK_GREEN, "§8");

        int halfWidth = 49 / 2;
        // Use player's value for height
        int halfHeight = 17 / 2;

        Location topLeft = getRelative(player,-halfWidth, -halfHeight);

        int width = halfWidth * 2 + 1;
        int height = halfHeight * 2 + 1;


        ret.add(new FancyMessage("§r"));
        ret.add(new FancyMessage("§r"));
        ret.add(new FancyMessage(ColorParser.parse("&7您可以在这里看到周围的区块，并查看它们的状态。")));

        // For each row
        for (int dz = 0; dz < height; dz++) {
            // Draw and add that row
            FancyMessage row = new FancyMessage("");

            if (dz < 3) {
                row.then(asciiCompass.get(dz));
            }
            for (int dx = (dz < 3 ? 6 : 3); dx < width; dx++) {
                if (dx == halfWidth && dz == halfHeight) {
                    row.then("+").color(ChatColor.AQUA).tooltip(ColorParser.parse("&7("+(dx + topLeft.getX())+","+(dz + topLeft.getZ())+")"));
                } else {
                    FactionsEntity factions = FactionsManager.getInstance().getFactionsChunk(new ChunkData((int) (dx + topLeft.getX()), (int) (dz + topLeft.getZ()),0));
                    FactionsEntity meFactions = FactionsManager.getInstance().getFactionsByPlayer(player.getUniqueId());
                    if (factions==null){
                        row.then("-").color(ChatColor.GRAY).tooltip(ColorParser.parse("&7("+(dx + topLeft.getX())+","+(dz + topLeft.getZ())+")"));
                    }else if (meFactions!=null && factions.getUuid()==meFactions.getUuid()){
                        row.then("-").color(ChatColor.GREEN).tooltip(ColorParser.parse("&7("+(dx + topLeft.getX())+","+(dz + topLeft.getZ())+")"));
                    }else {
                        row.then("-").color(ChatColor.RED).tooltip(ColorParser.parse("&7("+(dx + topLeft.getX())+","+(dz + topLeft.getZ())+")"));
                    }

                }
            }
            ret.add(row);
        }
        ret.add(new FancyMessage("§r"));
        return ret;
    }

    public Location getRelative(Player player, int dx, int dz) {
        return new Location(player.getWorld(), player.getLocation().getChunk().getX()+dx,0, player.getLocation().getChunk().getZ()+dz);
    }
}
