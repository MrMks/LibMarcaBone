package com.github.mrmks.mc.dev_tools_b.nbt;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class NBTUtils {
    private static boolean available;
    private static final String p_nms;
    private static final String p_obc;

    private static EnumMap<EnumTagType, Class<?>> classEnumMap;
    private static EnumMap<EnumTagType, Constructor<?>> constructorEnumMap;
    private static EnumMap<EnumTagType, Method> methodEnumMap;
    private static HashMap<Class<?>, EnumTagType> reClassMap;
    private static Field fieldOfLongArrayTag;

    private static Method[] listMethod;
    private static Method[] compoundMethod;

    private static Class<?> classOfOBCItem;
    private static Class<?> classOfNMSItem;
    private static Field fieldOfItemHandle;
    private static Method[] nmsItemMethod;

    private static Method craftCopyMethod;
    static {
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        p_obc = ver;
        p_nms = "net.minecraft.server." + ver.substring(ver.lastIndexOf(".") + 1);
        available = true;

        Class<?> nbt_base_klass = loadNMSClass("NBTBase");
        if (nbt_base_klass == null) available = false;

        if (available) {
            classEnumMap = new EnumMap<>(EnumTagType.class);
            constructorEnumMap = new EnumMap<>(EnumTagType.class);
            reClassMap = new HashMap<>();
            for (EnumTagType type : EnumTagType.values()) {
                if (!available) break;
                Class<?> klass = loadNMSClass(type.getClassName());
                classEnumMap.put(type, klass);
                reClassMap.put(klass, type);

                if (type == EnumTagType.END) continue;
                if (klass != null) {
                    Constructor<?> c = type.hasConstructorParams() ? loadClassConstructor(klass, type.getConstructorParamTypes()) : loadClassConstructor(klass);
                    if (c != null) constructorEnumMap.put(type, c);
                    else available = false;
                } else available = false;
            }
        }

        if (available) {
            methodEnumMap = new EnumMap<>(EnumTagType.class);
            for (EnumTagType type : EnumTagType.values()) {
                if (!available) break;
                Class<?> k = classEnumMap.get(type);
                if (!type.getGetterName().isEmpty()) {
                    try {
                        methodEnumMap.put(type, loadMethod(k, type.getGetterName()));
                    } catch (Throwable e) {
                        available = false;
                    }
                } else {
                    if (type == EnumTagType.LONG_ARRAY) {
                        try {
                            fieldOfLongArrayTag = k.getDeclaredField("b");
                            fieldOfLongArrayTag.setAccessible(true);
                        } catch (Throwable e) {
                            available = false;
                        }
                    }
                }
            }
        }

        if (available) {
            Class<?> k = classEnumMap.get(EnumTagType.LIST);
            try {
                listMethod = new Method[]{
                        loadMethod(k, "add", nbt_base_klass),             //add
                        loadMethod(k, "a", int.class, nbt_base_klass),    //add
                        loadMethod(k, "i", int.class),                    //get
                        loadMethod(k, "remove", int.class),               //remove
                        loadMethod(k, "isEmpty"),                         //isEmpty
                        loadMethod(k, "size")                             //size
                };
            } catch (Throwable e) {
                available = false;
            }
            k = classEnumMap.get(EnumTagType.COMPOUND);
            try {
                compoundMethod = new Method[]{
                        loadMethod(k, "set", String.class, nbt_base_klass),   //set
                        loadMethod(k, "get", String.class),                   //get
                        loadMethod(k, "remove", String.class),                //remove
                        loadMethod(k, "isEmpty"),                             //isEmpty
                        loadMethod(k, "d"),                                   //size
                        loadMethod(k, "hasKey", String.class),                //hasKey
                        loadMethod(k, "c")                                    //keySet
                };
            } catch (Throwable e) {
                available = false;
            }
        }

        if (available) {
            classOfOBCItem = loadOBCClass("inventory.CraftItemStack");
            classOfNMSItem = loadNMSClass("ItemStack");
            if (classOfNMSItem != null && classOfOBCItem != null) {
                try {
                    fieldOfItemHandle = classOfOBCItem.getDeclaredField("handle");
                    fieldOfItemHandle.setAccessible(true);
                    nmsItemMethod = new Method[] {
                            loadMethod(classOfNMSItem, "getTag"),                                             // getTag
                            loadMethod(classOfNMSItem, "setTag", classEnumMap.get(EnumTagType.COMPOUND))      // setTag
                    };
                } catch (Throwable e) {
                    available = false;
                }
            } else available = false;
        }

        if (available) {
            try {
                craftCopyMethod = loadMethod(classOfOBCItem, "asCraftCopy", ItemStack.class);
            } catch (Throwable tr) {
                available = false;
            }
        }

        if (!available) {
            craftCopyMethod = null;
            classOfOBCItem = null;
            classOfOBCItem = null;
            fieldOfItemHandle = null;
            fieldOfLongArrayTag = null;
            if (classEnumMap != null) classEnumMap.clear();
            if (reClassMap != null) reClassMap.clear();
            if (constructorEnumMap != null) constructorEnumMap.clear();
            if (methodEnumMap != null) methodEnumMap.clear();
            if (listMethod != null) Arrays.fill(listMethod, null);
            if (compoundMethod != null) Arrays.fill(compoundMethod, null);
            if (nmsItemMethod != null) Arrays.fill(nmsItemMethod, null);
        }
    }

    public static boolean isAvailable() {
        return available;
    }

    private static void chkAv0() {
        if (!available) {
            throw new IllegalStateException("Can't access this method while the NBTUtils#isAvailable() return false.");
        }
    }

    // reflect methods
    // this method will return null if the class can't be found
    static Class<?> loadNMSClass(String klassName) {
        chkAv0();
        return loadClass(p_nms, klassName);
    }

    static Class<?> loadOBCClass(String klassName) {
        chkAv0();
        return loadClass(p_obc, klassName);
    }

    private static Class<?> loadClass(String pkg, String kls) {
        Class<?> ret = null;
        try {ret = Class.forName(pkg + '.' + kls);} catch (Throwable ignored){}
        return ret;
    }

    static Constructor<?> loadClassConstructor(Class<?> k, Class<?>... classes) {
        try {
            Constructor<?> c = k.getDeclaredConstructor(classes);
            c.setAccessible(true);
            return c;
        } catch (Throwable e) {
            return null;
        }
    }

    static Method loadMethod(Class<?> k, String name, Class<?>... classes) throws NoSuchMethodException {
        Method m = k.getDeclaredMethod(name, classes);
        m.setAccessible(true);
        return m;
    }

    // new instance
    static Object newInstance(EnumTagType type) {
        return invokeConstructor(type, null);
    }

    static Object newInstance(EnumTagType type, Object d) {
        return invokeConstructor(type, d);
    }

    static Object newByte(byte d) {
        return invokeConstructor(EnumTagType.BYTE, d);
    }

    static Object newShort(short d) {
        return invokeConstructor(EnumTagType.SHORT, d);
    }

    static Object newInt(int d) {
        return invokeConstructor(EnumTagType.INT, d);
    }

    static Object newLong(long d) {
        return invokeConstructor(EnumTagType.LONG, d);
    }

    static Object newFloat(float d) {
        return invokeConstructor(EnumTagType.FLOAT, d);
    }

    static Object newDouble(double d) {
        return invokeConstructor(EnumTagType.DOUBLE, d);
    }

    static Object newByteArray(byte[] d) {
        return invokeConstructor(EnumTagType.BYTE_ARRAY, d);
    }

    static Object newString(String d) {
        return invokeConstructor(EnumTagType.STRING, d);
    }

    static Object newList() {
        return invokeConstructor(EnumTagType.LIST, null);
    }

    static Object newCompound() {
        return invokeConstructor(EnumTagType.COMPOUND, null);
    }

    static Object newIntArray(int[] d) {
        return invokeConstructor(EnumTagType.INT_ARRAY, d);
    }

    static Object newLongArray(long[] d) {
        return invokeConstructor(EnumTagType.LONG_ARRAY, d);
    }

    private static Object invokeConstructor(EnumTagType type, Object data) {
        chkAv0();
        Constructor<?> c = constructorEnumMap.get(type);
        try {
            return type.hasConstructorParams() ? c.newInstance(data) : c.newInstance();
        } catch (Throwable tr) {
            // this should never happen since the reflect method has been tested in static code block;
            // when this happen, NBTUtils will stop itself;
            available = false;
        }
        return null;
    }

    // get
    private static Object getTagValue(EnumTagType type, TagBase tag, Object def) {
        if (!type.getGetterName().isEmpty()) {
            try {
                return methodEnumMap.get(type).invoke(tag.getNMSIns());
            } catch (IllegalAccessException | InvocationTargetException e) {
                return def;
            }
        } else {
            if (type == EnumTagType.LONG_ARRAY) {
                try {
                    return fieldOfLongArrayTag.get(tag.getNMSIns());
                } catch (IllegalAccessException e) {
                    return def;
                }
            }
        }
        return def;
    }

    static byte getByteVal(TagByte tag) {
        return (byte) getTagValue(EnumTagType.BYTE, tag, 0);
    }

    static short getShortVal(TagShort tag) {
        return (short) getTagValue(EnumTagType.SHORT, tag, 0);
    }

    static int getIntVal(TagInt tag) {
        return (int) getTagValue(EnumTagType.INT, tag,0);
    }

    static long getLongVal(TagLong tag) {
        return (long) getTagValue(EnumTagType.LONG, tag, 0);
    }

    static float getFloatVal(TagFloat tag) {
        return (float) getTagValue(EnumTagType.FLOAT, tag, 0.0f);
    }

    static double getDoubleVal(TagDouble tag) {
        return (double) getTagValue(EnumTagType.DOUBLE, tag, 0.0d);
    }

    static byte[] getByteArrayVal(TagByteArray tag) {
        return (byte[]) getTagValue(EnumTagType.BYTE_ARRAY, tag, new byte[0]);
    }

    static int[] getIntArrayVal(TagIntArray tag) {
        return (int[]) getTagValue(EnumTagType.INT_ARRAY, tag, new int[0]);
    }

    static long[] getLongArrayVal(TagLongArray tag) {
        return (long[]) getTagValue(EnumTagType.LONG_ARRAY, tag, new long[0]);
    }

    static String getStringVal(TagString tag) {
        return (String) getTagValue(EnumTagType.STRING, tag, "");
    }

    // instance test
    static boolean isInstanceOf(EnumTagType type, Object obj) {
        chkAv0();
        return classEnumMap.get(type).isInstance(obj);
    }

    // wrap nms instance to my nbt instance
    private static TagBase wrap(Object obj) {
        if (obj == null) return null;

        TagBase tag = null;
        EnumTagType type = reClassMap.get(obj.getClass());
        if (type == null) {
            for (Map.Entry<EnumTagType, Class<?>> entry : classEnumMap.entrySet()) {
                if (entry.getValue().isInstance(obj)) {
                    type = entry.getKey();
                    break;
                }
            }
        }
        if (type != null) {
            tag = generateWrapIns(type);
            tag.wrapNMSIns(obj);
        }
        return tag;
    }

    // generate a empty nbt instance
    private static TagBase generateWrapIns(EnumTagType type) {
        switch (type) {
            case BYTE:
                return new TagByte();
            case SHORT:
                return new TagShort();
            case INT:
                return new TagInt();
            case LONG:
                return new TagLong();
            case FLOAT:
                return new TagFloat();
            case DOUBLE:
                return new TagDouble();
            case BYTE_ARRAY:
                return new TagByteArray();
            case STRING:
                return new TagString();
            case INT_ARRAY:
                return new TagIntArray();
            case LONG_ARRAY:
                return new TagLongArray();
            case LIST:
                return new TagList(false);
            case COMPOUND:
                return new TagCompound(false);
            case END:
            default:
                return new TagEnd();
        }
    }

    // list methods
    static void listAdd(TagList tag, TagBase val) {
        chkAv0();
        try {
            listMethod[0].invoke(tag.getNMSIns(), val.getNMSIns());
        } catch (Throwable tr) {
            // this should never happen;
        }
    }

    static void listAdd(TagList tag, int index, TagBase val) {
        chkAv0();
        try {
            listMethod[1].invoke(tag.getNMSIns(), index, val.getNMSIns());
        } catch (Throwable tr) {
            // this should never happen;
        }
    }

    static TagBase listRemove(TagList list, int index) {
        chkAv0();
        try {
            Object obj = listMethod[3].invoke(list.getNMSIns(), index);
            return wrap(obj);
        } catch (Throwable ignored) {}
        return new TagEnd();
    }

    static TagBase listGet(TagList list, int index) {
        chkAv0();
        try {
            Object obj = listMethod[2].invoke(list.getNMSIns(), index);
            return wrap(obj);
        } catch (Throwable ignored) {
            return new TagEnd();
        }
    }

    static boolean listIsEmpty(TagList list) {
        chkAv0();
        try {
            return (boolean) listMethod[4].invoke(list.getNMSIns());
        } catch (Throwable tr) {
            return true;
        }
    }

    static int listSize(TagList list) {
        chkAv0();
        try {
            return (int) listMethod[5].invoke(list.getNMSIns());
        } catch (Throwable tr) {
            return 0;
        }
    }

    // compound methods
    static void compoundSet(TagCompound cmp, String key, TagBase val) {
        chkAv0();
        try {
            compoundMethod[0].invoke(cmp.getNMSIns(), key, val.getNMSIns());
        } catch (Throwable ignored){}
    }

    static TagBase compoundGet(TagCompound cmp, String key) {
        chkAv0();
        try {
            Object o = compoundMethod[1].invoke(cmp.getNMSIns(), key);
            return wrap(o);
        } catch (Throwable tr) {
            return null;
        }
    }

    static void compoundRemove(TagCompound cmp, String key) {
        chkAv0();
        try {
            compoundMethod[2].invoke(cmp.getNMSIns(), key);
        } catch (Throwable ignored) {}
    }

    static boolean compoundIsEmpty(TagCompound cmp) {
        chkAv0();
        try {
            return (boolean) compoundMethod[3].invoke(cmp.getNMSIns());
        } catch (Throwable tr) {
            return true;
        }
    }

    static int compoundSize(TagCompound cmp) {
        chkAv0();
        try {
            return (int) compoundMethod[4].invoke(cmp.getNMSIns());
        } catch (Throwable tr) {
            return 0;
        }
    }

    static boolean compoundHasKey(TagCompound cmp, String key) {
        chkAv0();
        try {
            return (boolean) compoundMethod[5].invoke(cmp.getNMSIns(), key);
        } catch (Throwable tr) {
            return false;
        }
    }

    static Set<String> compoundKeySet(TagCompound cmp) {
        chkAv0();
        try {
            //noinspection unchecked
            return (Set<String>) compoundMethod[6].invoke(cmp.getNMSIns());
        } catch (Throwable tr) {
            return Collections.emptySet();
        }
    }

    static ItemStack wrapCopy(ItemStack stack) {
        if (available) {
            if (classOfOBCItem.isInstance(stack)) {
                return stack;
            } else {
                try {
                    return (ItemStack) craftCopyMethod.invoke(null, stack);
                } catch (Throwable tr) {
                    return stack;
                }
            }
        } else return stack;
    }

    // item methods
    static boolean isItemTagModifiable(Object stack) {
        if (available && classOfOBCItem.isInstance(stack)) {
            try {
                Object nmsObj = fieldOfItemHandle.get(stack);
                if (classOfNMSItem.isInstance(nmsObj)) {
                    Object tag = nmsItemMethod[0].invoke(nmsObj);
                    nmsItemMethod[1].invoke(nmsObj, tag);
                    return true;
                }
            } catch (Throwable ignored) {}
        }
        return false;
    }

    static TagCompound getItemStackTag(Object stack) {
        chkAv0();
        if (classOfOBCItem.isInstance(stack)) {
            try {
                return getItemStackTag(fieldOfItemHandle.get(stack));
            } catch (IllegalAccessException e) {
                return null;
            }
        } else if (classOfNMSItem.isInstance(stack)) {
            try {
                TagBase tag = wrap(nmsItemMethod[0].invoke(stack));
                if (tag instanceof TagCompound) {
                    return (TagCompound) tag;
                } else {
                    return null;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                return null;
            }
        } else return null;
    }

    static void setItemStackTag(Object stack, TagCompound tag) {
        chkAv0();
        if (classOfOBCItem.isInstance(stack)) {
            try {
                setItemStackTag(fieldOfItemHandle.get(stack), tag);
            } catch (IllegalAccessException ignored) {}
        } else if (classOfNMSItem.isInstance(stack)) {
            try {
                nmsItemMethod[1].invoke(stack, tag.getNMSIns());
            } catch (IllegalAccessException | InvocationTargetException ignored) {}
        }
    }

    // entity methods
}
