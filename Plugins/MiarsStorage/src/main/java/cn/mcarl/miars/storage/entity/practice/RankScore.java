package cn.mcarl.miars.storage.entity.practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankScore {
    private Integer id;
    private UUID uuid;
    private Integer season;
    private Long score;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private UUID uuid;
        private Integer season;
    }
}

