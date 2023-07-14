package cn.mcarl.miars.core.impl.lunarclient.nethandler.client;

import lombok.Getter;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.ByteBufWrapper;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.LCPacket;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.shared.LCNetHandler;

import java.io.IOException;

public final class LCPacketWorldBorderRemove extends LCPacket {

    @Getter
    private String id;

    public LCPacketWorldBorderRemove() {
    }

    public LCPacketWorldBorderRemove(String id) {
        this.id = id;
    }

    @Override
    public void write(ByteBufWrapper buf) throws IOException {
        buf.writeString(id);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.id = buf.readString();
    }

    @Override
    public void process(LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleWorldBorderRemove(this);
    }

}