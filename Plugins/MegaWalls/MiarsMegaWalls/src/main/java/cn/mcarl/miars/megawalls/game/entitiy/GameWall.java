package cn.mcarl.miars.megawalls.game.entitiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameWall {
    private Location minCorner;
    private Location maxCorner;
    private boolean collapse;
}
