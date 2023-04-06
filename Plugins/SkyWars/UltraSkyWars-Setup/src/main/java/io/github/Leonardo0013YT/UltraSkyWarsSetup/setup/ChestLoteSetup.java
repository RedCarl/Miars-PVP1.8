package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup;

import java.util.ArrayList;

public class ChestLoteSetup {

    private int chance;
    private boolean center, refill;
    private ArrayList<String> modes;

    public ChestLoteSetup(int chance, boolean center, boolean refill, ArrayList<String> modes) {
        this.chance = chance;
        this.center = center;
        this.refill = refill;
        this.modes = modes;
    }

    public boolean isRefill() {
        return refill;
    }

    public void setRefill(boolean refill) {
        this.refill = refill;
    }

    public ArrayList<String> getModes() {
        return modes;
    }

    public void setModes(ArrayList<String> modes) {
        this.modes = modes;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }
}