package cn.mcarl.miars.megawalls.game.entitiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameParty {

    private GamePlayer owner;
    private List<GamePlayer> gamePlayers = new ArrayList<>();

}
