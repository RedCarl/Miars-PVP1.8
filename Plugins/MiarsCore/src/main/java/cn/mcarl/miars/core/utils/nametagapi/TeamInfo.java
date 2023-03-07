package cn.mcarl.miars.core.utils.nametagapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamInfo {

    private String name;
    private String prefix;
    private String suffix;

    TeamInfo(String name) {
        this.name = name;
    }

}