package io.github.Leonardo0013YT.UltraSkyWars.superclass;

public abstract class GameEvent {

    public String type, name, sound, title, subtitle;
    public int reset, time;

    public abstract void start(Game game);

    public abstract void stop(Game game);

    public abstract void reset();

    public abstract GameEvent clone();

    public void update() {
        time--;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
        this.reset = time;
    }

    public int getReset() {
        return reset;
    }

    public String getName() {
        return name;
    }

    public String getSound() {
        return sound;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subtitle;
    }

    public String getType() {
        return type;
    }

}