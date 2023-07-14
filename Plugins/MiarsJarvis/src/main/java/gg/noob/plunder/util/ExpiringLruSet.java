// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import javax.annotation.Nonnull;
import java.util.Iterator;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.ConcurrentMap;
import java.io.Serializable;
import java.util.Set;
import java.util.AbstractSet;

public class ExpiringLruSet<E> extends AbstractSet<E> implements Set<E>, Serializable
{
    private static final Object PRESENT;
    private final ConcurrentMap<E, Object> map;
    
    public ExpiringLruSet(final long expireMillis) {
        this.map = (ConcurrentMap<E, Object>)CacheBuilder.newBuilder().expireAfterWrite(expireMillis, TimeUnit.MILLISECONDS).build((CacheLoader)new CacheLoader() {
            public Object load(final Object o) throws Exception {
                return ExpiringLruSet.PRESENT;
            }
        }).asMap();
    }
    
    @Nonnull
    @Override
    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.map.containsKey(o);
    }
    
    @Override
    public int size() {
        return this.map.size();
    }
    
    @Override
    public boolean add(final E e) {
        return this.map.put(e, ExpiringLruSet.PRESENT) == null;
    }
    
    static {
        PRESENT = new Object();
    }
}
