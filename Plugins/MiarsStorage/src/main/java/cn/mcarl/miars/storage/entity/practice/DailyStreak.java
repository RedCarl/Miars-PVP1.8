package cn.mcarl.miars.storage.entity.practice;

import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.enums.QueueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyStreak {

    // 玩家
    UUID uuid;

    // 连胜
    Integer streak;

    // 匹配类型
    QueueType queueType;

    // 模式类型
    FKitType fKitType;
}
