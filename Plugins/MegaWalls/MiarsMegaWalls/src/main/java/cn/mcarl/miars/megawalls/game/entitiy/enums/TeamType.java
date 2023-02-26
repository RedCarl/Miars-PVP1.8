package cn.mcarl.miars.megawalls.game.entitiy.enums;

import cc.carm.lib.easyplugin.utils.ColorParser;

public enum TeamType {
    RED("&c","红"),
    YELLOW("&e", "黄"),
    BLUE("&b", "蓝"),
    GREEN("&a", "绿"),
    ;

    private final String color;
    private final String type;

    TeamType(String color, String type) {
        this.type = type;
        this.color = color;
    }

    public String getColor(){
        return ColorParser.parse(this.color);
    }
    public String getType(){
        return ColorParser.parse(this.type);
    }

    public String getTeamName(){
        return getColor()+getType();
    }
}
