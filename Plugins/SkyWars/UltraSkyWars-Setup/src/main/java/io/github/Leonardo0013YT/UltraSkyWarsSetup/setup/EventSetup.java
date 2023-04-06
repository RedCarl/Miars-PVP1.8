package io.github.Leonardo0013YT.UltraSkyWarsSetup.setup;

public class EventSetup {

    private String type;
    private int seconds;

    public EventSetup(String type) {
        this.type = type;
        this.seconds = 60;
    }

    public String getType() {
        return type;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void addSeconds(int seconds) {
        this.seconds += seconds;
    }

    public void removeSeconds(int seconds) {
        this.seconds -= seconds;
    }

}