package cn.mcarl.miars.faction.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.PatternType;

import java.util.List;

/**
 * @Author: carl0
 * @DATE: 2022/7/14 21:50
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BannerEntity {
    private DyeColor color;
    private List<Pattern> patterns;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Pattern {
        private DyeColor dyeColor;
        private PatternType patternType;
    }
}
