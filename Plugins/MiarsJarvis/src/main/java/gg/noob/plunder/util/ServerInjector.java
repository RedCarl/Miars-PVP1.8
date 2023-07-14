// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import java.util.Arrays;
import java.util.Iterator;
import gg.noob.plunder.data.listener.DataListener;
import gg.noob.plunder.data.PlayerData;
import gg.noob.lib.util.LinkedList;
import gg.noob.plunder.Plunder;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import java.lang.reflect.Field;
import java.util.Collection;

public class ServerInjector
{
    private static final Collection<String> TICKABLE_CLASS_NAMES;
    private Field hookedField;
    
    public void inject() throws Exception {
        final MinecraftServer server = MinecraftServer.getServer();
        final Class<?> serverClass = MinecraftServer.class;
        for (final Field field : serverClass.getDeclaredFields()) {
            try {
                if (field.getType().equals(List.class)) {
                    final Class<?> genericType = (Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                    if (ServerInjector.TICKABLE_CLASS_NAMES.contains(genericType.getSimpleName())) {
                        field.setAccessible(true);
                        final HookedListWrapper<?> wrapper = new HookedListWrapper<Object>((List)field.get(server)) {
                            @Override
                            public void onSize() {
                                try {
                                    final LinkedList<Runnable> runnables = (LinkedList<Runnable>)new LinkedList((Collection)Plunder.getInstance().onTickEnd);
                                    try {
                                        Runnable toRun;
                                        while ((toRun = (Runnable)runnables.poll()) != null) {
                                            toRun.run();
                                            Plunder.getInstance().onTickEnd.remove(toRun);
                                        }
                                    }
                                    catch (Exception ex) {
                                        ex.printStackTrace();
                                        Plunder.getInstance().onTickEnd.clear();
                                    }
                                    for (final PlayerData data : Plunder.getInstance().getDataManager().getPlayerDataSet()) {
                                        if (data == null) {
                                            continue;
                                        }
                                        try {
                                            DataListener.tickEndEvent(data);
                                        }
                                        catch (Exception ex2) {
                                            ex2.printStackTrace();
                                        }
                                    }
                                }
                                catch (Exception ex3) {
                                    ex3.printStackTrace();
                                }
                            }
                        };
                        ReflectionUtils.setUnsafe(server, field, wrapper);
                        this.hookedField = field;
                        break;
                    }
                }
            }
            catch (Exception ex) {}
        }
    }
    
    public void eject() throws Exception {
        if (this.hookedField != null) {
            final MinecraftServer server = MinecraftServer.getServer();
            final HookedListWrapper<?> hookedListWrapper = (HookedListWrapper<?>)this.hookedField.get(server);
            ReflectionUtils.setUnsafe(server, this.hookedField, hookedListWrapper.getBase());
            this.hookedField = null;
        }
    }
    
    static {
        TICKABLE_CLASS_NAMES = Arrays.asList("IUpdatePlayerListBox", "ITickable", "Runnable");
    }
}
