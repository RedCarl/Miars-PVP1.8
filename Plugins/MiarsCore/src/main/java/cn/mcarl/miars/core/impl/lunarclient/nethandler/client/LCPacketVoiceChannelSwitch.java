package cn.mcarl.miars.core.impl.lunarclient.nethandler.client;

import lombok.Getter;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.ByteBufWrapper;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.LCPacket;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.server.LCNetHandlerServer;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.shared.LCNetHandler;

import java.io.IOException;
import java.util.UUID;

public final class LCPacketVoiceChannelSwitch extends LCPacket {

    @Getter
    private UUID switchingTo;

    public LCPacketVoiceChannelSwitch() {
    }

    public LCPacketVoiceChannelSwitch(UUID switchingTo) {
        this.switchingTo = switchingTo;
    }

    @Override
    public void write(ByteBufWrapper b) throws IOException {
        b.writeUUID(switchingTo);
    }

    @Override
    public void read(ByteBufWrapper b) throws IOException {
        this.switchingTo = b.readUUID();
    }

    @Override
    public void process(LCNetHandler handler) {
        ((LCNetHandlerServer) handler).handleVoiceChannelSwitch(this);
    }
}
