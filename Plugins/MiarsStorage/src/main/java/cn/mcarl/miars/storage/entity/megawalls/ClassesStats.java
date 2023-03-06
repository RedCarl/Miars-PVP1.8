package cn.mcarl.miars.storage.entity.megawalls;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassesStats {
    private int id;
    private String uuid;
    private int level;
    private int equipLevel;
    private int skillLevel;
    private int skill2Level;
    private int skill3Level;
    private int skill4Level;
    private int wins;
    private int games;
    private int finalKills;
    private int finalAssists;
    private int enderChest;
    private int masterPoints;
    private boolean enableGoldTag;
    private long playTime;
    private JsonObject inventory;
}
