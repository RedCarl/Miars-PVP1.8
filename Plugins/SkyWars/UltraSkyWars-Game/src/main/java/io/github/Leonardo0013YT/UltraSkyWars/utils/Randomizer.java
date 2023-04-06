package io.github.Leonardo0013YT.UltraSkyWars.utils;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {

    private Integer[][] randomizers;

    public Randomizer() {
        randomizers = new Integer[30][27];
        for (int i = 0; i < randomizers.length; i++) {
            HashSet<Integer> selected = new HashSet<>();
            while (selected.size() < randomizers[i].length) {
                int slot = ThreadLocalRandom.current().nextInt(0, 27);
                selected.add(slot);
            }
            randomizers[i] = selected.toArray(new Integer[0]);
        }
    }

    public Integer[] getRandomizer() {
        return randomizers[ThreadLocalRandom.current().nextInt(0, 30)];
    }

}