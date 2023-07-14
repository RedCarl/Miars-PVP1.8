// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import io.netty.buffer.EmptyByteBuf;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.ChatMessage;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ByteBufReader extends ByteToMessageDecoder
{
    private CraftPlayer player;
    
    public ByteBufReader(final CraftPlayer player) {
        this.player = player;
    }
    
    protected void decode(final ChannelHandlerContext channel, final ByteBuf byteBuf, final List<Object> list) throws Exception {
        try {
            if (this.player == null) {
                return;
            }
            if (channel == null) {
                return;
            }
            if (channel.channel() == null) {
                return;
            }
            if (!channel.channel().isActive()) {
                this.player.getHandle().playerConnection.networkManager.close((IChatBaseComponent)new ChatMessage("Wrongly byte reader!", new Object[0]));
                return;
            }
            if (!channel.channel().isOpen()) {
                this.player.getHandle().playerConnection.networkManager.close((IChatBaseComponent)new ChatMessage("Wrongly byte reader!", new Object[0]));
                return;
            }
            if (!channel.channel().isWritable()) {
                this.player.getHandle().playerConnection.networkManager.close((IChatBaseComponent)new ChatMessage("Wrongly byte reader!", new Object[0]));
                return;
            }
            if (channel.channel().remoteAddress() == null) {
                this.player.getHandle().playerConnection.networkManager.close((IChatBaseComponent)new ChatMessage("Wrongly byte reader!", new Object[0]));
                return;
            }
            if (byteBuf.capacity() < 0) {
                this.player.getHandle().playerConnection.networkManager.close((IChatBaseComponent)new ChatMessage("Wrongly byte reader!", new Object[0]));
                return;
            }
            if (byteBuf.refCnt() < 1) {
                this.player.getHandle().playerConnection.networkManager.close((IChatBaseComponent)new ChatMessage("Wrongly byte reader!", new Object[0]));
                return;
            }
            final String clientAddress = channel.channel().remoteAddress().toString().split(":")[0].replace("/", "");
            if (byteBuf instanceof EmptyByteBuf) {
                list.add(byteBuf.readBytes(byteBuf.readableBytes()));
                return;
            }
            if (byteBuf.array().length > 5000) {
                this.player.getHandle().playerConnection.networkManager.close((IChatBaseComponent)new ChatMessage("Sending a packet with an too big input!", new Object[0]));
                return;
            }
            list.add(byteBuf.readBytes(byteBuf.readableBytes()));
        }
        catch (Exception ex) {}
    }
}
