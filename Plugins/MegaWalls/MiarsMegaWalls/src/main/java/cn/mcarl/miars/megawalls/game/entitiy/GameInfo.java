package cn.mcarl.miars.megawalls.game.entitiy;

import cn.mcarl.miars.megawalls.game.entitiy.enums.GameState;
import cn.mcarl.miars.megawalls.game.entitiy.enums.TeamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class GameInfo {
    private GameState gameState;
    private GameWall gameWall;
    private Map<TeamType,GameTeam> gameTeams = new HashMap<>();
    private Location lobbySpawn;
    private boolean isWitherFury;
    private GameTeam winTeam;
    private GameRegion centerArea;

    public GameInfo(){}

//    public boolean isUnbreakable(Location location) {
//        if (this.isProtected(location)) {
//            return true;
//        } else if (!this.getCenterArea().isInRegion(location) && !this.isTeamRegion(location)) {
//            return true;
//        } else {
//            return this.isWall(location) && !this.isWitherFury();
//        }
//    }

//    public boolean isWall(Location location) {
//
//        for (GameWall gameWall : this.walls) {
//            if (gameWall.isInWall(location)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public boolean isTeamRegion(Location location) {
//
//        for (GameTeam gameTeam : this.gameTeams.values()) {
//            if (gameTeam.getRegion().isInRegion(location)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public boolean isProtected(Location location) {
//        Iterator<GameTeam> var2 = this.gameTeams.values().iterator();
//
//        GameTeam gameTeam;
//        do {
//            if (!var2.hasNext()) {
//                return false;
//            }
//
//            gameTeam = var2.next();
//        } while(!gameTeam.getCastleRegion().isInRegion(location) || gameTeam.isWitherDead());
//
//        return true;
//    }

}
