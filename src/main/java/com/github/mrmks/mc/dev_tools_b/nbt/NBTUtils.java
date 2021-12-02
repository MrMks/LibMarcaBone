package com.github.mrmks.mc.dev_tools_b.nbt;

import com.github.mrmks.mc.dev_tools_b.utils.ReflectConstructor;
import com.github.mrmks.mc.dev_tools_b.utils.ReflectFieldGetter;
import com.github.mrmks.mc.dev_tools_b.utils.ReflectMethod;
import com.github.mrmks.mc.dev_tools_b.utils.ReflectStaticMethod;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static com.github.mrmks.mc.dev_tools_b.utils.ReflectUtils.loadNMSClass;
import static com.github.mrmks.mc.dev_tools_b.utils.ReflectUtils.loadOBCClass;

public class NBTUtils {
    private static boolean available;

    private static EnumMap<EnumTagType, Class<?>> classEnumMap;
    private static EnumMap<EnumTagType, ReflectConstructor> constructorEnumMap;
    private static EnumMap<EnumTagType, ReflectMethod> methodEnumMap;
    private static HashMap<Class<?>, EnumTagType> reClassMap;
    private static ReflectFieldGetter fieldOfLongArrayTag;

    private static ReflectMethod[] listMethod;
    private static ReflectMethod[] compoundMethod;

    private static Class<?> classOfOBCItem;
    private static Class<?> classOfNMSItem;
    private static ReflectFieldGetter fieldOfItemHandle;
    private static ReflectMethod[] nmsItemMethod;

    private static ReflectStaticMethod craftCopyMethod;

    public static void init() {

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
                    ReflectConstructor c = type.hasConstructorParams() ? loadClassConstructor(klass, type.getConstructorParamTypes()) : loadClassConstructor(klass);
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
                    ReflectMethod rm = new ReflectMethod(k, type.getGetterName(), type.getGetterType());
                    available = rm.isValid();
                    if (available) methodEnumMap.put(type, rm);
                } else {
                    if (type == EnumTagType.LONG_ARRAY) {
                        ReflectFieldGetter rfg = new ReflectFieldGetter(k, "b", long[].class);
                        available = rfg.isValid();
                        if (available) fieldOfLongArrayTag = rfg;
                    }
                }
            }
        }

        if (available) {
            Class<?> k = classEnumMap.get(EnumTagType.LIST);
            listMethod = new ReflectMethod[]{
                    new ReflectMethod(k, "add", void.class, nbt_base_klass),                //add
                    new ReflectMethod(k, "a", void.class, int.class, nbt_base_klass),       //add
                    new ReflectMethod(k, "i", nbt_base_klass, int.class),                   //get
                    new ReflectMethod(k, "remove", nbt_base_klass, int.class),              //remove
                    new ReflectMethod(k, "isEmpty", boolean.class),                         //isEmpty
                    new ReflectMethod(k, "size", int.class)                                 //size
            };
            for (ReflectMethod rm : listMethod)
                if (!rm.isValid()) {
                    available = false;
                    break;
                }
        }

        if (available) {
            Class<?> k = classEnumMap.get(EnumTagType.COMPOUND);
            compoundMethod = new ReflectMethod[]{
                    new ReflectMethod(k, "set", void.class, String.class, nbt_base_klass),      //set
                    new ReflectMethod(k, "get", nbt_base_klass, String.class),                  //get
                    new ReflectMethod(k, "remove", void.class, String.class),                   //remove
                    new ReflectMethod(k, "isEmpty", boolean.class),                             //isEmpty
                    new ReflectMethod(k, "d", int.class),                                       //size
                    new ReflectMethod(k, "hasKey", boolean.class, String.class),                //hasKey
                    new ReflectMethod(k, "c", Set.class)                                        //keySet
            };
            for (ReflectMethod rm : compoundMethod)
                if (!rm.isValid()) {
                    available = false;
                    break;
                }
        }

        if (available) {
            classOfOBCItem = loadOBCClass("inventory.CraftItemStack");
            classOfNMSItem = loadNMSClass("ItemStack");
            if (classOfNMSItem != null && classOfOBCItem != null) {
                ReflectFieldGetter rfg = new ReflectFieldGetter(classOfOBCItem, "handle", classOfNMSItem);
                available = rfg.isValid();
                if (available) {
                    fieldOfItemHandle = rfg;
                    Class<?> tagK = classEnumMap.get(EnumTagType.COMPOUND);
                    nmsItemMethod = new ReflectMethod[] {
                            new ReflectMethod(classOfNMSItem, "getTag", tagK),                  // getTag
                            new ReflectMethod(classOfNMSItem, "setTag", void.class, tagK)       // setTag
                    };
                    for (ReflectMethod rm : nmsItemMethod) if (!rm.isValid()) {available = false;break;}
                }
            } else available = false;
        }

        if (available) {
            ReflectStaticMethod rsm = new ReflectStaticMethod(classOfOBCItem, "asCraftCopy", classOfOBCItem, ItemStack.class);
            available = rsm.isValid();
            if (available) craftCopyMethod = rsm;
        }

        if (!available) {
            craftCopyMethod = null;
            classOfOBCItem = null;
            classOfNMSItem = null;
            fieldOfItemHandle = null;
            fieldOfLongArrayTag = null;
            if (classEnumMap != null) {
                classEnumMap.clear();
                classEnumMap = null;
            }
            if (reClassMap != null) {
                reClassMap.clear();
                reClassMap = null;
            }
            if (constructorEnumMap != null) {
                constructorEnumMap.clear();
                constructorEnumMap = null;
            }
            if (methodEnumMap != null) {
                methodEnumMap.clear();
                methodEnumMap = null;
            }
            if (listMethod != null) {
                Arrays.fill(listMethod, null);
                listMethod = null;
            }
            if (compoundMethod != null) {
                Arrays.fill(compoundMethod, null);
                compoundMethod = null;
            }
            if (nmsItemMethod != null) {
                Arrays.fill(nmsItemMethod, null);
                nmsItemMethod = null;
            }
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

    private static ReflectConstructor loadClassConstructor(Class<?> k, Class<?>... classes) {
        ReflectConstructor rc = new ReflectConstructor(k, classes);
        return rc.isValid() ? rc : null;
    }

    // new instance
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
        ReflectConstructor c = constructorEnumMap.get(type);
        try {
            return type.hasConstructorParams() ? c.invoke(data) : c.invoke();
        } catch (Throwable tr) {
            // this should never happen since the reflect method has been tested in static code block;
            // when this happen, NBTUtils will stop itself;
            // available = false;
        }
        return null;
    }

    // get
    private static Object getTagValue(EnumTagType type, TagBase tag, Object def) {
        if (!type.getGetterName().isEmpty()) {
            try {
                return methodEnumMap.get(type).invoke(tag.getNMSIns());
            } catch (Throwable e) {
                return def;
            }
        } else {
            if (type == EnumTagType.LONG_ARRAY) {
                try {
                    return fieldOfLongArrayTag.invoke(tag.getNMSIns());
                } catch (Throwable e) {
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
                return new TagList();
            case COMPOUND:
                return new TagCompound();
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
                    return (ItemStack) craftCopyMethod.invoke(stack);
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
                Object nmsObj = fieldOfItemHandle.invoke(stack);
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
                return getItemStackTag(fieldOfItemHandle.invoke(stack));
            } catch (Throwable e) {
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
            } catch (Throwable e) {
                return null;
            }
        } else return null;
    }

    static void setItemStackTag(Object stack, TagCompound tag) {
        chkAv0();
        if (classOfOBCItem.isInstance(stack)) {
            try {
                setItemStackTag(fieldOfItemHandle.invoke(stack), tag);
            } catch (Throwable ignored) {}
        } else if (classOfNMSItem.isInstance(stack)) {
            try {
                nmsItemMethod[1].invoke(stack, tag.getNMSIns());
            } catch (Throwable ignored) {}
        }
    }

    // entity methods
}
