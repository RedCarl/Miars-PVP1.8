package io.github.Leonardo0013YT.UltraSkyWars.objects;

import java.util.HashMap;

public class Index {

    private String type;
    private HashMap<String, Integer> index = new HashMap<>();

    public Index(String type) {
        this.type = type;
    }

}