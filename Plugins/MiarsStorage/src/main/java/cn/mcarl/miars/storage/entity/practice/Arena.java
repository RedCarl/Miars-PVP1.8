package cn.mcarl.miars.storage.entity.practice;

import cn.mcarl.miars.storage.enums.practice.FKitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.sql.Date;

@Data
@AllArgsConstructor
public class Arena implements Cloneable{

    // 编号
    private Integer id;
    // 编号
    private String world;
    // 名称
    private String name;
    // 模式
    private FKitType mode;

    // 是否可以建筑
    private Boolean build;

    // 玩家出生点位置
    private Location loc1;
    private Location loc2;

    // 中心点
    private Location center;


    public Arena(){
        this.build = false;
    }

    @Override
    public Arena clone() {
        try {
            // TODO: 复制此处的可变状态，这样此克隆就不能更改初始克隆的内部项
            return (Arena) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
