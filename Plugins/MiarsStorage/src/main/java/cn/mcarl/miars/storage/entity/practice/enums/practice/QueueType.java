package cn.mcarl.miars.storage.entity.practice.enums.practice;

public enum QueueType {
    UNRANKED("Unranked","&a"),
    RANKED("Ranked","&b");


    String name;
    String color;
    QueueType(String name,String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
