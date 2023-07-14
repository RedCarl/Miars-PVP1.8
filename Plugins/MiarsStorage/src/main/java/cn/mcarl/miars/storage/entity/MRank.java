package cn.mcarl.miars.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @Author: carl0
 * @DATE: 2022/11/30 12:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MRank {

    /**
     * 编号
     */
    private Integer id;

    /**
     * 头衔名称
     */
    private String name;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 名字颜色
     */
    private String nameColor;

    /**
     * 权限
     */
    private String permissions;

    /**
     * 组
     */
    private String group;

    /**
     * 描述
     */
    private String describe;

    /**
     * 权重
     */
    private Integer power;

}
