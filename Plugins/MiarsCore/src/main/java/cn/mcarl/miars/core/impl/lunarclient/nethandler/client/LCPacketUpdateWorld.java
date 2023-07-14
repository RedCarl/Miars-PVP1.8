package cn.mcarl.miars.core.impl.lunarclient.nethandler.client;

import lombok.Getter;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.ByteBufWrapper;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.LCPacket;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.shared.LCNetHandler;

import java.io.IOException;

public final class LCPacketUpdateWorld extends LCPacket {

    @Getter
    private String world;

    public LCPacketUpdateWorld() {
    }

    public LCPacketUpdateWorld(String world) {
        this.world = world;
    }

    @Override
    public void write(ByteBufWrapper buf) throws IOException {
        buf.writeString(world);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.world = buf.readString();
    }

    @Override
    public void process(LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleUpdateWorld(this);
    }

}