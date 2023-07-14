package cn.mcarl.miars.core.impl.lunarclient.nethandler.client;

import lombok.Getter;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.ByteBufWrapper;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.LCPacket;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.server.LCNetHandlerServer;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.shared.LCNetHandler;

import java.io.IOException;

public final class LCPacketClientVoice extends LCPacket {

    @Getter
    private byte[] data;

    public LCPacketClientVoice() {
    }

    public LCPacketClientVoice(byte[] data) {
        this.data = data;
    }

    @Override
    public void write(ByteBufWrapper b) throws IOException {
        writeBlob(b, this.data);
    }

    @Override
    public void read(ByteBufWrapper b) throws IOException {
        this.data = readBlob(b);
    }

    @Override
    public void process(LCNetHandler handler) {
        ((LCNetHandlerServer) handler).handleVoice(this);
    }
}
