package io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.animations;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.enums.CustomSound;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.InjectionSoulWell;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.interfaces.SoulWellAnimation;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.SoulWellPath;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.SoulWellRow;
import io.github.Leonardo0013YT.UltraSkyWars.modules.soulwell.soulwell.SoulWellSession;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class SoulWellNormalAnimation implements SoulWellAnimation {

    private Inventory inv;
    private ItemStack black;
    private InjectionSoulWell is;
    private SoulWellSession sws;
    private UltraSkyWars plugin;
    private SoulWellRow row;
    private Player p;
    private ItemStack orange;
    private int executes;
    private Location loc;
    private ArrayList<BukkitTask> tasks = new ArrayList<>();

    public SoulWellNormalAnimation(UltraSkyWars plugin, InjectionSoulWell is, SoulWellSession sws, Player p, SoulWellRow row, Location loc) {
        this.plugin = plugin;
        this.inv = Bukkit.createInventory(null, 45, plugin.getLang().get(null, "menus.soulwellmenu.title"));
        this.black = ItemBuilder.item(XMaterial.BLACK_STAINED_GLASS, 1, plugin.getLang().get(null, "soulwell.rolling"), "§7");
        this.is = is;
        this.sws = sws;
        this.row = row;
        this.p = p;
        this.loc = loc;
    }

    @Override
    public void execute() {
        for (int i : row.getGlass()) {
            inv.setItem(i, black);
        }
        p.openInventory(inv);
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (executes == 21) {
                    executes = 0;
                    executePhase2();
                    cancel();
                    return;
                }
                for (SoulWellPath path : row.getPaths()) {
                    ItemStack fr = inv.getItem(path.getStart());
                    ItemStack sc = inv.getItem(path.getSecond());
                    ItemStack tr = inv.getItem(path.getThree());
                    ItemStack fo = inv.getItem(path.getFour());
                    inv.setItem(path.getStart(), plugin.getLvl().getRandomReward().getIcon());
                    inv.setItem(path.getSecond(), fr);
                    inv.setItem(path.getThree(), sc);
                    inv.setItem(path.getFour(), tr);
                    inv.setItem(path.getFive(), fo);
                }
                executes++;
                CustomSound.SOULWELL.reproduce(p);
                for (int i = 0; i < inv.getSize(); i++) {
                    if (inv.getItem(i) == null || inv.getItem(i).getType().name().endsWith("STAINED_GLASS_PANE")) {
                        inv.setItem(i, is.getSwm().getRandomGlass());
                    }
                }
            }
        }.runTaskTimer(plugin, 3, 3);
        tasks.add(task);
    }

    private void executePhase2() {
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (executes == 7) {
                    executes = 0;
                    executePhase3();
                    cancel();
                    return;
                }
                for (SoulWellPath path : row.getPaths()) {
                    ItemStack fr = inv.getItem(path.getStart());
                    ItemStack sc = inv.getItem(path.getSecond());
                    ItemStack tr = inv.getItem(path.getThree());
                    ItemStack fo = inv.getItem(path.getFour());
                    inv.setItem(path.getStart(), plugin.getLvl().getRandomReward().getIcon());
                    inv.setItem(path.getSecond(), fr);
                    inv.setItem(path.getThree(), sc);
                    inv.setItem(path.getFour(), tr);
                    inv.setItem(path.getFive(), fo);
                }
                executes++;
                CustomSound.SOULWELL.reproduce(p);
                for (int i = 0; i < inv.getSize(); i++) {
                    if (inv.getItem(i) == null || inv.getItem(i).getType().name().endsWith("STAINED_GLASS_PANE")) {
                        inv.setItem(i, is.getSwm().getRandomGlass());
                    }
                }
            }
        }.runTaskTimer(plugin, 6, 6);
        tasks.add(task);
    }

    private void executePhase3() {
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (executes == 4) {
                    executes = 0;
                    for (SoulWellPath path : row.getPaths()) {
                        sws.executeReward(p, inv.getItem(path.getThree()));
                    }
                    sws.firework(loc.clone().add(0.5, -1, 0.5));
                    sws.setRolling(false);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            is.getSwm().removeSession(p);
                            p.closeInventory();
                        }
                    }.runTaskLater(plugin, 20);
                    cancel();
                    return;
                }
                for (SoulWellPath path : row.getPaths()) {
                    ItemStack fr = inv.getItem(path.getStart());
                    ItemStack sc = inv.getItem(path.getSecond());
                    ItemStack tr = inv.getItem(path.getThree());
                    ItemStack fo = inv.getItem(path.getFour());
                    inv.setItem(path.getStart(), plugin.getLvl().getRandomReward().getIcon());
                    inv.setItem(path.getSecond(), fr);
                    inv.setItem(path.getThree(), sc);
                    inv.setItem(path.getFour(), tr);
                    inv.setItem(path.getFive(), fo);
                }
                executes++;
                CustomSound.SOULWELL.reproduce(p);
                for (int i = 0; i < inv.getSize(); i++) {
                    if (inv.getItem(i) == null || inv.getItem(i).getType().name().endsWith("STAINED_GLASS_PANE")) {
                        inv.setItem(i, is.getSwm().getRandomGlass());
                    }
                }
            }
        }.runTaskTimer(plugin, 10, 10);
        tasks.add(task);
    }

    @Override
    public void cancel(Player p) {
        for (BukkitTask bt : tasks) {
            if (bt == null) continue;
            bt.cancel();
        }
    }

    @Override
    public Inventory getInv() {
        return inv;
    }
}