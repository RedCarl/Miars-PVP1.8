package cn.mcarl.miars.pay.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class MapColor {
    public static Map<Integer, int[]> colorMap = (Map)new HashMap<>();

    static {
        putMapColor(1, 127, 178, 56);
        putMapColor(2, 247, 233, 163);
        putMapColor(3, 199, 199, 199);
        putMapColor(4, 255, 0, 0);
        putMapColor(5, 160, 160, 255);
        putMapColor(6, 167, 167, 167);
        putMapColor(7, 0, 124, 0);
        putMapColor(8, 255, 255, 255);
        putMapColor(9, 164, 168, 184);
        putMapColor(10, 151, 109, 77);
        putMapColor(11, 112, 112, 112);
        putMapColor(12, 64, 64, 255);
        putMapColor(13, 143, 119, 72);
        putMapColor(14, 255, 252, 245);
        putMapColor(15, 216, 127, 51);
        putMapColor(16, 178, 76, 216);
        putMapColor(17, 102, 153, 216);
        putMapColor(18, 229, 229, 51);
        putMapColor(19, 127, 204, 25);
        putMapColor(20, 242, 127, 165);
        putMapColor(21, 76, 76, 76);
        putMapColor(22, 153, 153, 153);
        putMapColor(23, 76, 127, 153);
        putMapColor(24, 127, 63, 178);
        putMapColor(25, 51, 76, 178);
        putMapColor(26, 102, 76, 51);
        putMapColor(27, 102, 127, 51);
        putMapColor(28, 153, 51, 51);
        putMapColor(29, 25, 25, 25);
        putMapColor(30, 250, 238, 77);
        putMapColor(31, 92, 219, 213);
        putMapColor(32, 74, 128, 255);
        putMapColor(33, 0, 217, 58);
        putMapColor(34, 129, 86, 49);
        putMapColor(35, 112, 2, 0);
        putMapColor(36, 209, 177, 161);
        putMapColor(37, 159, 82, 36);
        putMapColor(38, 149, 87, 108);
        putMapColor(39, 112, 108, 138);
        putMapColor(40, 186, 133, 36);
        putMapColor(41, 103, 117, 53);
        putMapColor(42, 160, 77, 78);
        putMapColor(43, 57, 41, 35);
        putMapColor(44, 135, 107, 98);
        putMapColor(45, 87, 92, 92);
        putMapColor(46, 122, 73, 88);
        putMapColor(47, 76, 62, 92);
        putMapColor(48, 76, 50, 35);
        putMapColor(49, 76, 82, 42);
        putMapColor(50, 142, 60, 46);
        putMapColor(51, 37, 22, 16);
    }

    public static void putMapColor(int id, int r, int g, int b) {
        colorMap.put(Integer.valueOf(id * 4), color(0.71D, r, g, b));
        colorMap.put(Integer.valueOf(id * 4 + 1), color(0.86D, r, g, b));
        colorMap.put(Integer.valueOf(id * 4 + 2), color(1.0D, r, g, b));
        colorMap.put(Integer.valueOf(id * 4 + 3), color(0.53D, r, g, b));
    }

    public static int[] color(double a, int red, int green, int blue) {
        return new int[] { (int)(a * red), (int)(a * green), (int)(a * blue) };
    }

    public static byte colorPicker(int r, int g, int b) {
        byte id = 0;
        double similarity = 196608.0D;
        for (Map.Entry<Integer, int[]> en : colorMap.entrySet()) {
            int R = r - ((int[])en.getValue())[0];
            int G = g - ((int[])en.getValue())[1];
            int B = b - ((int[])en.getValue())[2];
            double s = (R * R + G * G + B * B);
            if (s < similarity) {
                id = ((Integer)en.getKey()).byteValue();
                similarity = s;
            }
        }
        return id;
    }

    public static byte[] getByte(BufferedImage bufferedImage) {
        byte[] bys = new byte[16384];
        int n = 0;
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                Color color = new Color(bufferedImage.getRGB(j, i));
                int e = colorPicker(color.getRed(), color.getGreen(), color.getBlue());
                bys[n] = (byte)e;
                n++;
            }
        }
        return bys;
    }
}
