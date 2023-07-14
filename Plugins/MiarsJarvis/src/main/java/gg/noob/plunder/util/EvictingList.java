// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import java.util.Collection;
import java.util.LinkedList;

public class EvictingList<T> extends LinkedList<T>
{
    private int maxSize;
    
    public EvictingList(final int maxSize) {
        this.maxSize = maxSize;
    }
    
    public EvictingList(final Collection<? extends T> c, final int maxSize) {
        super(c);
        this.maxSize = maxSize;
    }
    
    public int getMaxSize() {
        return this.maxSize;
    }
    
    @Override
    public boolean add(final T t) {
        if (this.size() >= this.maxSize) {
            this.removeFirst();
        }
        return super.add(t);
    }
    
    public boolean isFull() {
        return this.size() >= this.maxSize;
    }
}
