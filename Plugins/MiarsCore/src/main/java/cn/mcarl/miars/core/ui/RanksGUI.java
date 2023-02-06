package cn.mcarl.miars.core.ui;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.utils.ItemBuilder;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

import java.util.HashMap;
import java.util.Map;

public class RanksGUI extends GUI {
    final Player player;

    public RanksGUI(Player player) {
        super(GUIType.SIX_BY_NINE, "&0头衔管理");
        this.player = player;
        load();
    }

    Map<String, MRank> mRankMap = new HashMap<>();
    MPlayer mPlayer = new MPlayer();

    public void load(){
        mRankMap = MRankDataStorage.getInstance().getMRankList();
        mPlayer = MPlayerDataStorage.getInstance().getMPlayer(player);

        int i=0;
        for (String s:mRankMap.keySet()){
            if (mPlayer.getRanks().contains(s)){

                if (mPlayer.getRank().equals(s)){
                    setSelectRankItem(mRankMap.get(s));
                }else {
                    setRankItem(mRankMap.get(s));
                }

            }else {
                setUnlockRankItem(mRankMap.get(s));
            }
            i++;
        }

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
                clicker.sendMessage(ColorParser.parse("&7您的头衔已经更换为 "+mRank.getPrefix()+" 请注意查看。"));

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


    public static void open(Player player) {
        player.closeInventory();
        RanksGUI gui = new RanksGUI(player);
        gui.openGUI(player);
    }

}
