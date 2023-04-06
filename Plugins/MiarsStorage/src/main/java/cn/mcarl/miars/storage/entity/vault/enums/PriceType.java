package cn.mcarl.miars.storage.entity.vault.enums;

public enum PriceType {
    VAULT("硬币","&e"),
    POINTS("金子","&6");

    final String name;
    final String color;

    PriceType(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }
}
