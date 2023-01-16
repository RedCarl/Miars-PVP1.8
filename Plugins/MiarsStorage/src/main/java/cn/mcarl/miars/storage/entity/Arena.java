package cn.mcarl.miars.storage.entity;

import cn.mcarl.miars.storage.enums.FKitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Arena {

    // 编号
    Integer id;
    // 模式
    FKitType mode;
    // 竞技场名称
    String name;
    // 竞技场显示名称
    String displayName;
    // 是否可以建筑
    Boolean build;

    // 玩家出生点位置
    Location loc1;
    Location loc2;

    // 竞技场范围
    Location corner1;
    Location corner2;

    // 中心点
    Location center;

    // 图标
    ItemStack icon;

    // 更新时间
    private Date updateTime;

    // 创建时间
    private Date createTime;




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
