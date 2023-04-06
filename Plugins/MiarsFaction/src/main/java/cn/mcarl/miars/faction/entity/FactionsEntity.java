package cn.mcarl.miars.faction.entity;

import cn.mcarl.miars.storage.entity.faction.enums.Group;
import lombok.*;
import org.bukkit.Location;

import java.util.*;

/**
 * @Author: carl0
 * @DATE: 2022/6/24 14:42
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FactionsEntity {
    private UUID uuid;
    private String name;
    private BannerEntity banner;

    //信息
    private String present;
    private Map<UUID, Group> playerGroups = new HashMap<>();
    private Set<ChunkData> chunks = new HashSet<>();


    //储存
    private long moneyBank;
    private long tntBank;


    //领地柜
    private Location territoryCabinet;
}
