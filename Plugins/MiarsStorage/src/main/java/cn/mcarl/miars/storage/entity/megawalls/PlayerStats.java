package cn.mcarl.miars.storage.entity.megawalls;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerStats {

    private int id;
    private String uuid;
    private int kills;
    private int finalKills;
    private int wins;
    private int games;
    private int coins;
    private int mythicDust;
    private String classes;

}
