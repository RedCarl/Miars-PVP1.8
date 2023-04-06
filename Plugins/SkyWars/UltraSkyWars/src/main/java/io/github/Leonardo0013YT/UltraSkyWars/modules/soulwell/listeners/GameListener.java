package io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.listeners;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.api.events.USWGameKillEvent;
import io.github.Leonardo0013YT.UltraSkyWars.data.SWPlayer;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.upgrades.SoulWellAngelOfDeath;
import io.github.Leonardo0013YT.UltraSkyWars.superclass.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.ThreadLocalRandom;

public class GameListener implements Listener {

    private UltraSkyWars plugin;
    private InjectionSoulWell is;

    public GameListener(UltraSkyWars plugin, InjectionSoulWell is) {
        this.plugin = plugin;
        this.is = is;
    }

    @EventHandler
    public void onGameKill(USWGameKillEvent e) {
        Player k = e.getPlayer();
        Player d = e.getDeath();
        Game game = e.getGame();
        SWPlayer sw = plugin.getDb().getSWPlayer(k);
        SoulWellAngelOfDeath sa = is.getSwm().getAngelByLevel(sw.getSoulWellHead());
        int random = ThreadLocalRandom.current().nextInt(0, 101);
        String rarity = is.getSwm().getRandomRarity();
        if (sa.getProbability() >= random) {
            if (!sw.hasHead(rarity, d.getName())) {
                game.sendGameSound(CustomSound.COLLECT_HEAD);
                game.sendGameMessage(is.getSoulwell().get("collected").replaceAll("<player>", k.getName()).replaceAll("<death>", d.getName()).replaceAll("<rarity>", is.getSoulwell().get("raritys." + rarity)));
                sw.addHead(rarity, d.getName(), System.currentTimeMillis());
            }
        }
    }

}