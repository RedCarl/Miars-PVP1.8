package cn.mcarl.miars.core.tab;

import cn.mcarl.miars.core.MiarsCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TabTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player:Bukkit.getOnlinePlayers()) {
            MiarsCore.getInstance().getTabHeaderAndFooter().show(player);
        }
    }
}
