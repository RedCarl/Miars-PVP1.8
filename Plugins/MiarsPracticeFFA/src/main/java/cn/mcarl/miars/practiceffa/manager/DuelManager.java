package cn.mcarl.miars.practiceffa.manager;

import lombok.Data;

public class DuelManager {

    private static final DuelManager instance = new DuelManager();
    public static DuelManager getInstance() {
        return instance;
    }

    @Data
    static
    class Duel{
        public Duel(String a, String b, Long time) {
            this.a = a;
            this.b = b;
            this.time = time;
        }

        private String a;
        private String b;
        private Long time;
    }
}
