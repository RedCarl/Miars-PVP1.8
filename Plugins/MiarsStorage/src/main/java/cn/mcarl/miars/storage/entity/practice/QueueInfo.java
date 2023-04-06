package cn.mcarl.miars.storage.entity.practice;

import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
