package io.github.Leonardo0013YT.UltraSkyWars.tops;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopPlayer {

    private final String type;
    private final String uuid;
    private final String name;
    private final int position;
    private final int amount;

    @Override
    public String toString() {
        return type + ":" + uuid + ":" + amount;
    }

}