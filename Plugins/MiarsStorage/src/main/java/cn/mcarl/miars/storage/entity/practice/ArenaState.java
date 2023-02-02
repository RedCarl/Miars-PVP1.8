package cn.mcarl.miars.storage.entity.practice;

import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.ffa.FInventoryByte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArenaState {

    // 房间ID
    private Integer id;

    // 0 空闲 | 1 正在使用 | 2 故障
    private Integer state;

    // 玩家
    private String a;
    // 玩家的库存
    private FInventoryByte aFInventory;
    // 玩家
    private String b;
    // 玩家的库存
    private FInventoryByte bFInventory;

    // 对局开始时间
    private Long startTime;

    // 对局结束时间
    private Long endTime;

    // 获胜方
    private String win;

}
