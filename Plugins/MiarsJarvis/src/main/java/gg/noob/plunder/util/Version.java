// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

public enum Version
{
    UNKNOWN(-1), 
    v_1_7_10(5), 
    v_1_8(47), 
    v_1_9(107), 
    v_1_9_1(108), 
    v_1_9_2(109), 
    v_1_9_3(110), 
    v_1_10(210), 
    v_1_11(315), 
    v_1_11_1(316), 
    v_1_12(335), 
    v_1_12_1(338), 
    v_1_12_2(340), 
    v_1_13(393), 
    v_1_13_1(401), 
    v_1_13_2(404), 
    v_1_14(477), 
    v_1_14_1(480), 
    v_1_14_2(485), 
    v_1_14_3(490), 
    v_1_14_4(498), 
    v_1_15(573), 
    v_1_15_1(575), 
    v_1_15_2(578), 
    v_1_16(735), 
    v_1_16_1(736), 
    v_1_16_2(751), 
    v_1_16_3(753), 
    v_1_16_4(754), 
    v_1_17(755), 
    v_1_17_1(756), 
    v_1_18(757), 
    v_1_18_2(758), 
    v_1_19(759), 
    v_1_19_1(760);
    
    private int protocol;
    
    private Version(final int protocol) {
        this.protocol = protocol;
    }
    
    public int getProtocol() {
        return this.protocol;
    }
}
