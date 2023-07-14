package cn.mcarl.miars.core.impl.lunarclient.nethandler.server;

import cn.mcarl.miars.core.impl.lunarclient.nethandler.client.LCPacketClientVoice;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.client.LCPacketVoiceChannelSwitch;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.client.LCPacketVoiceMute;
import cn.mcarl.miars.core.impl.lunarclient.nethandler.shared.LCNetHandler;

public interface LCNetHandlerServer extends LCNetHandler {

    void handleStaffModStatus(LCPacketStaffModStatus packet);

    void handleVoice(LCPacketClientVoice packet);

    void handleVoiceMute(LCPacketVoiceMute packet);

    void handleVoiceChannelSwitch(LCPacketVoiceChannelSwitch packet);
}