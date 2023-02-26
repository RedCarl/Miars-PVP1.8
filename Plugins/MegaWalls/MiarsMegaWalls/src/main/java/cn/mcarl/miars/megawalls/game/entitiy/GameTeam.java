package cn.mcarl.miars.megawalls.game.entitiy;

import cn.mcarl.miars.megawalls.game.entitiy.enums.TeamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameTeam {

    private TeamType teamType;
    private List<GamePlayer> gamePlayers = new ArrayList<>();
    private Location respawn;
}
