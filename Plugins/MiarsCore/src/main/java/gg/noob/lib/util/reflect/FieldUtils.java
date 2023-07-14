package gg.noob.lib.util.reflect;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FieldUtils {

    public static List<Field> getFields(Class<?> c, Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        try {
            for (Field field : c.getDeclaredFields()) {
                if (field.getType().equals(type)) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
        } catch (IllegalArgumentException | SecurityException e1) {
            e1.printStackTrace();
        }
        return fields;
    }

    public static Object get(Field f, Object obj) {
        try {
            f.setAccessible(true);
            return f.get(obj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static boolean set(@Nullable Field f, Object obj, Object value) {
        if (f == null) return false;
        try {
            f.setAccessible(true);
            f.set(obj, value);
            return true;
        } catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Field getField(Class<?> c, String name) {
        try{
            Field field = c.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch(IllegalArgumentException | NoSuchFieldException | SecurityException ignored) {
            return null;
        }
    }

}
