// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public class SyncCatcher
{
    public static void catchOp(final String reason) {
        if (Thread.currentThread() == MinecraftServer.getServer().primaryThread) {
            throw new IllegalStateException("Synchronous " + reason + "!");
        }
    }
}
