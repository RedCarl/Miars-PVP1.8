package cn.mcarl.miars.core.impl.lunarclient.nethandler.client;

import lombok.Getter;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.ByteBufWrapper;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.LCPacket;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.shared.LCNetHandler;

import java.io.IOException;

public final class LCPacketStaffModState extends LCPacket {

    @Getter
    private String mod;
    @Getter
    private boolean state;

    public LCPacketStaffModState() {
    }

    public LCPacketStaffModState(String mod, boolean state) {
        this.mod = mod;
        this.state = state;
    }

    @Override
    public void write(ByteBufWrapper buf) throws IOException {
        buf.writeString(mod);
        buf.buf().writeBoolean(state);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.mod = buf.readString();
        this.state = buf.buf().readBoolean();
    }

    @Override
    public void process(LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleStaffModState(this);
    }

}