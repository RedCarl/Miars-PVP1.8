package cn.mcarl.miars.skypvp.enums;

public enum LuckBlockType {
    NORMAL("&7普通","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTc5ZmFkNDIwZDJjOTcyMDUxYzljMDRkNGYyNmE4ZDBkMGE5YTNiZWMyOGQ4MGIxZjY4YmQ1ZGQ4Y2ZhZTBjZiJ9fX0="),
    RARE("&c稀有","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzE1NDRiZDcyNjA1ODI3YjlhODgwMjNlNzgzNjhiNDFlOGY4M2FjNWM5ZTQ3YzgyZmIxOTZmYzY3MmIyMmE3NiJ9fX0="),
    EPIC("&d史诗","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTA2ZWExMDRjYjliZTcwM2NjZWQxYjFmNTY1Mjg2NzUyZTI3MTc1MmM1YWM4NWU4MTEzYjNlMmRjNDM1MmMyMCJ9fX0="),
    LEGENDARY("&e传奇","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDNkMDRkYmE1MWY4OTI0OTU4MzRmZjcxYTQyOWE4YTkxMDE1YTVhNzg2Yjg1NmZmZTljMDI0Y2RiNTJmYmM4ZiJ9fX0=");


    private final String name;
    private final String value;
    LuckBlockType(String name,String value) {
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return this.name;
    }

    public String getValue(){
        return this.value;
    }


}
