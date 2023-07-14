package cn.mcarl.miars.core.impl.lunarclient.nethandler.client;

import lombok.Getter;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.ByteBufWrapper;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.LCPacket;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.shared.LCNetHandler;

import java.io.IOException;
import java.util.UUID;

public final class LCPacketHologramRemove extends LCPacket {

    @Getter
    private UUID uuid;

    public LCPacketHologramRemove() {
    }

    public LCPacketHologramRemove(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void write(ByteBufWrapper buf) throws IOException {
        buf.writeUUID(uuid);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.uuid = buf.readUUID();
    }

    @Override
    public void process(LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleRemoveHologram(this);
    }

}