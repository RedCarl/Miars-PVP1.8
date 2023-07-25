package cn.mcarl.miars.core.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.conf.PluginConfig;
import cn.mcarl.miars.core.impl.lunarclient.LunarClientAPI;
import cn.mcarl.miars.core.impl.lunarclient.enums.Mods;
import cn.mcarl.miars.core.impl.lunarclient.event.LCPlayerRegisterEvent;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.client.LCPacketModSettings;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.client.obj.ModSettings;
import cn.mcarl.miars.core.impl.lunarclient.serverrule.LunarClientAPIServerRule;
import cn.mcarl.miars.core.utils.LunarClientAPIUtils;
import cn.mcarl.miars.core.utils.MiarsUtils;
import cn.mcarl.miars.storage.storage.data.MPlayerDataStorage;
import gg.kazer.event.SendTitleEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: carl0
 * @DATE: 2022/11/30 20:48
 */
public class PlayerListener implements Listener {

    // Lunar Client start
    private static final LCPacketModSettings LC_PACKET_MOD_SETTINGS;

    static {
        ModSettings modSettings = new ModSettings();
        // Go through all the items in the list, and disable each mod.
//        for (Mods mod : List.of(Mods.HYPIXEL_MOD)) {
//            modSettings.addModSetting(mod.getModId(), new ModSettings.ModSetting(false, new HashMap<>()));
//        }
        LC_PACKET_MOD_SETTINGS = new LCPacketModSettings(modSettings);
    }
    // Lunar Client end

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // 初始化玩家数据
        MPlayerDataStorage.getInstance().checkMPlayer(player);

        player.setGameMode(GameMode.SURVIVAL);

        MiarsCore.getInstance().getTabHeaderAndFooter().show(player);

        // 禁止玩家进入消息
        e.setJoinMessage(null);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e){
        Player player = e.getPlayer();

        // DataCache
        MPlayerDataStorage.getInstance().clearUserCacheData(player);

        // Tab
        MiarsCore.getInstance().getTabHeaderAndFooter().hide(player);

        // NameTag
        MiarsCore.getNametagAPI().clearNametag(player);

        // 禁止玩家进入消息
        e.setQuitMessage(null);
    }

    @EventHandler
    public void PlayerGameModeChangeEvent(PlayerGameModeChangeEvent e){
        Player player = e.getPlayer();
        player.sendMessage(ColorParser.parse("&7You changed the mode to &b"+e.getNewGameMode().name()+" &7 by &b"+e.getPlayer().getName()+" &7("+(player.isOnline()?"&aOnline":"&cOffline")+"&7)"));
    }

    // Lunar Client Start
    @EventHandler
    public void onLCRegister(LCPlayerRegisterEvent e) {
        LunarClientAPI.getInstance().sendPacket(e.getPlayer(), LC_PACKET_MOD_SETTINGS);
        LunarClientAPIServerRule.sendServerRule(e.getPlayer());
    }

    @EventHandler
    public void SendTitleEvent(SendTitleEvent e){
        e.setCancelled(true);

        if (e.getTitle()!=null){
            LunarClientAPIUtils.sendTitle(e.getPlayer(),e.getTitle());
        }
        if (e.getSubtitle()!=null){
            LunarClientAPIUtils.sendSubTitle(e.getPlayer(),e.getSubtitle());
        }

    }
    // Lunar Client end
}
