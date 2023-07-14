// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import java.util.Queue;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Stream;

public class JavaV
{
    public static <T> Stream<T> stream(final T... array) {
        return Arrays.stream(array);
    }
    
    public static <T> T firstNonNull(@Nullable final T t, @Nullable final T t2) {
        return (t != null) ? t : t2;
    }
    
    public static <T> Queue<T> trim(final Queue<T> queue, final int n) {
        for (int i = queue.size(); i > n; --i) {
            queue.poll();
        }
        return queue;
    }
}
