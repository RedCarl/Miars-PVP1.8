package io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell;

public class SoulWellPath {

    private int[] path;

    public SoulWellPath(int[] path) {
        this.path = path;
    }

    public int getStart() {
        return path[0];
    }

    public int getSecond() {
        return path[1];
    }

    public int getThree() {
        return path[2];
    }

    public int getFour() {
        return path[3];
    }

    public int getFive() {
        return path[4];
    }

}