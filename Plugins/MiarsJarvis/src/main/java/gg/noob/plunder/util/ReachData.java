// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import gg.noob.plunder.data.PlayerData;
import org.bukkit.Location;
import java.util.List;

public class ReachData
{
    private List<DistanceData> distanceList;
    private Location location;
    private Location lastLocation;
    private List<Pair<Long, Location>> possibleMoves;
    private DistanceData distanceData;
    private double movement;
    private int previousSwingTicks;
    private double certainty;
    private PlayerData playerData;
    private boolean previousLook;
    
    public ReachData(final PlayerData playerData, final Location location, final Location lastLocation, final DistanceData distanceData, final List<Pair<Long, Location>> possibleMoves, final List<DistanceData> distanceList, final boolean previousLook, final int previousSwingTicks, final double movement, final double certainty) {
        this.playerData = playerData;
        this.location = location;
        this.lastLocation = lastLocation;
        this.distanceData = distanceData;
        this.possibleMoves = possibleMoves;
        this.distanceList = distanceList;
        this.previousLook = previousLook;
        this.previousSwingTicks = previousSwingTicks;
        this.movement = movement;
        this.certainty = certainty;
    }
    
    public Location getLastLocation() {
        return this.lastLocation;
    }
    
    public double getCertainty() {
        return this.certainty;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public int getPreviousSwingTicks() {
        return this.previousSwingTicks;
    }
    
    public DistanceData getDistanceData() {
        return this.distanceData;
    }
    
    @Deprecated
    public double getReach() {
        return this.distanceData.getReach();
    }
    
    public List<DistanceData> getDistanceList() {
        return this.distanceList;
    }
    
    public List<Pair<Long, Location>> getPossibleMoves() {
        return this.possibleMoves;
    }
    
    public PlayerData getPlayerData() {
        return this.playerData;
    }
    
    @Deprecated
    public double getExtra() {
        return this.distanceData.getExtra();
    }
    
    public boolean isPreviousLook() {
        return this.previousLook;
    }
    
    public double getMovement() {
        return this.movement;
    }
    
    public double getUncertaintyReachValue() {
        final double reach = this.distanceData.getReach();
        return (reach > 3.0) ? (3.0 + (reach - 3.0) * this.certainty) : reach;
    }
    
    @Deprecated
    public double getVertical() {
        return this.distanceData.getVertical();
    }
    
    @Deprecated
    public double getHorizontal() {
        return this.distanceData.getHorizontal();
    }
}
