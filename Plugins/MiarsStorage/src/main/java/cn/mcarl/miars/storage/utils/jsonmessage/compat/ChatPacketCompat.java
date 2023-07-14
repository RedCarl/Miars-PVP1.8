package cn.mcarl.miars.storage.utils.jsonmessage.compat;

public interface ChatPacketCompat {
    Object createActionbarPacket(Object chatComponent);
    Object createTextPacket(Object chatComponent);
}
