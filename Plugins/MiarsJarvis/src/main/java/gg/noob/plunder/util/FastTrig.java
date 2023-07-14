/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package gg.noob.plunder.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FastTrig {
    private static final double ONE_SIXTH = 0.16666666666666666;
    private static final int FRAC_EXP = 8;
    private static final int LUT_SIZE = 257;
    private static final double FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
    private static final double[] ASIN_TAB = new double[257];
    private static final double[] COS_TAB = new double[257];

    public static double invSqrt(double x) {
        double xhalf = 0.5 * x;
        long i = Double.doubleToRawLongBits(x);
        i = 6910469410427058090L - (i >> 1);
        x = Double.longBitsToDouble(i);
        x *= 1.5 - xhalf * x * x;
        return x;
    }

    public static String user(String user) {
        user = "%%__USER__%%";
        Player player = Bukkit.getPlayer((String)user);
        return player.getName();
    }

    public static double fast_atan2(double y, double x) {
        boolean steep;
        boolean negX;
        boolean negY;
        double d2 = x * x + y * y;
        if (Double.isNaN(d2) || Double.doubleToRawLongBits(d2) < 0x10000000000000L) {
            return Double.NaN;
        }
        boolean bl = negY = y < 0.0;
        if (negY) {
            y = -y;
        }
        boolean bl2 = negX = x < 0.0;
        if (negX) {
            x = -x;
        }
        boolean bl3 = steep = y > x;
        if (steep) {
            double t = x;
            x = y;
            y = t;
        }
        double rinv = FastTrig.invSqrt(d2);
        x *= rinv;
        double yp = FRAC_BIAS + (y *= rinv);
        int ind = (int)Double.doubleToRawLongBits(yp);
        double a = ASIN_TAB[ind];
        double ca = COS_TAB[ind];
        double sa = yp - FRAC_BIAS;
        double sd = y * ca - x * sa;
        double d = (6.0 + sd * sd) * sd * 0.16666666666666666;
        double c = a + d;
        if (steep) {
            c = 1.5707963267948966 - c;
        }
        if (negX) {
            c = Math.PI - c;
        }
        if (negY) {
            c = -c;
        }
        return c;
    }

    static {
        for (int ind = 0; ind < 257; ++ind) {
            double v = (double)ind / 256.0;
            double asinv = Math.asin(v);
            FastTrig.COS_TAB[ind] = Math.cos(asinv);
            FastTrig.ASIN_TAB[ind] = asinv;
        }
    }
}

