package cn.mcarl.miars.storage.utils;

import cn.mcarl.miars.storage.entity.FInventory;
import cn.mcarl.miars.storage.entity.FInventoryByte;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class BukkitUtils {
    public static FInventory byteConvertItemStack(FInventoryByte f){
        Map<Integer, ItemStack> a = new HashMap<>();
        for (Integer i:f.getBackpack().keySet()) {
            a.put(i,read(f.getBackpack().get(i)));
        }
        Map<Integer, ItemStack> b = new HashMap<>();
        for (Integer i:f.getItemCote().keySet()) {
            b.put(i,read(f.getItemCote().get(i)));
        }

        return new FInventory(
                f.getType(),
                read(f.getHelmet()),
                read(f.getChestPlate()),
                read(f.getLeggings()),
                read(f.getBoots()),
                a,
                b
        );
    }

    public static FInventoryByte ItemStackConvertByte(FInventory f){
        Map<Integer, byte[]> a = new HashMap<>();
        for (Integer i:f.getBackpack().keySet()) {
            a.put(i,write(f.getBackpack().get(i)));
        }
        Map<Integer, byte[]> b = new HashMap<>();
        for (Integer i:f.getItemCote().keySet()) {
            b.put(i,write(f.getItemCote().get(i)));
        }

        return new FInventoryByte(
                f.getType(),
                write(f.getHelmet()),
                write(f.getChestPlate()),
                write(f.getLeggings()),
                write(f.getBoots()),
                a,
                b
        );
    }

    public static byte[] write(ItemStack itemStack) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOut = new BukkitObjectOutputStream(out);
            bukkitOut.writeObject(itemStack);
            bukkitOut.close();
            return out.toByteArray();
        } catch (Exception var3) {
            var3.printStackTrace();
            return new byte[0];
        }
    }
    public static ItemStack read(byte[] bytes) {
        try {
            BukkitObjectInputStream bukkitIn = new BukkitObjectInputStream(new ByteArrayInputStream(bytes));
            return (ItemStack)bukkitIn.readObject();
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }
}
