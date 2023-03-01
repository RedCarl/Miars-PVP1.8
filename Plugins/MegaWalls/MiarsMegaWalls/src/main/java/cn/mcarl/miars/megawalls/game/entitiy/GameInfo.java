package cn.mcarl.miars.megawalls.game.entitiy;

import cn.mcarl.miars.megawalls.game.entitiy.enums.GameState;
import cn.mcarl.miars.megawalls.game.entitiy.enums.TeamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameInfo {
    private GameState gameState;
    private GameWall gameWall;
    private Map<TeamType,GameTeam> gameTeams = new HashMap<>();
    private Location lobbySpawn;
    private boolean isWitherFury;
    private GameTeam winTeam;
}
