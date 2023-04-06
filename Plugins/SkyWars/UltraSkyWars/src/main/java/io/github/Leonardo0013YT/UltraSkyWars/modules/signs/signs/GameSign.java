package io.github.Leonardo0013YT.UltraSkyWars.modules.signs.signs;

import io.github.Leonardo0013YT.UltraSkyWars.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.utils.Utils;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XBlock;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class GameSign {

    private Block retract;
    private String type;
    private Location loc;
    private GameData data;
    private boolean occupied;

    public GameSign(String type, Location loc) {
        this.type = type;
        this.loc = loc;
        this.retract = Utils.getBlockFaced(loc.getBlock());
        this.occupied = false;
    }

    public synchronized void setLines(String... line) {
        if (loc.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) loc.getBlock().getState();
            sign.setLine(0, line[0]);
            sign.setLine(1, line[1]);
            sign.setLine(2, line[2]);
            sign.setLine(3, line[3]);
            sign.update();
            sign.update(true);
        }
    }

    public void setState(String state) {
        switch (state) {
            case "WAITING":
                XBlock.setColor(retract, DyeColor.LIME);
                break;
            case "STARTING":
                XBlock.setColor(retract, DyeColor.YELLOW);
                break;
            case "PREGAME":
                XBlock.setColor(retract, DyeColor.PINK);
                break;
            case "GAME":
                XBlock.setColor(retract, DyeColor.RED);
                break;
            case "FINISH":
                XBlock.setColor(retract, DyeColor.BLUE);
                break;
            case "RESTARTING":
                XBlock.setColor(retract, DyeColor.PURPLE);
                break;
            default:
                XBlock.setColor(retract, DyeColor.WHITE);
                break;
        }
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getType() {
        return type.toLowerCase();
    }

    public GameData getData() {
        return data;
    }

    public void setData(GameData data) {
        this.data = data;
    }

}