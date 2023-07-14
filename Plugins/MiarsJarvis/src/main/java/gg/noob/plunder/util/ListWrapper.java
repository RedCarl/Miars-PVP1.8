// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import java.util.List;

public abstract class ListWrapper<T> implements List<T>
{
    protected final List<T> base;
    
    public ListWrapper(final List<T> base) {
        this.base = base;
    }
    
    public List<T> getBase() {
        return this.base;
    }
}
