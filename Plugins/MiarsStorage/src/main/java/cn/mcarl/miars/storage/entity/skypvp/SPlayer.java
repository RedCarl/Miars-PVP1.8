package cn.mcarl.miars.storage.entity.skypvp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SPlayer {


    // 玩家UUID
    private UUID uuid;

    // 玩家名称
    private String name;

    // 击杀人数
    private Long killsCount;

    // 死亡次数
    private Long deathCount;

    // 经验
    private Long exp;

    // 开启幸运方块
    private Long lucky;
}
