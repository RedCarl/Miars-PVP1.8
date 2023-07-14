////
//// Decompiled by Procyon v0.5.36
////
//
//package gg.noob.plunder.logs.menu;
//
//import org.bukkit.Sound;
//import java.util.Iterator;
//import org.bukkit.DyeColor;
//import com.google.inject.internal.util.ImmutableList;
//import gg.noob.plunder.util.TimeUtils;
//import java.util.Date;
//import org.bukkit.ChatColor;
//import gg.noob.lib.util.ItemBuilder;
//import org.bukkit.Material;
//import java.util.Comparator;
//import me.blade.gg.core.util.MathUtils;
//import org.bukkit.entity.Player;
//import gg.noob.plunder.checks.Check;
//import gg.noob.plunder.logs.Log;
//import java.util.List;
//import gg.noob.lib.gui.GUIBase;
//
//public class LogsCheckMenu extends GUIBase
//{
//    private String targetName;
//    private List<Log> logs;
//    private static int pageMax;
//    private int checksSize;
//    private Check check;
//    private int lastPage;
//    private List<Log> allLogs;
//
//    public LogsCheckMenu(final Player player, final String targetName, final int page, final List<Log> logs, final int checksSize, final Check check, final int lastPage, final List<Log> allLogs) {
//        super(player, targetName + "'s " + check.getDisplayName() + " logs (" + page + "/" + MathUtils.toPage(checksSize, LogsCheckMenu.pageMax) + ")", 36);
//        this.targetName = targetName;
//        this.logs = logs;
//        this.checksSize = checksSize;
//        this.check = check;
//        this.lastPage = lastPage;
//        this.allLogs = allLogs;
//        this.setPage(MathUtils.toPage(checksSize, LogsCheckMenu.pageMax));
//        this.setCurrentPage(page);
//        this.setAsync(true);
//    }
//
//    public void onUpdate() {
//        this.logs.sort(Comparator.comparingLong(o -> o.getTime()).reversed());
//        int count = 0;
//        int i = 0;
//        for (final Log log : this.logs) {
//            if (!log.getCheck().equalsIgnoreCase(this.check.getName())) {
//                continue;
//            }
//            if (++count < LogsCheckMenu.pageMax * (this.getCurrentPage() - 1)) {
//                continue;
//            }
//            if (i >= LogsCheckMenu.pageMax) {
//                break;
//            }
//            this.setItem(i, new ItemBuilder(Material.PAPER).displayName(ChatColor.RED + this.check.getDisplayName()).lore((List)ImmutableList.of((Object)"", (Object)(ChatColor.AQUA + "Ping: " + ChatColor.WHITE + log.getPing()), (Object)(ChatColor.AQUA + "Violations: " + ChatColor.WHITE + log.getViolations()), (Object)(ChatColor.AQUA + "More Message: " + ChatColor.WHITE + ((log.getMoreMessage() == null) ? "NONE" : log.getMoreMessage())), (Object)(ChatColor.AQUA + "Time: " + ChatColor.WHITE + TimeUtils.formatIntoCalendarString(new Date(log.getTime()))))).getItemStack());
//            ++i;
//        }
//        for (int j = 18; j < 27; ++j) {
//            this.setItem(j, new ItemBuilder(Material.STAINED_GLASS_PANE).displayName("§7").data((short)DyeColor.GRAY.getWoolData()).getItemStack());
//        }
//        if (this.getCurrentPage() > 1) {
//            this.setItem(27, new ItemBuilder(Material.ARROW).displayName(ChatColor.YELLOW + "<< Previous Page").getItemStack());
//        }
//        this.setItem(31, new ItemBuilder(Material.ARROW).displayName(ChatColor.YELLOW + "Back").getItemStack());
//        if (this.getCurrentPage() < this.getPage()) {
//            this.setItem(35, new ItemBuilder(Material.ARROW).displayName(ChatColor.YELLOW + "Next Page >>").getItemStack());
//        }
//    }
//
//    public void onClick(final int slot) {
//        if (slot == 27) {
//            if (this.getCurrentPage() > 1) {
//                this.getOwner().playSound(this.getOwner().getLocation(), Sound.CLICK, 10.0f, 1.0f);
//                new LogsCheckMenu(this.getOwner(), this.targetName, this.getCurrentPage() - 1, this.logs, this.checksSize, this.check, this.lastPage, this.allLogs).open();
//            }
//        }
//        else if (slot == 31) {
//            this.getOwner().playSound(this.getOwner().getLocation(), Sound.CLICK, 10.0f, 1.0f);
//            new ChecksLogMenu(this.getOwner(), this.targetName, this.lastPage, this.allLogs, this.checksSize).open();
//        }
//        else if (slot == 35 && this.getCurrentPage() < this.getPage()) {
//            this.getOwner().playSound(this.getOwner().getLocation(), Sound.CLICK, 10.0f, 1.0f);
//            new LogsCheckMenu(this.getOwner(), this.targetName, this.getCurrentPage() + 1, this.logs, this.checksSize, this.check, this.lastPage, this.allLogs).open();
//        }
//    }
//
//    static {
//        LogsCheckMenu.pageMax = 18;
//    }
//}
