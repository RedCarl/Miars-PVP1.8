package cn.mcarl.miars.faction.utils;

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


        // ret.add(new FancyMessage(new TranslateMessage("factions-hell.map.header.message").getMessage(player)));

        // For each row
        for (int dz = 0; dz < height; dz++) {
            // Draw and add that row
            FancyMessage row = new FancyMessage("");

            if (dz < 3) {
                row.then(asciiCompass.get(dz));
            }
            for (int dx = (dz < 3 ? 6 : 3); dx < width; dx++) {
                if (dx == halfWidth && dz == halfHeight) {
                    // row.then("+").color(ChatColor.AQUA).tooltip(new TranslateMessage("factions-hell.map.me.tooltip",dx + topLeft.getX(), dz + topLeft.getZ()).getMessage(player));
                } else {
                    FactionsEntity factions = FactionsManager.getInstance().getFactionsChunk(new ChunkData((int) (dx + topLeft.getX()), (int) (dz + topLeft.getZ()),0));
                    FactionsEntity meFactions = FactionsManager.getInstance().getFactionsByPlayer(player.getUniqueId());
                    if (factions==null){
                        // row.then("-").color(ChatColor.GRAY).tooltip(new TranslateMessage("factions-hell.map.field.tooltip",dx + topLeft.getX(), dz + topLeft.getZ()).getMessage(player));
                    }else if (meFactions!=null && factions.getUuid()==meFactions.getUuid()){
                        // row.then("-").color(ChatColor.GREEN).tooltip(new TranslateMessage("factions-hell.map.me-factions.tooltip",dx + topLeft.getX(), dz + topLeft.getZ()).getMessage(player));
                    }else {
                        // row.then("-").color(ChatColor.RED).tooltip(new TranslateMessage("factions-hell.map.factions.tooltip",dx + topLeft.getX(), dz + topLeft.getZ(),factions.getName()).getMessage(player));
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
