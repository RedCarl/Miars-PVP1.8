package io.github.Leonardo0013YT.UltraSkyWars.enums;

public enum HealthType {

    HEALTH5(10, true),
    HEALTH10(20, true),
    HEALTH20(40, true),
    UHC(20, false);

    private int amount;
    private boolean regen;

    HealthType(int amount, boolean regen) {
        this.amount = amount;
        this.regen = regen;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isRegen() {
        return regen;
    }
}