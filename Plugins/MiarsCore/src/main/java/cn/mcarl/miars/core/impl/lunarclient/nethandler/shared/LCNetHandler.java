package cn.mcarl.miars.core.impl.lunarclient.nethandler.shared;

public interface LCNetHandler {

    void handleAddWaypoint(LCPacketWaypointAdd packet);

    void handleRemoveWaypoint(LCPacketWaypointRemove packet);

    void handleEmote(LCPacketEmoteBroadcast packet);

}