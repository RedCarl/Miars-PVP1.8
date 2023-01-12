package cn.mcarl.bungee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 重写玩家实体,减轻体积
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MPlayer {

    /**
     * 玩家修饰名称
     */
    private String displayName;

    /**
     * 玩家名称
     */
    private String name;

    /**
     * 玩家UUID
     */
    private String uuid;

    /**
     * 玩家延迟
     */
    private int ping;
}
