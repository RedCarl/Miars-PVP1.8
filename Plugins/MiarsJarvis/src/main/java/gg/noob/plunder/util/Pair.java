// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import java.util.Objects;

public class Pair<X, Y>
{
    private X x;
    private Y y;
    
    public void setX(final X x) {
        this.x = x;
    }
    
    public void setY(final Y y) {
        this.y = y;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        final Pair<?, ?> other = (Pair<?, ?>)o;
        final Object this$x = this.getX();
        final Object other$x = other.getX();
        if (!Objects.equals(this$x, other$x)) {
            return false;
        }
        final Object this$y = this.getY();
        final Object other$y = other.getY();
        return Objects.equals(this$y, other$y);
    }
    
    public Pair clone() {
        return new Pair(this.x, this.y);
    }
    
    @Override
    public String toString() {
        return "Pair(x=" + this.getX() + ", y=" + this.getY() + ")";
    }
    
    public Pair(final X x, final Y y) {
        this.x = x;
        this.y = y;
    }
    
    public X getX() {
        return this.x;
    }
    
    public Y getY() {
        return this.y;
    }
}
