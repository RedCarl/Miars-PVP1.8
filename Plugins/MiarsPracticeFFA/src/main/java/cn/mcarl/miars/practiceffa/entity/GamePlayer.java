package cn.mcarl.miars.practiceffa.entity;

import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.publics.items.BackBed;
import cn.mcarl.miars.core.publics.items.Ranks;
import cn.mcarl.miars.core.utils.MiarsUtils;
import cn.mcarl.miars.practiceffa.conf.PluginConfig;
import cn.mcarl.miars.practiceffa.items.*;
import cn.mcarl.miars.practiceffa.kits.FFAGame;
import cn.mcarl.miars.practiceffa.kits.NoDeBuff;
import cn.mcarl.miars.practiceffa.manager.CombatManager;
import cn.mcarl.miars.practiceffa.manager.GamePlayerManager;
import cn.mcarl.miars.practiceffa.utils.FFAUtil;
import cn.mcarl.miars.storage.entity.ffa.FCombatInfo;
import cn.mcarl.miars.storage.entity.ffa.FInventory;
import cn.mcarl.miars.storage.entity.ffa.FPlayer;
import cn.mcarl.miars.storage.entity.practice.QueueInfo;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import cn.mcarl.miars.storage.storage.data.practice.FPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayer {

    private FPlayer fPlayer;
    private FCombatInfo fCombatInfo;
    private FInventory fInventory;
    private QueueType queueType;
    private FKitType queueModel;
    private boolean queue = true;
    private PracticeBackpack practiceBackpack;
    enum PracticeBackpack{
        PRACTICE,
        CANCEL_QUEUE,
        FFA
    }


    public GamePlayer(Player player){

        this.fPlayer = FPlayerDataStorage.getInstance().getFPlayer(player);

        initData();
    }

    public void initData(){
        Player player = getPlayer();

        this.fCombatInfo = CombatManager.getInstance().getCombatInfo(player);

        QueueInfo queueInfo = PracticeQueueDataStorage.getInstance().getQueue(this.fPlayer);
        this.queueType = queueInfo==null ? null : queueInfo.getQueueType();
        this.queueModel = queueInfo==null ? null : queueInfo.getFKitType();

        if (FFAUtil.isRange(
                player,
                PluginConfig.FFA_SITE.LOCATION.get(),
                PluginConfig.FFA_SITE.RADIUS.get())
        ){
            if (queueInfo==null){
                this.practiceBackpack = PracticeBackpack.PRACTICE;
                if (this.queue || this.fInventory!=null){
                    this.fInventory = null;
                    setPractice();
                    FFAUtil.initializePlayer(player);
                }
            }else {
                this.practiceBackpack = PracticeBackpack.CANCEL_QUEUE;
                if (!this.queue || this.fInventory!=null){
                    this.fInventory = null;
                    setQueue();
                    FFAUtil.initializePlayer(player);
                }
            }
        }else {
            this.practiceBackpack = PracticeBackpack.FFA;
           if (this.fInventory==null){
               this.fInventory = NoDeBuff.get();

               FFAUtil.autoEquip(player,this.fInventory);
               FFAUtil.initializePlayer(player);
           }
        }
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(this.fPlayer.getUuid());
    }


    private void setPractice(){
        Player p = getPlayer();
        FFAUtil.clearPlayerInv(p);
        this.queue = false;
        MiarsUtils.initPlayerNametag(p,false);

        new PracticeSelect().give(p,0);
        new ShopMenu().give(p,1);
        new Team().give(p,2);
        new Spectate().give(p,4);
        new Event().give(p,6);
        new Settings().give(p,7);
        new EditKit().give(p,8);
    }

    private void setQueue(){
        Player p = getPlayer();
        FFAUtil.clearPlayerInv(p);
        this.queue = true;
        new QueueCancel().give(p,4);

//        MiarsUtils.initPlayerNametag(p,queueType.getColor()+" ["+queueModel.getName()+"]",false);
    }

    public static GamePlayer get(Player player){
        return GamePlayerManager.getInstance().get(player);
    }

    public static void remove(Player player){
        GamePlayerManager.getInstance().remove(player);
//        FFAUtil.initializePlayer(player);
    }

    public static void init(Player player){
        GamePlayerManager.getInstance().init(player);
        GamePlayer.get(player).initData();
    }
}
