package cn.mcarl.miars.faction.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.stream.Location;

/**
 * @Author: carl0
 * @DATE: 2022/7/17 19:48
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BedHomeEntity {
    Location headLocation; //头坐标
    Location tailLocation; //尾坐标
    long lastTime; //最后一次使用的时间
}
