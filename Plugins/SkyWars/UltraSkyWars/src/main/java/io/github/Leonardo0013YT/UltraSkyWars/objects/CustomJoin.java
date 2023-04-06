package io.github.Leonardo0013YT.UltraSkyWars.objects;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.State;
import io.github.Leonardo0013YT.UltraSkyWars.game.GameData;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;

import java.util.List;

public class CustomJoin {

    private String name;
    private List<String> maps;
    private UltraSkyWars plugin;

    public CustomJoin(UltraSkyWars plugin, String path) {
        this.plugin = plugin;
        this.name = plugin.getJoin().get(null, path + ".name");
        this.maps = plugin.getJoin().getList(path + ".maps");
    }

    public String getName() {
        return name;
    }

    public GameData getRandomGame() {
        int alto = 0;
        GameData g = null;
        for (String map : maps) {
            GameData game = plugin.getGm().getGameData().get(map);
            if (game == null || game.isState(State.GAME) || game.isState(State.RESTARTING) || game.isState(State.FINISH) || game.isState(State.PREGAME)) {
                continue;
            }
            if (g == null || (alto < game.getPlayers())) {
                g = game;
                alto = game.getPlayers();
            }
        }
        return g;
    }

    public int getGameSize() {
        int count = 0;
        for (String map : maps) {
            Game game = plugin.getGm().getGameByName(map);
            if (game == null) {
                continue;
            }
            count += game.getPlayers().size();
        }
        return count;
    }

}