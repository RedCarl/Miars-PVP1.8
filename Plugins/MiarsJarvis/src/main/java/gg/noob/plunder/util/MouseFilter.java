// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

public class MouseFilter
{
    public float x;
    public float y;
    public float z;
    
    public float smooth(float p_76333_1_, final float p_76333_2_) {
        this.x += p_76333_1_;
        p_76333_1_ = (this.x - this.y) * p_76333_2_;
        this.z += (p_76333_1_ - this.z) * 0.5f;
        if ((p_76333_1_ > 0.0f && p_76333_1_ > this.z) || (p_76333_1_ < 0.0f && p_76333_1_ < this.z)) {
            p_76333_1_ = this.z;
        }
        this.y += p_76333_1_;
        return p_76333_1_;
    }
    
    public void reset() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }
}
