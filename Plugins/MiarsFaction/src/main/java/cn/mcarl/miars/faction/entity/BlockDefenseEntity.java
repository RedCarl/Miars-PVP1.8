package cn.mcarl.miars.faction.entity;

import lombok.*;

/**
 * @Author: carl0
 * @DATE: 2022/7/14 17:54
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlockDefenseEntity {
    long lastAttack;
    double healthy;
}
