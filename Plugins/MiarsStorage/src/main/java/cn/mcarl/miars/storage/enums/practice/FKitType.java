package cn.mcarl.miars.storage.enums.practice;

public enum FKitType {
    FFAGAME("FFA"),
    NO_DEBUFF("NodeBuff"),
    BUILD_UHC("BuildUHC"),
    PRACTICE("Practice"),
    ;// 大厅

    final String name;
    FKitType(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
