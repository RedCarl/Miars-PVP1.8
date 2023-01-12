package cn.mcarl.miars.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @Author: carl0
 * @DATE: 2023/1/5 0:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FCombatInfo {

    // 对手
    private UUID opponent;

    // 战斗结束的时间
    private Long date;

}
