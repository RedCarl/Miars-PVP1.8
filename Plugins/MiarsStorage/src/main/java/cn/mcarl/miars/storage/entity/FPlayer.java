package cn.mcarl.miars.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FPlayer {

    // 玩家UUID
    private String uuid;

    // 击杀人数
    private Long killsCount;

    // 死亡数
    private Long deathCount;

    // 排位分
    private Long rankScore;

    // 更新时间
    private Date updateTime;

    // 创建时间
    private Date createTime;

}
