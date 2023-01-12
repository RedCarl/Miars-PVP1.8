package cn.mcarl.miars.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 服务器实体,包含一个服务器的基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MServerInfo {

    /**
     * 服务器名称
     */
    private String name;

    /**
     * 服务器描述
     */
    private String motd;

    /**
     * 服务器玩家列表
     */
    private List<MPlayer> players;
}
