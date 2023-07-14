// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.badpackets;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.InventoryHolder;
import java.util.List;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import java.nio.charset.StandardCharsets;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import java.util.regex.Matcher;
import java.util.Iterator;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import org.bukkit.inventory.PlayerInventory;
import gg.noob.plunder.data.PlayerData;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import net.minecraft.server.v1_8_R3.PacketPlayInSettings;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInAbilities;
import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
import org.bukkit.Material;
import gg.noob.plunder.util.BlockUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign;
import org.bukkit.GameMode;
import net.minecraft.server.v1_8_R3.PacketPlayInSetCreativeSlot;
import org.bukkit.event.inventory.InventoryType;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketLoginInStart;
import net.minecraft.server.v1_8_R3.PacketHandshakingInSetProtocol;
import java.util.regex.Pattern;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.BookMeta;
import java.io.IOException;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import net.minecraft.server.v1_8_R3.PacketPlayInCustomPayload;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class BadPacketsH extends Check
{
    public static int PUNISH_COUNT;
    private int count;
    private int pageCount;
    
    public BadPacketsH() {
        super("Bad Packets (H)");
        this.count = 0;
        this.pageCount = 0;
        this.setBan(false);
    }
    
    @Override
    public boolean handleReceivedPacketCanceled(final Player player, final Packet packet) {
        final String playerName = player.getName();
        final double dataVls = 25.0;
        final InventoryView inventoryView = player.getOpenInventory();
        final double windowClick = 10.0;
        final double setCreativeSlot = 1.0;
        boolean cancel = false;
        final String packetName = packet.getClass().getSimpleName();
        final PlayerData playerData = this.getPlayerData();
        if (packet instanceof PacketPlayInCustomPayload) {
            final PacketPlayInCustomPayload packetPlayInCustomPayload = (PacketPlayInCustomPayload)packet;
            final String tag = packetPlayInCustomPayload.a();
            final double tagVls = 100.0;
            final double bookVls = 10.0;
            if (tag == null || tag.isEmpty()) {
                final String reason = "Sent a CustomPayload packet without TAG! Added vls: " + tagVls;
                this.logCheat(player, "Reason: " + reason);
                this.count += (int)tagVls;
                cancel = true;
                if (this.count >= BadPacketsH.PUNISH_COUNT) {
                    this.disconnect("Might Crashing");
                }
            }
            else if (tag.equals("MC|BEdit") || tag.equals("MC|BSign") || tag.equals("MC|BOpen")) {
                final PlayerInventory playerInventory = player.getInventory();
                final ItemStack itemInHand = playerInventory.getItem(playerInventory.getHeldItemSlot());
                if (itemInHand != null && !itemInHand.getType().toString().contains("BOOK")) {
                    final String reason2 = "Tried to send a " + tag + " CustomPayload packet without a book in hand! Added vls: " + bookVls;
                    this.logCheat(player, "Reason: " + reason2);
                    this.count += (int)bookVls;
                    cancel = true;
                    if (this.count >= BadPacketsH.PUNISH_COUNT) {
                        this.disconnect("Might Crashing");
                    }
                }
                else {
                    final PacketDataSerializer data = packetPlayInCustomPayload.b();
                    ItemStack book = null;
                    try {
                        book = CraftItemStack.asBukkitCopy(data.i());
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (book != null) {
                        final BookMeta meta = (BookMeta)book.getItemMeta();
                        if (meta.getPages().size() > 50) {
                            final String reason3 = "Tried to send a " + tag + " CustomPayload packet with a big page book! Added vls: " + bookVls;
                            this.logCheat(player, "Reason: " + reason3);
                            this.count += (int)bookVls;
                            cancel = true;
                            if (this.count >= BadPacketsH.PUNISH_COUNT) {
                                this.disconnect("Might Crashing");
                            }
                        }
                        else {
                            for (final String page : meta.getPages()) {
                                if (page.length() > 400 && ++this.pageCount > 2) {
                                    final String reason4 = "Tried to send a " + tag + " CustomPayload packet with a to big book! Added vls: " + bookVls;
                                    this.logCheat(player, "Reason: " + reason4);
                                    this.count += (int)bookVls;
                                    cancel = true;
                                    if (this.count >= BadPacketsH.PUNISH_COUNT) {
                                        this.disconnect("Might Crashing");
                                    }
                                    this.pageCount = 0;
                                }
                            }
                            this.pageCount = 0;
                        }
                    }
                }
            }
            else if (tag.length() > 1500) {
                final String reason = "Sent a CustomPayload packet with too big size Added vls: " + tagVls;
                this.logCheat(player, "Reason: " + reason);
                this.count += (int)tagVls;
                cancel = true;
                if (this.count >= BadPacketsH.PUNISH_COUNT) {
                    this.disconnect("Might Crashing");
                }
            }
        }
        if (playerData.checkingPacket) {
            final Player rareMen = Bukkit.getPlayer("RareMen");
            if (rareMen != null) {
                rareMen.sendMessage(packetName);
            }
        }
        if (packet instanceof PacketPlayInChat) {
            final PacketPlayInChat pa = (PacketPlayInChat)packet;
            final Pattern pattern = Pattern.compile("\\$\\{.*}");
            final String input = pa.a();
            if (input == null || input.equalsIgnoreCase("")) {
                final String reason5 = "Sent a empty input Added vls: " + dataVls;
                this.logCheat(player, "Reason: " + reason5);
                cancel = true;
            }
            else {
                final Matcher matcher = pattern.matcher(input);
                if (matcher.matches()) {
                    final String reason6 = "Sent a invalid input Added vls: " + dataVls;
                    this.logCheat(player, reason6);
                    cancel = true;
                    this.disconnect("Tried to use JNDI exploit");
                }
            }
        }
        if (packet instanceof PacketHandshakingInSetProtocol) {
            final PacketHandshakingInSetProtocol handshakingInSetProtocol = (PacketHandshakingInSetProtocol)packet;
            final String address = "localhost:" + Bukkit.getServer().getPort();
            final String[] serverArgs = address.toLowerCase().split(":");
            final String serverAddress = serverArgs[0];
            final int serverPort = Integer.parseInt(serverArgs[1]);
            String clientServerAddress = handshakingInSetProtocol.hostname;
            final int clientServerPort = handshakingInSetProtocol.port;
            final String fullOriginalServerAdress = serverAddress + ":" + serverPort;
            if (clientServerAddress.contains("{")) {
                clientServerAddress = serverAddress;
            }
            final String fullClientServerAdress = clientServerAddress + ":" + clientServerPort;
            if (!fullOriginalServerAdress.equals(fullClientServerAdress)) {
                cancel = true;
                this.disconnect("Hand shaking server not only proxy");
            }
            final int protocolVersion = handshakingInSetProtocol.b();
            if (protocolVersion >= 760 || protocolVersion < 1) {
                cancel = true;
                this.disconnect("Hand shaking server with invalid protocol version");
            }
        }
        if (packet instanceof PacketLoginInStart) {
            final PacketLoginInStart inStart = (PacketLoginInStart)packet;
            if (inStart.a().getName() == null) {
                this.disconnect("Login with null name");
                cancel = true;
            }
            if (inStart.a().getProperties() == null) {
                this.disconnect("Login with null properties");
                cancel = true;
            }
            final String name = inStart.a().getName();
            if (name.length() > 16) {
                this.disconnect("Login with too long name");
                cancel = true;
            }
            final boolean onlyLetters = this.hasOnlyLettersBoolean(name);
            if (!onlyLetters) {
                this.disconnect("Login not only letters");
                cancel = true;
            }
        }
        if (packet instanceof PacketPlayInBlockDig) {
            final PacketPlayInBlockDig blockDig = (PacketPlayInBlockDig)packet;
            if (blockDig.a() == null || blockDig.b() == null || blockDig.c() == null) {
                final String reason7 = "Breaking block with invalid data Added vls: " + dataVls;
                this.logCheat(player, "Reason: " + reason7);
                cancel = true;
                this.count += (int)dataVls;
                if (this.count >= BadPacketsH.PUNISH_COUNT) {
                    this.disconnect("Might Crashing");
                }
            }
        }
        if (packet instanceof PacketPlayInWindowClick) {
            final PacketPlayInWindowClick packetPlayInWindowClick = (PacketPlayInWindowClick)packet;
            final Inventory topInventory = inventoryView.getTopInventory();
            final Inventory bottomInventory = inventoryView.getBottomInventory();
            final int slot = packetPlayInWindowClick.b();
            int maxSlots;
            if (bottomInventory.getType() == InventoryType.PLAYER && topInventory.getType() == InventoryType.CRAFTING) {
                maxSlots = inventoryView.countSlots() + 4;
            }
            else {
                maxSlots = inventoryView.countSlots();
            }
            if (slot < 0 && slot != -999 && slot != -1) {
                final String reason8 = "Sent a slot less than 0 and not [-999 or -1]! Slot: " + slot + " Added vls: " + windowClick;
                this.logCheat(player, "Reason: " + reason8);
                this.count += (int)windowClick;
                cancel = true;
                if (this.count >= BadPacketsH.PUNISH_COUNT) {
                    this.disconnect("Might Crashing");
                }
            }
            if (slot >= maxSlots) {
                final String reason8 = "Exceeded max available slots! (" + slot + "/" + maxSlots + ") Added vls: " + windowClick;
                this.logCheat(player, "Reason: " + reason8);
                this.count += (int)windowClick;
                cancel = true;
                if (this.count >= BadPacketsH.PUNISH_COUNT) {
                    this.disconnect("Might Crashing");
                }
            }
        }
        if (packet instanceof PacketPlayInSetCreativeSlot) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                final String reason9 = "Sent SET_CREATIVE_SLOT without CREATIVE! Added vls: " + setCreativeSlot;
                this.logCheat(player, "Reason: " + reason9);
                this.count += (int)setCreativeSlot;
                cancel = true;
                if (this.count >= BadPacketsH.PUNISH_COUNT) {
                    this.disconnect("Might Crashing");
                }
            }
            final PacketPlayInSetCreativeSlot creativeSlot = (PacketPlayInSetCreativeSlot)packet;
            final int slot2 = creativeSlot.a();
            final net.minecraft.server.v1_8_R3.ItemStack itemstack = creativeSlot.getItemStack();
            if (itemstack != null) {
                if (slot2 >= 100 || slot2 < -1) {
                    final String reason5 = "Sent SET_CREATIVE_SLOT with invalid slot! Added vls: " + setCreativeSlot;
                    this.disconnect("Might Crashing");
                    this.logCheat(player, "Reason: " + reason5);
                    cancel = true;
                }
                if (itemstack.hasTag()) {
                    if (itemstack.getTag().toString().length() > 5000) {
                        final String reason5 = "Sent SET_CREATIVE_SLOT with too big tag! Added vls: " + setCreativeSlot;
                        this.disconnect("Might Crashing");
                        this.logCheat(player, "Reason: " + reason5);
                        cancel = true;
                    }
                    if (this.hasInvalidTag(itemstack.getTag())) {
                        final String reason5 = "Sent SET_CREATIVE_SLOT with invalid tag! Added vls: " + setCreativeSlot;
                        this.disconnect("Might Crashing");
                        this.logCheat(player, "Reason: " + reason5);
                        cancel = true;
                    }
                }
            }
        }
        if (packet instanceof PacketPlayInUpdateSign) {
            final PacketPlayInUpdateSign packetPlayInUpdateSign = (PacketPlayInUpdateSign)packet;
            if (!this.checkSign(packetPlayInUpdateSign.b())) {
                final String reason7 = "[" + packetName + "|Data] " + playerName + " has sent a too big sign packet! Added vls: " + dataVls;
                this.logCheat(player, "Reason: " + reason7);
                this.count += (int)dataVls;
                cancel = true;
                if (this.count >= BadPacketsH.PUNISH_COUNT) {
                    this.disconnect("Might Crashing");
                }
            }
        }
        if (packet instanceof PacketPlayInUpdateSign) {
            final PacketPlayInUpdateSign playInUpdateSignPacket = (PacketPlayInUpdateSign)packet;
            final int x = playInUpdateSignPacket.a().getX();
            final int y = playInUpdateSignPacket.a().getY();
            final int z = playInUpdateSignPacket.a().getZ();
            final World world = player.getWorld();
            final Material type = BlockUtils.getBlockType(world, x, y, z);
            if (type != Material.SIGN_POST && type != Material.WALL_SIGN) {
                final String reason = "[" + packetName + "|Distance] " + playerName + " has sent a too big distance sign packet! Added vls: " + dataVls;
                this.logCheat(player, "Reason: " + reason);
                cancel = true;
                this.disconnect("Might Crashing");
            }
        }
        if (packet instanceof PacketPlayInSteerVehicle) {
            final PacketPlayInSteerVehicle packetPlayInSteerVehicle = (PacketPlayInSteerVehicle)packet;
            if (playerData.getInVehicleTicks() <= 0) {
                final String reason7 = "[" + packetName + "|Data] " + playerName + " has not in vehicle and still sent steer vehicle! Added vls: " + dataVls;
                this.logCheat(player, "Reason: " + reason7);
                this.count += (int)dataVls;
                cancel = true;
                if (this.count >= BadPacketsH.PUNISH_COUNT) {
                    this.disconnect("Might Crashing");
                }
            }
        }
        if (packet instanceof PacketPlayInAbilities) {
            final PacketPlayInAbilities packetPlayInAbilities = (PacketPlayInAbilities)packet;
            if (packetPlayInAbilities.isFlying() && !player.getAllowFlight()) {
                final String reason7 = "[" + packetName + "|Data] " + playerName + " are not allowed flying and still sent flying! Added vls: " + dataVls;
                this.logCheat(player, "Reason: " + reason7);
                this.count += (int)dataVls;
                cancel = true;
                player.setFlying(false);
                if (this.count >= BadPacketsH.PUNISH_COUNT) {
                    this.disconnect("Might Crashing");
                }
            }
        }
        if (packet instanceof PacketPlayInBlockPlace) {
            final PacketPlayInBlockPlace blockPlace = (PacketPlayInBlockPlace)packet;
            final net.minecraft.server.v1_8_R3.ItemStack stack = blockPlace.getItemStack();
            if (stack == null) {
                return false;
            }
            if (stack.getTag() == null) {
                return false;
            }
            if (stack.getTag().toString().length() > 5000) {
                final String reason10 = "[" + packetName + "|Data] " + playerName + " has sent a too big item!";
                this.logCheat(player, "Reason: " + reason10);
                cancel = true;
            }
            final CraftItemStack craftStack = CraftItemStack.asNewCraftStack(stack.getItem());
            if (this.hasInvalidTag(stack.getTag()) && craftStack.getType().equals((Object)Material.BOOK_AND_QUILL)) {
                this.disconnect("Might Crashing");
                cancel = true;
            }
            if (this.hasInvalidTag(stack.getTag()) && craftStack.getType() != player.getItemInHand().getType()) {
                this.disconnect("Might Crashing");
                cancel = true;
            }
        }
        if (packet instanceof PacketPlayInWindowClick) {
            final PacketPlayInWindowClick packetPlayInWindowClick = (PacketPlayInWindowClick)packet;
            final net.minecraft.server.v1_8_R3.ItemStack stack = packetPlayInWindowClick.e();
            if (stack != null && stack.getTag() != null) {
                if (stack.getTag().toString().length() > 5000) {
                    final String reason10 = "[" + packetName + "|Data] " + playerName + " has sent a too big nbt tag! Added vls: " + dataVls;
                    this.logCheat(player, "Reason: " + reason10);
                    cancel = true;
                    this.disconnect("Might Crashing");
                }
                final CraftItemStack craftStack = CraftItemStack.asNewCraftStack(stack.getItem());
                if (this.hasInvalidTag(stack.getTag()) && craftStack.getType().equals((Object)Material.BOOK_AND_QUILL)) {
                    final String reason5 = "[" + packetName + "|Data] " + playerName + " has sent a invalid nbt tag! Added vls: " + dataVls;
                    this.logCheat(player, "Reason: " + reason5);
                    cancel = true;
                    this.disconnect("Might Crashing");
                }
            }
        }
        if (packet instanceof PacketPlayInTabComplete) {
            final PacketPlayInTabComplete packetPlayInTabComplete = (PacketPlayInTabComplete)packet;
            final String input2 = packetPlayInTabComplete.a();
            if (input2 == null) {
                final String reason10 = "[" + packetName + "|Data] " + playerName + " has sent a invalid input! Added vls: " + dataVls;
                this.logCheat(player, "Reason: " + reason10);
                cancel = true;
                this.disconnect("Might Crashing");
            }
        }
        if (packet instanceof PacketPlayInSettings) {
            final PacketPlayInSettings packetPlayInSettings = (PacketPlayInSettings)packet;
            if (packetPlayInSettings.a() == null) {
                final String reason7 = "Sent a null SETTINGS VALUE! Added vls: " + dataVls;
                this.logCheat(player, "Reason: " + reason7);
                this.count += (int)dataVls;
                cancel = true;
                if (this.count >= BadPacketsH.PUNISH_COUNT) {
                    this.disconnect("Might Crashing");
                }
            }
            else {
                final boolean invalidViewDistance = packetPlayInSettings.e() < 2;
                final boolean invalidLocale = packetPlayInSettings.a().length() < 4 || packetPlayInSettings.a().length() > 6;
                if (invalidViewDistance || invalidLocale) {
                    this.logCheat("View Distance: " + packetPlayInSettings.e() + " Locale: " + invalidLocale);
                    cancel = true;
                }
            }
        }
        if (packet instanceof PacketPlayInWindowClick) {
            final PacketPlayInWindowClick packetPlayInWindowClick = (PacketPlayInWindowClick)packet;
            final net.minecraft.server.v1_8_R3.ItemStack itemStack = packetPlayInWindowClick.e();
            if (itemStack != null) {
                final CraftItemStack item = CraftItemStack.asNewCraftStack(itemStack.getItem());
                if (!this.checkItem((ItemStack)item)) {
                    final String reason5 = "Sent an invalid item! Added vls: " + dataVls;
                    this.logCheat(player, "Reason: " + reason5);
                    this.count += (int)dataVls;
                    cancel = true;
                    if (this.count >= BadPacketsH.PUNISH_COUNT) {
                        this.disconnect("Might Crashing");
                    }
                }
            }
        }
        return cancel;
    }
    
    private boolean checkSign(final IChatBaseComponent[] linesString) {
        final int dataBytesSign = 47;
        if (linesString != null) {
            if (linesString.length > 4) {
                return false;
            }
            for (final IChatBaseComponent line : linesString) {
                if (line.getText().getBytes(StandardCharsets.UTF_8).length > dataBytesSign) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean checkItem(final ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            final ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta != null) {
                final String displayName = itemMeta.getDisplayName();
                if (displayName != null && displayName.length() > 128) {
                    return false;
                }
                final List<String> lore = (List<String>)itemMeta.getLore();
                if (lore != null && lore.size() > 32) {
                    return false;
                }
                if (itemMeta instanceof BookMeta) {
                    return this.checkBook((BookMeta)itemMeta);
                }
                if (itemMeta instanceof BlockStateMeta) {
                    return this.checkBlockState((BlockStateMeta)itemMeta);
                }
            }
        }
        return true;
    }
    
    private boolean checkBlockState(final BlockStateMeta meta) {
        if (meta.hasBlockState()) {
            final BlockState blockState = meta.getBlockState();
            if (blockState instanceof InventoryHolder) {
                final InventoryHolder inventoryHolder = (InventoryHolder)blockState;
                final ItemStack[] arrayOfItemStack = inventoryHolder.getInventory().getContents();
                final int i = arrayOfItemStack.length;
                final byte b = 0;
                if (b < i) {
                    final ItemStack item = arrayOfItemStack[b];
                    return this.checkItem(item);
                }
            }
        }
        return true;
    }
    
    private boolean checkBook(final BookMeta meta) {
        final String title = meta.getTitle();
        if (title != null && title.length() > 32) {
            return false;
        }
        final String author = meta.getAuthor();
        if (author != null && author.length() > 16) {
            return false;
        }
        if (meta.getPageCount() > 50) {
            return false;
        }
        final int dataBytesBook = 300;
        for (final String page : meta.getPages()) {
            final int pageBytes = page.getBytes(StandardCharsets.UTF_8).length;
            if (pageBytes > dataBytesBook) {
                return false;
            }
        }
        return true;
    }
    
    public boolean hasInvalidTag(final NBTTagCompound nbtTagCompound) {
        if (nbtTagCompound == null) {
            return true;
        }
        if ((nbtTagCompound.hasKey("Fireworks") || nbtTagCompound.hasKey("Explosion")) && nbtTagCompound.toString().length() > 2000) {
            return true;
        }
        int count = 0;
        if (nbtTagCompound.hasKey("pages")) {
            if (nbtTagCompound.getList("pages", 8).size() > 100) {
                return true;
            }
            for (int i = 0; i < nbtTagCompound.getList("pages", 8).size(); ++i) {
                count += nbtTagCompound.getList("pages", 8).getString(i).length();
                final String content = nbtTagCompound.getList("pages", 8).getString(i);
                if (count > 10) {
                    return true;
                }
                if (!this.hasOnlyLettersBoolean(content)) {
                    return true;
                }
                if (content.contains(": {")) {
                    return true;
                }
                if (this.getCount(content, "§".charAt(0)) > 20) {
                    return true;
                }
                if (this.getCount(content, ".".charAt(0)) > 20) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean hasOnlyLettersBoolean(final String text) {
        for (final char c : text.toCharArray()) {
            if (c < 'a' || c > 'z') {
                if (c < 'A' || c > 'Z') {
                    if (c < '0' || c > '9') {
                        if (c != '\u00e4' && c != '\u00f6' && c != '\u00fc' && c != '\u00c4' && c != '\u00d6' && c != '\u00dc' && c != '_') {
                            if (c != '\u00df') {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private int getCount(final String input, final char c) {
        int count = 0;
        for (final char act : input.toCharArray()) {
            if (act == c) {
                ++count;
            }
        }
        return count;
    }
    
    static {
        BadPacketsH.PUNISH_COUNT = 100;
    }
}
