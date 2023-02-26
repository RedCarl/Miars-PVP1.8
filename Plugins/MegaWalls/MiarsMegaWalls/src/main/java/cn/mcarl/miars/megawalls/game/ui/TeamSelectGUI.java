package cn.mcarl.miars.megawalls.game.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIType;
import cn.mcarl.miars.core.ui.ServerMenuGUI;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.game.manager.GamePlayerManager;
import org.bukkit.entity.Player;

public class TeamSelectGUI extends GUI {
    public TeamSelectGUI(GamePlayer gamePlayer) {
        super(GUIType.ONE_BY_NINE, "&0你的队伍");
    }

    public void load(){

    }

    public static void open(Player player) {
        player.closeInventory();

        GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(player);

        TeamSelectGUI gui = new TeamSelectGUI(gamePlayer);
        gui.openGUI(player);
    }
}
