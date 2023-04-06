package cn.mcarl.miars.faction.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FPlayerEntity {
    private UUID uuid;
    private int tagId;
    private long playTime;
    private int power;
    private int powerMax;

    private Location fieldLocation;
    private List<BedHomeEntity> homes;
}
