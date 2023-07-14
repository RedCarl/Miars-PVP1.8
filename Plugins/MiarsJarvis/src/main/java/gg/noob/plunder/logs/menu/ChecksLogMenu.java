////
//// Decompiled by Procyon v0.5.36
////
//
//package gg.noob.plunder.logs.menu;
//
//import java.util.Objects;
//import org.bukkit.Sound;
//import java.util.Iterator;
//import com.google.inject.internal.util.ImmutableList;
//import gg.noob.plunder.checks.Check;
//import gg.noob.plunder.Plunder;
//import java.util.Comparator;
//import org.bukkit.DyeColor;
//import gg.noob.lib.util.ItemBuilder;
//import org.bukkit.Material;
//import org.bukkit.ChatColor;
//import java.util.ArrayList;
//import java.util.HashMap;
//import me.blade.gg.core.util.MathUtils;
//import org.bukkit.entity.Player;
//import java.util.Map;
//import gg.noob.plunder.logs.Log;
//import java.util.List;
//import gg.noob.lib.gui.GUIBase;
//
//public class ChecksLogMenu extends GUIBase
//{
//    private String targetName;
//    private List<Log> logs;
//    private static int pageMax;
//    private int checksSize;
//    private Map<Integer, List<Log>> slotMap;
//
//    public ChecksLogMenu(final Player player, final String targetName, final int page, final List<Log> logs, final int checksSize) {
//        super(player, targetName + "'s checks log (" + page + "/" + MathUtils.toPage(checksSize, ChecksLogMenu.pageMax) + ")", 54);
//        this.slotMap = new HashMap<Integer, List<Log>>();
//        this.targetName = targetName;
//        this.logs = logs;
//        this.checksSize = checksSize;
//        this.setPage(MathUtils.toPage(checksSize, ChecksLogMenu.pageMax));
//        this.setCurrentPage(page);
//        this.setAsync(true);
//    }
//
//    public void onUpdate() {
//        final List<String> lore = new ArrayList<String>();
//        lore.add("");
//        lore.add(ChatColor.WHITE + "Logs: " + ChatColor.AQUA + this.logs.size());
//        lore.add(ChatColor.WHITE + "Banned logs: ");
//        lore.add("");
//        for (final Log log : this.logs) {
//            if (!log.isBanned()) {
//                continue;
//            }
//            lore.add(ChatColor.GRAY + "- " + ChatColor.WHITE + log.getCheck());
//        }
//        this.setItem(4, new ItemBuilder(Material.SKULL_ITEM).data((short)3).displayName(ChatColor.AQUA + ChatColor.BOLD.toString() + this.targetName).lore((List)lore).getItemStack());
//        for (int i = 9; i < 18; ++i) {
//            this.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).displayName("§7").data((short)DyeColor.GRAY.getWoolData()).getItemStack());
//        }
//        this.logs.sort(Comparator.comparingLong(o -> o.getTime()).reversed());
//        int count = 0;
//        int j = 18;
//        for (final Check check : Plunder.getInstance().getChecks()) {
//            final List<Log> checkLogs = new ArrayList<Log>();
//            for (final Log log2 : this.logs) {
//                if (!log2.getCheck().equalsIgnoreCase(check.getName())) {
//                    continue;
//                }
//                checkLogs.add(log2);
//            }
//            if (checkLogs.isEmpty()) {
//                continue;
//            }
//            if (++count < ChecksLogMenu.pageMax * (this.getCurrentPage() - 1)) {
//                continue;
//            }
//            if (j - 18 >= ChecksLogMenu.pageMax) {
//                break;
//            }
//            this.setItem(j, new ItemBuilder(Material.PAPER).displayName(ChatColor.RED + check.getDisplayName()).lore((List)ImmutableList.of((Object)"", (Object)(ChatColor.AQUA + "Count: " + ChatColor.WHITE + checkLogs.size()), (Object)"", (Object)(ChatColor.YELLOW + "Click to view!"))).getItemStack());
//            this.slotMap.put(j, checkLogs);
//            ++j;
//        }
//        for (int k = 36; k < 45; ++k) {
//            this.setItem(k, new ItemBuilder(Material.STAINED_GLASS_PANE).displayName("§7").data((short)DyeColor.GRAY.getWoolData()).getItemStack());
//        }
//        if (this.getCurrentPage() > 1) {
//            this.setItem(45, new ItemBuilder(Material.ARROW).displayName(ChatColor.YELLOW + "<< Previous Page").getItemStack());
//        }
//        if (this.getCurrentPage() < this.getPage()) {
//            this.setItem(53, new ItemBuilder(Material.ARROW).displayName(ChatColor.YELLOW + "Next Page >>").getItemStack());
//        }
//    }
//
//    public void onClick(final int slot) {
//        if (this.slotMap.containsKey(slot)) {
//            final List<Log> logs = this.slotMap.get(slot);
//            this.getOwner().playSound(this.getOwner().getLocation(), Sound.CLICK, 10.0f, 1.0f);
//            new LogsCheckMenu(this.getOwner(), this.targetName, 1, logs, this.checksSize, Objects.requireNonNull(Plunder.getInstance().getChecks().stream().filter(check -> check.getName().equalsIgnoreCase(logs.get(0).getCheck())).findFirst().orElse(null)), this.getCurrentPage(), this.logs).open();
//        }
//        else if (slot == 45) {
//            if (this.getCurrentPage() > 1) {
//                this.getOwner().playSound(this.getOwner().getLocation(), Sound.CLICK, 10.0f, 1.0f);
//                new ChecksLogMenu(this.getOwner(), this.targetName, this.getCurrentPage() - 1, this.logs, this.checksSize).open();
//            }
//        }
//        else if (slot == 53 && this.getCurrentPage() < this.getPage()) {
//            this.getOwner().playSound(this.getOwner().getLocation(), Sound.CLICK, 10.0f, 1.0f);
//            new ChecksLogMenu(this.getOwner(), this.targetName, this.getCurrentPage() + 1, this.logs, this.checksSize).open();
//        }
//    }
//
//    static {
//        ChecksLogMenu.pageMax = 18;
//    }
//}
