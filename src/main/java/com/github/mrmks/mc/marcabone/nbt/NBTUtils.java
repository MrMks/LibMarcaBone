package com.github.mrmks.mc.marcabone.nbt;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.Set;

import static com.github.mrmks.mc.marcabone.nbt.NBTType.*;

public class NBTUtils {
    private static boolean available;
    private static NBTMethods methods;
    public static void init(ConfigurationSection cs) {
        available = true;
        NBTMethods[] ms = new NBTMethods[]{new Delegate_1_12_R1(), new DelegateReflect(cs)};
        for (NBTMethods m : ms) {
            if (m.init() && testMethods(m)) {
                methods = m;
                break;
            }
        }
        if (methods == null) available = false;
    }

    private static boolean testMethods(NBTMethods me) {
        try {
            ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);

            stack = me.itemConvert(stack);
            if (!me.itemCanModify(stack)) return false;

            TagCompound itemTag = me.itemGetCompound(stack);
            if (itemTag == null) return false;


            Object tag = me.newCompound();
            if (tag == null) return false;
            me.compoundSet(tag, "B", me.newByte((byte) 1));
            me.compoundSet(tag, "SH", me.newShort((short) 2));
            me.compoundSet(tag, "I", me.newInt(3));
            me.compoundSet(tag, "L", me.newLong(4));
            me.compoundSet(tag, "F", me.newFloat(0.5f));
            me.compoundSet(tag, "D", me.newDouble(0.75));
            me.compoundSet(tag, "ST", me.newString("sSsTtT"));
            me.compoundSet(tag, "BA", me.newByteArray(new byte[]{1}));
            me.compoundSet(tag, "IA", me.newIntArray(new int[]{2}));
            me.compoundSet(tag, "LA", me.newLongArray(new long[]{3}));

            Object lst = me.newList();
            me.listAdd(lst, me.newString("-1"));
            me.listAdd(lst, 0, me.newString("0"));
            me.listAdd(lst, me.newString("1"));

            me.compoundSet(tag, "LST", lst);
            me.compoundSet(itemTag.getNMSIns(), "__C__", tag);
            me.itemSetCompound(stack, itemTag.getNMSIns());

            itemTag = me.itemGetCompound(stack);
            if (itemTag == null) return false;
            if (!me.compoundHasKey(itemTag.getNMSIns(), "__C__")) return false;

            TagBase cTag = testCmpGet(me, itemTag.getNMSIns(), "__C__");
            if (!testTag(cTag, COMPOUND, me)) return false;
            tag = cTag.getNMSIns();

            boolean r;
            r = testTag(cTag = testCmpGet(me, tag, "B"), BYTE, me) && me.getByte(cTag.getNMSIns()) == 1;
            r = r && testTag(cTag = testCmpGet(me, tag, "SH"), SHORT, me) && me.getShort(cTag.getNMSIns()) == 2;
            r = r && testTag(cTag = testCmpGet(me, tag, "I"), INT, me) && me.getInt(cTag.getNMSIns()) == 3;
            r = r && testTag(cTag = testCmpGet(me, tag, "L"), LONG, me) && me.getLong(cTag.getNMSIns()) == 4;
            r = r && testTag(cTag = testCmpGet(me, tag, "F"), FLOAT, me) && me.getFloat(cTag.getNMSIns()) == 0.5f;
            r = r && testTag(cTag = testCmpGet(me, tag, "D"), DOUBLE, me) && me.getDouble(cTag.getNMSIns()) == 0.75;
            r = r && testTag(cTag = testCmpGet(me, tag, "ST"), STRING, me) && "sSsTtT".equals(me.getString(cTag.getNMSIns()));
            r = r && testTag(cTag = testCmpGet(me, tag, "BA"), BYTE_ARRAY, me) && me.getByteArray(cTag.getNMSIns())[0] == 1;
            r = r && testTag(cTag = testCmpGet(me, tag, "IA"), INT_ARRAY, me) && me.getIntArray(cTag.getNMSIns())[0] == 2;
            r = r && testTag(cTag = testCmpGet(me, tag, "LA"), LONG_ARRAY, me) && me.getLongArray(cTag.getNMSIns())[0] == 3;
            if (!r) return false;

            cTag = testCmpGet(me, tag, "LST");
            if (!testTag(cTag, LIST, me)) return false;
            lst = cTag.getNMSIns();

            cTag = wrapIns(me, me.listRemove(lst, 1));
            if (!testTag(cTag, STRING, me) || !"1".equals(me.getString(cTag.getNMSIns()))) return false;
            if (me.listSize(lst) != 1 || me.listIsEmpty(lst)) return false;
            cTag = wrapIns(me, me.listGet(lst, 0));
            if (cTag.getNMSIns() != me.listRemove(lst, 0)) return false;
            if (!testTag(cTag, STRING, me) || !"0".equals(me.getString(cTag.getNMSIns()))) return false;
            if (me.listSize(lst) != 0 || !me.listIsEmpty(lst)) return false;

            if (me.compoundSize(tag) != 11) return false;
            me.compoundRemove(tag, "LST");
            Set<String> ks = me.compoundKeySet(tag);
            if (ks.size() != 10 || ks.size() != me.compoundSize(tag)) return false;
            if (me.compoundIsEmpty(tag)) return false;
            int size = 10;
            Iterator<String> it = ks.iterator();
            while (it.hasNext()) {
                --size;
                it.next();
                it.remove();
                if (size != me.compoundSize(tag)) return false;
            }
            return me.compoundIsEmpty(tag) || size == 0;
        } catch (Throwable tr) {
            return false;
        }
    }

    private static boolean testTag(TagBase tag, NBTType type, NBTMethods me) {
        return tag != null && me.testInstance(type, tag.getNMSIns()) && tag.getType() == type;
    }

    private static TagBase testCmpGet(NBTMethods me, Object cmp, String key) {
        return wrapIns(me, me.compoundGet(cmp, key));
    }

    public static boolean isAvailable() {
        return available;
    }

    private static void chkAv0() {
        if (!available) {
            throw new IllegalStateException("Can't access this method while the NBTUtils#isAvailable() return false.");
        }
    }

    static Object newByte(byte d) {
        chkAv0();
        return methods.newByte(d);
    }

    static Object newShort(short d) {
        chkAv0();
        return methods.newShort(d);
    }

    static Object newInt(int d) {
        chkAv0();
        return methods.newInt(d);
    }

    static Object newLong(long d) {
        chkAv0();
        return methods.newLong(d);
    }

    static Object newFloat(float d) {
        chkAv0();
        return methods.newFloat(d);
    }

    static Object newDouble(double d) {
        chkAv0();
        return methods.newDouble(d);
    }

    static Object newByteArray(byte[] d) {
        chkAv0();
        return methods.newByteArray(d);
    }

    static Object newString(String d) {
        chkAv0();
        return methods.newString(d);
    }

    static Object newList() {
        chkAv0();
        return methods.newList();
    }

    static Object newCompound() {
        chkAv0();
        return methods.newCompound();
    }

    static Object newIntArray(int[] d) {
        chkAv0();
        return methods.newIntArray(d);
    }

    static Object newLongArray(long[] d) {
        chkAv0();
        return methods.newLongArray(d);
    }

    static byte getByteVal(TagByte tag) {
        chkAv0();
        return methods.getByte(tag.getNMSIns());
    }

    static short getShortVal(TagShort tag) {
        chkAv0();
        return methods.getShort(tag.getNMSIns());
    }

    static int getIntVal(TagInt tag) {
        chkAv0();
        return methods.getInt(tag.getNMSIns());
    }

    static long getLongVal(TagLong tag) {
        chkAv0();
        return methods.getLong(tag.getNMSIns());
    }

    static float getFloatVal(TagFloat tag) {
        chkAv0();
        return methods.getFloat(tag.getNMSIns());
    }

    static double getDoubleVal(TagDouble tag) {
        chkAv0();
        return methods.getDouble(tag.getNMSIns());
    }

    static byte[] getByteArrayVal(TagByteArray tag) {
        chkAv0();
        return methods.getByteArray(tag.getNMSIns());
    }

    static int[] getIntArrayVal(TagIntArray tag) {
        chkAv0();
        return methods.getIntArray(tag.getNMSIns());
    }

    static long[] getLongArrayVal(TagLongArray tag) {
        chkAv0();
        return methods.getLongArray(tag.getNMSIns());
    }

    static String getStringVal(TagString tag) {
        chkAv0();
        return methods.getString(tag.getNMSIns());
    }

    // instance test
    static boolean testInstance(NBTType type, Object obj) {
        chkAv0();
        return methods.testInstance(type, obj);
    }

    // list methods
    static void listAdd(TagList tag, TagBase val) {
        chkAv0();
        methods.listAdd(tag.getNMSIns(), val.getNMSIns());
    }

    static void listAdd(TagList tag, int index, TagBase val) {
        chkAv0();
        methods.listAdd(tag.getNMSIns(), index, val.getNMSIns());
    }

    static TagBase listRemove(TagList list, int index) {
        chkAv0();
        return wrapIns(methods, methods.listRemove(list.getNMSIns(), index));
    }

    static TagBase listGet(TagList list, int index) {
        chkAv0();
        return wrapIns(methods, methods.listGet(list.getNMSIns(), index));
    }

    static boolean listIsEmpty(TagList list) {
        chkAv0();
        return methods.listIsEmpty(list.getNMSIns());
    }

    static int listSize(TagList list) {
        chkAv0();
        return methods.listSize(list.getNMSIns());
    }

    // compound methods
    static void compoundSet(TagCompound cmp, String key, TagBase val) {
        chkAv0();
        methods.compoundSet(cmp.getNMSIns(), key, val.getNMSIns());
    }

    static TagBase compoundGet(TagCompound cmp, String key) {
        chkAv0();
        return wrapIns(methods, methods.compoundGet(cmp.getNMSIns(), key));
    }

    static void compoundRemove(TagCompound cmp, String key) {
        chkAv0();
        methods.compoundRemove(cmp.getNMSIns(), key);
    }

    static boolean compoundIsEmpty(TagCompound cmp) {
        chkAv0();
        return methods.compoundIsEmpty(cmp.getNMSIns());
    }

    static int compoundSize(TagCompound cmp) {
        chkAv0();
        return methods.compoundSize(cmp.getNMSIns());
    }

    static boolean compoundHasKey(TagCompound cmp, String key) {
        chkAv0();
        return methods.compoundHasKey(cmp.getNMSIns(), key);
    }

    static Set<String> compoundKeySet(TagCompound cmp) {
        chkAv0();
        return methods.compoundKeySet(cmp.getNMSIns());
    }

    static ItemStack wrapCopy(ItemStack stack) {
        return available ? methods.itemConvert(stack) : stack;
    }

    // item methods
    static boolean isItemTagModifiable(ItemStack stack) {
        return available && methods.itemCanModify(stack);
    }

    static TagCompound getItemStackTag(ItemStack stack) {
        return available ? methods.itemGetCompound(stack) : null;
    }

    static void setItemStackTag(ItemStack stack, TagCompound tag) {
        if (available) methods.itemSetCompound(stack, tag);
    }

    // convert methods
    static Object createEmptyInstance(NBTType type) {
        Object ret;
        switch (type) {
            case COMPOUND:
                ret = newCompound();
                break;
            case LIST:
                ret = newList();
                break;
            case BYTE:
                ret = newByte((byte) 0);
                break;
            case SHORT:
                ret = newShort((short) 0);
                break;
            case INT:
                ret = newInt(0);
                break;
            case LONG:
                ret = newLong(0L);
                break;
            case FLOAT:
                ret = newFloat(0);
                break;
            case DOUBLE:
                ret = newDouble(0.0d);
                break;
            case STRING:
                ret = newString("");
                break;
            case INT_ARRAY:
                ret = newIntArray(new int[0]);
                break;
            case BYTE_ARRAY:
                ret = newByteArray(new byte[0]);
                break;
            case LONG_ARRAY:
                ret = newLongArray(new long[0]);
                break;
            default:
                throw new IllegalStateException("Unrecognized NBT type");
        }

        return ret;
    }

    static TagBase wrapIns(NBTMethods me, Object ins) {
        return generateWrapIns(me.testInstance(ins), ins);
    }

    // generate a empty nbt instance
    static TagBase generateWrapIns(NBTType type, Object ins) {
        switch (type) {
            case BYTE:
                return new TagByte(ins);
            case SHORT:
                return new TagShort(ins);
            case INT:
                return new TagInt(ins);
            case LONG:
                return new TagLong(ins);
            case FLOAT:
                return new TagFloat(ins);
            case DOUBLE:
                return new TagDouble(ins);
            case BYTE_ARRAY:
                return new TagByteArray(ins);
            case STRING:
                return new TagString(ins);
            case INT_ARRAY:
                return new TagIntArray(ins);
            case LONG_ARRAY:
                return new TagLongArray(ins);
            case LIST:
                return new TagList();
            case COMPOUND:
                return new TagCompound();
            case END:
            default:
                return new TagEnd(ins);
        }
    }
}
