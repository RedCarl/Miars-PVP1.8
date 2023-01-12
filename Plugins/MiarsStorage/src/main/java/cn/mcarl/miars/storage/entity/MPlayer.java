package cn.mcarl.miars.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

/**
 * 重写玩家实体,减轻体积
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MPlayer {

    /**
     * 玩家UUID
     */
    private String uuid;

    /**
     * 当前佩戴头衔
     */
    private String rank;

    /**
     * 拥有的头衔
     */
    private List<String> ranks;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;
}
