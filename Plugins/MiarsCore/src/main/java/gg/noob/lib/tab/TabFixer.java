//package gg.noob.lib.tab;
//
//import cn.mcarl.miars.core.MiarsCore;
//import com.comphenix.protocol.PacketType;
//import com.comphenix.protocol.events.PacketAdapter;
//import com.comphenix.protocol.events.PacketContainer;
//import com.comphenix.protocol.events.PacketEvent;
//import com.comphenix.protocol.wrappers.PlayerInfoData;
//import com.mojang.authlib.GameProfile;
//import gg.noob.lib.tab.client.ClientVersionUtil;
//import net.minecraft.server.v1_8_R3.*;
//import org.bukkit.Bukkit;
//import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
//import org.bukkit.entity.Player;
//
//import java.lang.reflect.Field;
//import java.util.List;
//import java.util.UUID;
//
//public class TabFixer extends PacketAdapter {
//
//    private static Field playerField;
//    private static Field namedEntitySpawnField;
//
//    public TabFixer() {
//        super(MiarsCore.getInstance(), PacketType.Play.Server.PLAYER_INFO, PacketType.Play.Server.NAMED_ENTITY_SPAWN);
//    }
//
//    @Override
//    public void onPacketSending(PacketEvent event) {
//        TabManager tabManager = MiarsCore.getInstance().getTabManager();
//        if (tabManager == null || tabManager.getTabHandler() == null || !this.shouldForbid(event.getPlayer())) {
//            return;
//        }
//
//        if (event.getPacket().getHandle() instanceof PacketPlayOutPlayerInfo) {
//            PacketPlayOutPlayerInfo packet = (PacketPlayOutPlayerInfo) event.getPacket().getHandle();
//            List<PlayerInfoData> datas = event.getPacket().getPlayerInfoDataLists().read(0);
//            for (PlayerInfoData data : datas) {
//                GameProfile packetProfile = (GameProfile) data.getProfile().getHandle();
//
//                if (Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).anyMatch(uuid -> uuid == packetProfile.getId())) {
//                    event.setCancelled(true);
//                }
//            }
//        }
//    }
//
//    private boolean shouldCancel(Player player, PacketContainer packetContainer) {
//        UUID tabPacketPlayer;
//        if (ClientVersionUtil.getProtocolVersion(player) <= 20) {
//            return true;
//        }
//        PacketPlayOutPlayerInfo playerInfoPacket=(PacketPlayOutPlayerInfo) packetContainer.getHandle();
//        EntityPlayer recipient=((CraftPlayer) player).getHandle();
//        try {
//            tabPacketPlayer=((GameProfile) playerField.get(playerInfoPacket)).getId();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        Player bukkitPlayer=Bukkit.getPlayer(tabPacketPlayer);
//        if (bukkitPlayer == null) {
//            return true;
//        }
//        EntityTrackerEntry trackerEntry=(EntityTrackerEntry) ((WorldServer) ((CraftPlayer) bukkitPlayer).getHandle().getWorld()).getTracker().trackedEntities.get(bukkitPlayer.getEntityId());
//        if (trackerEntry == null) {
//            return true;
//        }
//        return !trackerEntry.trackedPlayers.contains(recipient);
//    }
//
//    private boolean shouldForbid(Player player) {
//        TabHandler tabHandler = MiarsCore.getInstance().getTabManager().getTabHandler();
//        TabAdapter playerTab = tabHandler.getAdapter(player);
//        return playerTab != null && playerTab.initialized.contains(player);
//    }
//
//    static {
//        try {
//            playerField=PacketPlayOutPlayerInfo.PlayerInfoData.class.getDeclaredField("d");
//            playerField.setAccessible(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            namedEntitySpawnField=PacketPlayOutNamedEntitySpawn.class.getDeclaredField("b");
//            namedEntitySpawnField.setAccessible(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
