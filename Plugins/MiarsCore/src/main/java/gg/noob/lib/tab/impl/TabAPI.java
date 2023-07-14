package gg.noob.lib.tab.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import gg.noob.lib.tab.TabAdapter;
import gg.noob.lib.tab.client.ClientVersionUtil;
import gg.noob.lib.tab.reflect.Reflection;
import gg.noob.lib.tab.skin.SkinType;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class TabAPI extends TabAdapter {

    private final Map<Integer, GameProfile> profiles = new HashMap<Integer, GameProfile>();

    @Override
    public TabAPI sendEntryData(Player player, int column, int row, String text, int ping) {
        if (column > 4) {
            throw new RuntimeException("Column is above 4 " + column);
        }

        if (row > 19) {
            throw new RuntimeException("Row is above 19 " + row);
        }

        final String[] split = splitText(ChatColor.translateAlternateColorCodes('&', text));

        final int index = getIndexOf(column, row, 20);
        final String name = getEntry(index);
        final String teamName = "$" + name;

        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

        Reflection.getField(packet.getClass(), "a", String.class).set(packet, teamName);
        Reflection.getField(packet.getClass(), "b", String.class).set(packet, teamName);

        Reflection.getField(packet.getClass(), "c", String.class).set(packet, split[0]);
        Reflection.getField(packet.getClass(), "d", String.class).set(packet, split[1]);

        Reflection.getField(packet.getClass(), "h", int.class).set(packet, 2);
        Reflection.getField(packet.getClass(), "f", int.class).set(packet, -1);

        this.sendPacket(player, packet);

        GameProfile profile = this.profiles.get(index);
        EntityPlayer entityPlayer = this.getEntityPlayer(profile);

        entityPlayer.ping = ping;

        this.sendInfoPacket(player, PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY, entityPlayer);
        return this;
    }

    @Override
    public void updateSkin(String[] skinData, int index, Player player) {
        GameProfile profile = this.profiles.get(index);
        Property property = profile.getProperties().get("textures").iterator().next();
        EntityPlayer entityPlayer = this.getEntityPlayer(profile);

        skinData = skinData != null && skinData.length >= 1 && !skinData[0].isEmpty() && !skinData[1].isEmpty() ? skinData : SkinType.DARK_GRAY.getSkinData();

        if (!property.getSignature().equals(skinData[1]) || !property.getValue().equals(skinData[0])) {
            profile.getProperties().remove("textures", property);
            profile.getProperties().put("textures", new Property("textures", skinData[0], skinData[1]));

            this.sendInfoPacket(player, PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
            this.sendInfoPacket(player, PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
        }
    }

    @Override
    public TabAPI addFakePlayers(Player player) {
        if (!initialized.contains(player)) {
            for (int row = 0; row < 20; row++) { //slot 0-59 for 1.7 players
                for(int column = 0; column < 3; column++) {
                    final int index = getIndexOf(column, row, 20);
                    final String name = getEntry(index);

                    final GameProfile profile = new GameProfile(UUID.randomUUID(), name);
                    final String[] skinData = SkinType.DARK_GRAY.getSkinData();

                    profile.getProperties().put("textures", new Property("textures", skinData[0], skinData[1]));

                    final EntityPlayer entityPlayer = this.getEntityPlayer(profile);
                    entityPlayer.ping = -1;

                    this.sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));

                    this.profiles.put(index, profile);
                }
            }

            for (int index = 60; index < 80; index++) {  //slot 60-79 for 1.8 players
                final String name = getEntry(index);

                final GameProfile profile = new GameProfile(UUID.randomUUID(), name);
                final String[] skinData = SkinType.DARK_GRAY.getSkinData();

                profile.getProperties().put("textures", new Property("textures", skinData[0], skinData[1]));

                final EntityPlayer entityPlayer = this.getEntityPlayer(profile);
                entityPlayer.ping = -1;

                this.sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));

                this.profiles.put(index, profile);
            }

            this.setupScoreboard(player, true);

            this.initialized.add(player);
        }

        return this;
    }

    @Override
    public TabAPI removeFakePlayers(Player player) {
        if (initialized.contains(player)) {
            for (int row = 0; row < 20; row++) { //slot 0-59 for 1.7 players
                for(int column = 0; column < 3; column++) {
                    int index = getIndexOf(column, row, 20);
                    GameProfile gameProfile = profiles.get(index);
                    EntityPlayer entityPlayer = getEntityPlayer(gameProfile);

                    this.sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));

                    this.profiles.remove(index);
                }
            }

            for (int index = 60; index < 80; index++) {  //slot 60-79 for 1.8 players
                GameProfile gameProfile = profiles.get(index);
                EntityPlayer entityPlayer = getEntityPlayer(gameProfile);;

                this.sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));

                this.profiles.remove(index);
            }

            setupScoreboard(player, false);

            this.initialized.remove(player);
        }

        return this;
    }

    @Override
    public TabAPI sendHeaderFooter(Player player, List<String> header, List<String> footer) {
        if (ClientVersionUtil.getProtocolVersion(player) >= 47 && (header != null || footer != null)) {
            final Packet<?> packet = new PacketPlayOutPlayerListHeaderFooter(
                    IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Joiner.on('\n').join(header) + "\"}")
            );

            try {
                final Field footerField = packet.getClass().getDeclaredField("b");

                footerField.setAccessible(true);
                footerField.set(packet, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Joiner.on('\n').join(footer) + "\"}"));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

            this.sendPacket(player, packet);
        }

        return this;
    }

    @Override
    public TabAPI setupScoreboard(Player player, boolean toggle) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

        Reflection.getField(packet.getClass(), "a", String.class).set(packet, "tabHandler");
        Reflection.getField(packet.getClass(), "b", String.class).set(packet, "tabHandler");

        Reflection.getField(packet.getClass(), "h", int.class).set(packet, 0);
        Reflection.getField(packet.getClass(), "f", int.class).set(packet, -1);

        Reflection.getField(packet.getClass(), "g", Object.class).set(packet, Lists
                .newArrayList(Bukkit.getOnlinePlayers()).stream().map(Player::getName).collect(Collectors.toList()));


        // this.sendPacket(player, packet);

        for(int row = 0; row < 20; row++) {
            for(int column = 0; column < 4; column++) {
                final int index = getIndexOf(column, row, 20);
                final String name = getEntry(index);
                final String teamName = "$" + name;

                packet = new PacketPlayOutScoreboardTeam();

                Reflection.getField(packet.getClass(), "a", String.class).set(packet, teamName);
                Reflection.getField(packet.getClass(), "b", String.class).set(packet, teamName);

                Reflection.getField(packet.getClass(), "h", int.class).set(packet, toggle ? 0 : 1);
                Reflection.getField(packet.getClass(), "f", int.class).set(packet, -1);

                Reflection.getField(packet.getClass(), "g", Object.class).set(packet, Collections.singleton(name));

                this.sendPacket(player, packet);

            }
        }

        packet = new PacketPlayOutScoreboardTeam();

        Reflection.getField(packet.getClass(), "a", String.class).set(packet, "tabHandler");
        Reflection.getField(packet.getClass(), "b", String.class).set(packet, "tabHandler");

        Reflection.getField(packet.getClass(), "h", int.class).set(packet, 3);
        Reflection.getField(packet.getClass(), "f", int.class).set(packet, -1);

        Reflection.getField(packet.getClass(), "g", Object.class).set(packet, Collections.singleton(player.getName()));

        for(Player other : Bukkit.getOnlinePlayers()) {
            // this.sendPacket(other, packet);
        }

        return this;
    }

    /**
     * Send a packet to the player
     *
     * @param player the player
     * @param packet the packet to send
     */
    public void sendPacket(Player player, Packet<?> packet) {
        getPlayerConnection(player).sendPacket(packet);
    }

    /**
     * Get the {@link PlayerConnection} of a player
     *
     * @param player the player to get the player connection object from
     * @return the object
     */
    public PlayerConnection getPlayerConnection(Player player) {
        return this.getEntityPlayer(player).playerConnection;
    }

    /**
     * Get the {@link EntityPlayer} object of a player
     *
     * @param player the player to get the entity player object from
     * @return the entity player
     */
    public EntityPlayer getEntityPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    /**
     * Get an entity player by a profile
     *
     * @param profile the profile
     * @return the entity player
     */
    public EntityPlayer getEntityPlayer(GameProfile profile) {
        final MinecraftServer server = MinecraftServer.getServer();
        final PlayerInteractManager interactManager = new PlayerInteractManager(server.getWorldServer(0));

        return new EntityPlayer(server, server.getWorldServer(0), profile, interactManager);
    }

    /**
     * Send the {@link PacketPlayOutPlayerInfo} to a player
     *
     * @param player the player
     * @param action the action
     * @param target the target
     */
    public void sendInfoPacket(Player player, PacketPlayOutPlayerInfo.EnumPlayerInfoAction action, EntityPlayer target) {
        this.sendPacket(player, new PacketPlayOutPlayerInfo(action, target));
    }
}
