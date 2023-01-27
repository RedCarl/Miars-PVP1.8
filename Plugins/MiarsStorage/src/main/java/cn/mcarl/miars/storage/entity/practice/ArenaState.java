package cn.mcarl.miars.storage.entity.practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArenaState {


    // 编号
    private Integer id;

    // 0 空闲 | 1 正在使用 | 2 故障
    private Integer state;

    // 玩家
    private Player a;
    private Player b;

    // 对局开始时间
    private Long startTime;

    // 对局结束时间
    private Long endTime;
}
