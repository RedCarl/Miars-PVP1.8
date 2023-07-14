package cn.mcarl.miars.storage.entity.practice.enums.practice;

public enum FKitType {
    NO_DEBUFF("NodeBuff"),
    BUILD_UHC("BuildUHC"),
    BOXING("Boxing"),
    SUMO("Sumo"),
    BOW("Bow"),
    COMBO("Combo")
    ;

    final String name;
    FKitType(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
