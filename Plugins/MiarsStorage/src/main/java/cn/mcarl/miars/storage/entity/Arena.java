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
    private Integer id;
    // 模式
    private FKitType mode;
    // 竞技场名称
    private String name;
    // 竞技场显示名称
    private String displayName;
    // 是否可以建筑
    private Boolean build;

    // 玩家出生点位置
    private Location loc1;
    private Location loc2;

    // 竞技场范围
    private Location corner1;
    private Location corner2;

    // 中心点
    private Location center;

    // 图标
    private ItemStack icon;

    // 更新时间
    private Date updateTime;

    // 创建时间
    private Date createTime;
}
