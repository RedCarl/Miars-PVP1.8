package cn.mcarl.miars.storage.entity.practice;

import cn.mcarl.miars.storage.entity.ffa.FInventoryByte;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.enums.QueueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArenaState {

    // 房间ID
    private Integer id;

    // 房间ID
    private Integer arenaId;

    // 0 空闲 | 1 正在分配 | 2 正在战斗 | 3 战斗结束 | 4 房间故障
    private Integer state;

    // 玩家
    private String playerA;
    // 玩家的库存
    private FInventoryByte aFInventory;
    // 玩家
    private String playerB;
    // 玩家的库存
    private FInventoryByte bFInventory;

    // 对局开始时间
    private Long startTime;

    // 对局结束时间
    private Long endTime;

    // 获胜方
    private String win;

    // 游戏类型
    private FKitType fKitType;

    // 匹配类型
    private QueueType queueType;

}
