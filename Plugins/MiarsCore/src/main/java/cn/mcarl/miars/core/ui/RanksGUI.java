package cn.mcarl.miars.core.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.utils.GUIUtils;
import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.MiarsUtil;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import cn.mcarl.miars.storage.utils.CustomSort;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RanksGUI extends GUI {
    final Player player;

    public RanksGUI(Player player) {
        super(GUIType.SIX_BY_NINE, "&0头衔管理");
        this.player = player;
        load();
    }

    List<MRank> mRanks = new ArrayList<>();
    MPlayer mPlayer = new MPlayer();

    public void load(){
        mRanks = new ArrayList<>(MRankDataStorage.getInstance().getMRankList().values().stream().toList());
        mPlayer = MPlayerDataStorage.getInstance().getMPlayer(player);

        CustomSort.sort(mRanks,"power",true);

        int i=0;
        for (MRank m:mRanks){
            if (!m.getName().equals("default")){
                if (mPlayer.getRanks().contains(m.getName())){

                    if (mPlayer.getRank().equals(m.getName())){
                        setItem(i, setSelectRankItem(m));
                    }else {
                        setItem(i, setRankItem(m));
                    }

                }else {
                    setItem(i, setUnlockRankItem(m));
                }
                i++;
            }
        }

        setItem(new GUIItem(GUIUtils.getLineItem()),36,37,38,39,40,41,42,43,44);

        setItem(53,setDefaultRankItem());
    }

    public GUIItem setSelectRankItem(MRank mRank){
        return new GUIItem(new ItemBuilder(Material.NAME_TAG)
                .setName(mRank.getPrefix()+"&a[已佩戴]")
                .setLore(mRank.getDescribe().split("/n"))
                .addEnchant(Enchantment.KNOCKBACK,1,true)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .toItemStack());
    }

    public GUIItem setRankItem(MRank mRank){
        return new GUIItem(new ItemBuilder(Material.NAME_TAG)
                .setName(mRank.getPrefix()+"&7[未佩戴]")
                .setLore(mRank.getDescribe().split("/n"))
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .toItemStack()){
            @Override
            public void onClick(Player clicker, ClickType type) {
                mPlayer.setRank(mRank.getName());
                MPlayerDataStorage.getInstance().putMPlayer(mPlayer);
                clicker.sendMessage(ColorParser.parse("&7您的头衔已经更换为 "+mRank.getPrefix()+" &7请注意查看。"));

                MiarsUtil.initPlayerNametag(player,true);

                load();
                updateView();
            }
        };
    }

    public GUIItem setUnlockRankItem(MRank mRank){
        return new GUIItem(new ItemBuilder(Material.BARRIER)
                .setName(mRank.getPrefix()+"&c[未拥有]")
                .setLore(mRank.getDescribe().split("/n"))
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .toItemStack());
    }

    public GUIItem setDefaultRankItem(){
        return new GUIItem(new ItemBuilder(Material.MILK_BUCKET)
                .setName("&c恢复默认头衔")
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .toItemStack()){
            @Override
            public void onClick(Player clicker, ClickType type) {
                mPlayer.setRank("default");
                MPlayerDataStorage.getInstance().putMPlayer(mPlayer);
                clicker.sendMessage(ColorParser.parse("&7您的头衔已经更换为默认头衔请注意查看。"));

                load();
                updateView();
            }
        };
    }


    public static void open(Player player) {
        player.closeInventory();
        RanksGUI gui = new RanksGUI(player);
        gui.openGUI(player);
    }

}
