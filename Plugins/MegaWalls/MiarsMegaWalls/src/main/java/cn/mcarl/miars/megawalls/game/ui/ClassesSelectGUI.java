package cn.mcarl.miars.megawalls.game.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.ClassesInfo;
import cn.mcarl.miars.megawalls.classes.ClassesManager;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.manager.GamePlayerManager;
import cn.mcarl.miars.megawalls.stats.ClassesStats;
import cn.mcarl.miars.megawalls.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class ClassesSelectGUI extends GUI {
    public ClassesSelectGUI(GamePlayer gamePlayer) {
        super(GUIType.SIX_BY_NINE, "&0职业选择器");

        load(gamePlayer);
    }

    public void load(GamePlayer gamePlayer){
        int i = 3;
        for (Classes classes:ClassesManager.getStaterClasses()) {
            setItem(i,setClassesItem(classes,gamePlayer));
            i++;
        }

        setItem(49,setCancelItem(gamePlayer));
        setItem(50,setRandomClassesItem(gamePlayer));
    }

    public GUIItem setClassesItem(Classes classes,GamePlayer gamePlayer){
        ClassesStats stats = gamePlayer.getPlayerStats().getClassesStats(classes);
        if (gamePlayer.getPlayerStats().getClassesStats(classes)!=null){
            List<String> lore = new ArrayList<>();

            lore.add("&8"+classes.getClassesType().getName()+"职业");
            lore.add("&7职业定位:");
            for (ClassesInfo.Orientation o:classes.getOrientations()) {
                lore.add("&r  "+o.getText()+"&7("+o.getInfo()+"&7)");
            }
            lore.add("&7驾驭难度:");
            lore.add("&r  "+classes.getDifficulty().getText());
            lore.add("&r");
            lore.add("&7技能("+classes.getMainSkill().getName()+"):");
            lore.addAll(classes.getMainSkill().getInfo(stats.getSkillLevel()));
            lore.add("&r");
            lore.add("&7升级进度:");
            lore.add("&r  "+stats.upgradePercent());
            lore.add("&7末影箱(行):");
            lore.add("&r  &a"+stats.getEnderChest());
            if (gamePlayer.getPlayerStats().getClasses().equals(classes.getName())){
                lore.add("&r");
                lore.add("&a您正在使用这个职业...");
            }else {
                lore.add("&r");
                lore.add("&e点击切换到这个职业");
            }
            return new GUIItem(
                    new ItemBuilder(classes.getIconType())
                            .setName(classes.getNameColor()+classes.getDisplayName())
                            .setLore(lore)
                            .addEnchant(Enchantment.LUCK,1,gamePlayer.getPlayerStats().getClasses().equals(classes.getName()))
                            .addFlag(ItemFlag.HIDE_ENCHANTS)
                            .toItemStack()
            ){
                @Override
                public void onClick(Player clicker, ClickType type) {
                    if (gamePlayer.getPlayerStats().getClasses().equals(classes.getName())){
                        clicker.closeInventory();
                    }else {
                        gamePlayer.getPlayerStats().setClasses(classes.getName());
                        Bukkit.getScheduler().runTaskLaterAsynchronously(MiarsMegaWalls.getInstance(), () -> {
                            PlayerUtils.skinChange(clicker, classes.getDefaultSkin().getValue(), classes.getDefaultSkin().getSignature());
                        }, 1L);
                    }
                }
            };
        }else {
            return new GUIItem(
                    new ItemBuilder(Material.BARRIER)
                            .setName("&c您还未解锁该职业")
                            .toItemStack()
            );
        }
    }

    public GUIItem setCancelItem(GamePlayer gamePlayer){
        return new GUIItem(
                new ItemBuilder(Material.BARRIER)
                        .setName("&c关闭")
                        .toItemStack()
        ){
            @Override
            public void onClick(Player clicker, ClickType type) {
                gamePlayer.getPlayer().closeInventory();
            }
        };
    }
    public GUIItem setRandomClassesItem(GamePlayer gamePlayer){
        return new GUIItem(
                new ItemBuilder(Material.NETHER_STAR)
                        .setName("&a随机职业")
                        .setLore(
                                "&7从您拥有的职业中随机选择一个。",
                                "&7选择此选项时，您将获得 &6+30%硬币 &7！",
                                "&r",
                                "&e点击选择"
                        )
                        .toItemStack()
        ){
            @Override
            public void onClick(Player clicker, ClickType type) {
                gamePlayer.getPlayer().closeInventory();
            }
        };
    }

    public static void open(Player player) {
        player.closeInventory();

        GamePlayer gamePlayer = GamePlayerManager.getInstance().getGamePlayer(player);

        ClassesSelectGUI gui = new ClassesSelectGUI(gamePlayer);
        gui.openGUI(player);
    }
}
