package cn.mcarl.miars.lobby.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.publics.items.Ranks;
import cn.mcarl.miars.core.publics.items.ServerMenu;
import cn.mcarl.miars.lobby.conf.PluginConfig;
import cn.mcarl.miars.lobby.manager.ScoreBoardManager;
import cn.mcarl.miars.storage.entity.MPlayer;
import cn.mcarl.miars.storage.entity.MRank;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import cn.mcarl.miars.storage.storage.data.MRankDataStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e){
        Player player = e.getPlayer();
        Location location = player.getLocation();

        if (location.getBlockY() <= PluginConfig.LOBBY_SITE.HEIGHT.get()){
            player.teleport(PluginConfig.LOBBY_SITE.LOCATION.get());
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.teleport(PluginConfig.LOBBY_SITE.LOCATION.get());

        new ServerMenu().give(player,0);
        new Ranks().give(player,1);

        ScoreBoardManager.getInstance().joinPlayer(player);

        player.sendMessage(ColorParser.parse("&6--&e-&8--------------------------------&6-&e--"));
        player.sendMessage(ColorParser.parse("&r"));
        player.sendMessage(ColorParser.parse("&6▪ &7您好，欢迎来到 &6磐石 &f(中国) 。"));
        player.sendMessage(ColorParser.parse("&r"));
        player.sendMessage(ColorParser.parse("&6▪ &7点击物品栏名为 “&f游戏选择&7”的&f书&7查看游戏列表。"));
        player.sendMessage(ColorParser.parse("&6▪ &7您可以点击下方的链接进入官网。"));
        player.sendMessage(ColorParser.parse("&6▪ &7➥ &fhttps://panshimc.cn"));
        player.sendMessage(ColorParser.parse("&r"));
        player.sendMessage(ColorParser.parse("&6--&e-&8--------------------------------&6-&e--"));

    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();
        ScoreBoardManager.getInstance().removePlayer(player);
    }

    @EventHandler
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent e){
        MPlayer mPlayer = MPlayerDataStorage.getInstance().getMPlayer(e.getPlayer());
        MRank mRank = MRankDataStorage.getInstance().getMRank(mPlayer.getRank());
        e.setFormat(ColorParser.parse(mRank.getPrefix()+mRank.getNameColor()+"%1$s&f: %2$s"));
    }

    @EventHandler
    public void FoodLevelChangeEvent(FoodLevelChangeEvent e){
        if (e.getEntity() instanceof Player p){
            p.setFoodLevel(20);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e){
        Player player = e.getPlayer();
        ItemStack itemStack = e.getItem();
        Block block = e.getClickedBlock();
        Action action = e.getAction();

        if(e.getAction().equals(Action.PHYSICAL)) {
            if (e.getClickedBlock().getType() == Material.STONE_PLATE) {
                Vector v = player.getVelocity();
                v.setX(8);
                v.setY(2);
                player.setVelocity(v);

            }
        }

    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event) {
        // 禁止玩家丢弃物品
        event.setCancelled(true);
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent e){
        e.setCancelled(true);
    }

}
