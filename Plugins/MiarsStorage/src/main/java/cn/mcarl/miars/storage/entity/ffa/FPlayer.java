package cn.mcarl.miars.storage.entity.ffa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FPlayer {

    // 玩家UUID
    private UUID uuid;

    // 玩家名称
    private String name;

    // 击杀人数
    private Long killsCount;

    // 死亡数量
    private Long deathCount;

    // 排位分
    private Long rankScore;

    // 更新时间
    private Date updateTime;

    // 创建时间
    private Date createTime;



}
