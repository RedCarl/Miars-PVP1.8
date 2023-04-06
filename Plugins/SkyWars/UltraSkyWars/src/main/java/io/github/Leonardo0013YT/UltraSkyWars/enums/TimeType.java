package io.github.Leonardo0013YT.UltraSkyWars.enums;

public enum TimeType {

    DAWN(0),
    DAY(500),
    AFTERNOON(8000),
    NIGHT(18000);

    private int time;

    TimeType(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}