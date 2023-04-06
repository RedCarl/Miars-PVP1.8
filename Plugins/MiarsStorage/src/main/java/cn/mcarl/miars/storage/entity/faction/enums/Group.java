package cn.mcarl.miars.storage.entity.faction.enums;

/**
 * @Author: carl0
 * @DATE: 2022/6/24 15:32
 */
public enum Group {
    OWNER("领导者","&c"),
    ADMIN("管理","&c"),
    KNIGHT("领导者","&c"),
    GUARD("领导者","&c"),
    SWORDSMAN("领导者","&c"),
    PROTECTOR("领导者","&c"),
    DEFAULT("领导者","&c"),
    ;

    final String name;
    final String color;

    Group(String name, String color) {
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
