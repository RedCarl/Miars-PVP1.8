package cn.mcarl.miars.core.utils.jsonmessage.compat;

public interface ChatPacketCompat {
    Object createActionbarPacket(Object chatComponent);
    Object createTextPacket(Object chatComponent);
}
