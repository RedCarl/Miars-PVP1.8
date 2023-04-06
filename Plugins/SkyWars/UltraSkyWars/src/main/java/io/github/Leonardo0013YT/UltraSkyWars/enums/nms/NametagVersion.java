package io.github.Leonardo0013YT.UltraSkyWars.enums.nms;

import lombok.Getter;

@Getter
public enum NametagVersion {

    v1_8_R3("a", "b", "c", "e", "g", "h", "i"),
    v1_9_R2("a", "b", "c", "e", "h", "i", "j"),
    v1_10_R1("a", "b", "c", "e", "h", "i", "j"),
    v1_11_R1("a", "b", "c", "e", "h", "i", "j"),
    v1_12_R1("a", "b", "c", "e", "h", "i", "j"),
    v1_13_R2("a", "b", "c", "e", "h", "i", "j"),
    v1_14_R1("a", "b", "c", "e", "h", "i", "j"),
    v1_15_R1("a", "b", "c", "e", "h", "i", "j"),
    v1_16_R1("a", "b", "c", "e", "h", "i", "j"),
    v1_16_R2("a", "b", "c", "e", "h", "i", "j"),
    v1_16_R3("a", "b", "c", "e", "h", "i", "j");

    private final String a, b, c, e, g, h, i;

    NametagVersion(String a, String b, String c, String e, String g, String h, String i) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.e = e;
        this.g = g;
        this.h = h;
        this.i = i;
    }

}
