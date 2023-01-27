package cn.mcarl.miars.storage.entity.ffa;

import cn.mcarl.miars.storage.enums.FKitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FKit {

    // 所属玩家
    private String uuid;

    // 类型
    private FKitType type;

    // 库存名称
    private String name;

    // 库存内容
    private FInventory inventory;

    // 权重
    private Integer power;

    // 更新时间
    private Date updateTime;

    // 创建时间
    private Date createTime;

}
