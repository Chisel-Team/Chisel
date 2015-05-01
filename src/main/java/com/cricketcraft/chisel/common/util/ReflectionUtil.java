package com.cricketcraft.chisel.common.util;

import com.cricketcraft.chisel.Chisel;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Reflection Utility
 *
 * @author minecreatr
 */
public class ReflectionUtil {

    /**
     * Gets the field object for a field
     * @param clazz the class
     * @param v the name of the field
     * @return the field
     * @throws NoSuchFieldException
     */
    public static Field getField(Class clazz, String v) throws NoSuchFieldException{
        Field f = clazz.getDeclaredField(v);
        f.setAccessible(true);
        return f;
    }

    /**
     * Get the value of the specified field
     * @param v The Name
     * @param o The Object
     * @return The Value
     */
    public static Object getValue(String v, Object o){
        try {
            return getField(o.getClass(), v).get(o);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets a final field
     * @param v
     * @param clazz the class
     * @param o
     * @param value
     */
    public static void setFinalValue(String v, Class clazz, Object o, Object value){
        try {
            Field f = getField(clazz, v);
            Field mField = getModifiersField();
            mField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            f.set(o, value);
            Chisel.logger.info("Set final field "+v);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static Field getModifiersField(){
        try {
            return getField(Field.class, "modifiers");
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
