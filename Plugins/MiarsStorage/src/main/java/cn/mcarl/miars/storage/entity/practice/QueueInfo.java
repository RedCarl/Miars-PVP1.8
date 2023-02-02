package cn.mcarl.miars.storage.entity.practice;

import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import cn.mcarl.miars.storage.enums.FKitType;
import cn.mcarl.miars.storage.enums.QueueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueInfo {

    // 队列模式
    FKitType fKitType;

    // 队列类型
    QueueType queueType;

    // 队列人员
    List<String> players;
}
