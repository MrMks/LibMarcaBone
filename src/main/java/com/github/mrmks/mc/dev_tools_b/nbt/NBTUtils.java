package com.github.mrmks.mc.dev_tools_b.nbt;

import com.github.mrmks.mc.dev_tools_b.utils.ReflectConstructor;
import com.github.mrmks.mc.dev_tools_b.utils.ReflectFieldGetter;
import com.github.mrmks.mc.dev_tools_b.utils.ReflectMethod;
import com.github.mrmks.mc.dev_tools_b.sheller.Sheller;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import static com.github.mrmks.mc.dev_tools_b.nbt.NBTType.*;

public class NBTUtils {
    private static boolean available;
    private static NBTMethods methods;
    public static void init(ConfigurationSection cs) {
        available = true;
        NBTMethods[] ms = new NBTMethods[]{/*new DirectCall(), */new ReflectCall(cs)};
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

            TagBase cTag = me.compoundGet(itemTag.getNMSIns(), "__C__");
            if (!testTag(cTag, COMPOUND, me)) return false;
            tag = cTag.getNMSIns();

            boolean r;
            r = testTag(cTag = me.compoundGet(tag, "B"), BYTE, me) && me.getByte(cTag.getNMSIns()) == 1;
            r = r && testTag(cTag = me.compoundGet(tag, "SH"), SHORT, me) && me.getShort(cTag.getNMSIns()) == 2;
            r = r && testTag(cTag = me.compoundGet(tag, "I"), INT, me) && me.getInt(cTag.getNMSIns()) == 3;
            r = r && testTag(cTag = me.compoundGet(tag, "L"), LONG, me) && me.getLong(cTag.getNMSIns()) == 4;
            r = r && testTag(cTag = me.compoundGet(tag, "F"), FLOAT, me) && me.getFloat(cTag.getNMSIns()) == 0.5f;
            r = r && testTag(cTag = me.compoundGet(tag, "D"), DOUBLE, me) && me.getDouble(cTag.getNMSIns()) == 0.75;
            r = r && testTag(cTag = me.compoundGet(tag, "ST"), STRING, me) && "sSsTtT".equals(me.getString(cTag.getNMSIns()));
            r = r && testTag(cTag = me.compoundGet(tag, "BA"), BYTE_ARRAY, me) && me.getByteArray(cTag.getNMSIns())[0] == 1;
            r = r && testTag(cTag = me.compoundGet(tag, "IA"), INT_ARRAY, me) && me.getIntArray(cTag.getNMSIns())[0] == 2;
            r = r && testTag(cTag = me.compoundGet(tag, "LA"), LONG_ARRAY, me) && me.getLongArray(cTag.getNMSIns())[0] == 3;
            if (!r) return false;

            cTag = me.compoundGet(tag, "LST");
            if (!testTag(cTag, LIST, me)) return false;
            lst = cTag.getNMSIns();

            cTag = me.listRemove(lst, 1);
            if (!testTag(cTag, STRING, me) || !"1".equals(me.getString(cTag.getNMSIns()))) return false;
            if (me.listSize(lst) != 1 || me.listIsEmpty(lst)) return false;
            cTag = me.listGet(lst, 0);
            if (cTag.getNMSIns() != me.listRemove(lst, 0).getNMSIns()) return false;
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

    // generate a empty nbt instance
    private static TagBase generateWrapIns(NBTType type) {
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
        methods.listAdd(tag.getNMSIns(), val.getNMSIns());
    }

    static void listAdd(TagList tag, int index, TagBase val) {
        chkAv0();
        methods.listAdd(tag.getNMSIns(), index, val.getNMSIns());
    }

    static TagBase listRemove(TagList list, int index) {
        chkAv0();
        return methods.listRemove(list.getNMSIns(), index);
    }

    static TagBase listGet(TagList list, int index) {
        chkAv0();
        return methods.listGet(list.getNMSIns(), index);
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
        return methods.compoundGet(cmp.getNMSIns(), key);
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
    static boolean isItemTagModifiable(Object stack) {
        return available && methods.itemCanModify(stack);
    }

    static TagCompound getItemStackTag(Object stack) {
        return available ? methods.itemGetCompound(stack) : null;
    }

    static void setItemStackTag(Object stack, TagCompound tag) {
        if (available) methods.itemSetCompound(stack, tag);
    }

    private interface NBTMethods {
        boolean init();
        // methods shell from Tag instances, to generate NBTTag instances.
        Object newByte(byte d);
        Object newShort(short d);
        Object newInt(int d);
        Object newLong(long d);
        Object newFloat(float d);
        Object newDouble(double d);
        Object newString(String d);
        Object newByteArray(byte[] d);
        Object newIntArray(int[] d);
        Object newLongArray(long[] d);
        Object newList();
        Object newCompound();

        // methods shell from tag instances, to get data in NBTTag instances.
        byte getByte(Object d);
        short getShort(Object d);
        int getInt(Object d);
        long getLong(Object d);
        float getFloat(Object d);
        double getDouble(Object d);
        String getString(Object d);
        byte[] getByteArray(Object d);
        int[] getIntArray(Object d);
        long[] getLongArray(Object d);

        // list methods
        void listAdd(Object list, Object tag);
        void listAdd(Object list, int index, Object tag);
        TagBase listRemove(Object list, int index);
        TagBase listGet(Object list, int index);
        boolean listIsEmpty(Object list);
        int listSize(Object list);

        // compound methods
        void compoundSet(Object cmp, String k, Object v);
        TagBase compoundGet(Object cmp, String k);
        void compoundRemove(Object cmp, String k);
        boolean compoundIsEmpty(Object cmp);
        int compoundSize(Object cmp);
        boolean compoundHasKey(Object cmp, String k);
        Set<String> compoundKeySet(Object cmp);

        boolean itemCanModify(Object stack);
        TagCompound itemGetCompound(Object stack);
        void itemSetCompound(Object stack, Object tag);
        boolean itemHasCompound(Object stack);
        ItemStack itemConvert(ItemStack stack);

        boolean testInstance(NBTType type, Object obj);
    }

    private static class DirectCall implements NBTMethods {

        @Override
        public boolean init() {
            return true;
        }

        @Override
        public Object newByte(byte d) {
            return new NBTTagByte(d);
        }

        @Override
        public Object newShort(short d) {
            return new NBTTagShort(d);
        }

        @Override
        public Object newInt(int d) {
            return new NBTTagInt(d);
        }

        @Override
        public Object newLong(long d) {
            return new NBTTagLong(d);
        }

        @Override
        public Object newFloat(float d) {
            return new NBTTagFloat(d);
        }

        @Override
        public Object newDouble(double d) {
            return new NBTTagDouble(d);
        }

        @Override
        public Object newString(String d) {
            return new NBTTagString(d);
        }

        @Override
        public Object newByteArray(byte[] d) {
            return new NBTTagByteArray(d);
        }

        @Override
        public Object newIntArray(int[] d) {
            return new NBTTagIntArray(d);
        }

        @Override
        public Object newLongArray(long[] d) {
            return new NBTTagLongArray(d);
        }

        @Override
        public Object newList() {
            return new NBTTagList();
        }

        @Override
        public Object newCompound() {
            return new NBTTagCompound();
        }

        @Override
        public byte getByte(Object d) {
            if (d instanceof NBTTagByte) {
                return ((NBTTagByte) d).g();
            }
            return 0;
        }

        @Override
        public short getShort(Object d) {
            if (d instanceof NBTTagShort) return ((NBTTagShort) d).f();
            return 0;
        }

        @Override
        public int getInt(Object d) {
            if (d instanceof NBTTagInt) return ((NBTTagInt)d).e();
            return 0;
        }

        @Override
        public long getLong(Object d) {
            if (d instanceof NBTTagLong) return ((NBTTagLong)d).d();
            return 0;
        }

        @Override
        public float getFloat(Object d) {
            if (d instanceof NBTTagFloat) return ((NBTTagFloat)d).i();
            return 0;
        }

        @Override
        public double getDouble(Object d) {
            if (d instanceof NBTTagDouble) return ((NBTTagDouble)d).asDouble();
            return 0;
        }

        @Override
        public String getString(Object d) {
            if (d instanceof NBTTagString) return ((NBTTagString)d).c_();
            return "";
        }

        @Override
        public byte[] getByteArray(Object d) {
            if (d instanceof NBTTagByteArray) return ((NBTTagByteArray)d).c();
            return new byte[0];
        }

        @Override
        public int[] getIntArray(Object d) {
            if (d instanceof NBTTagIntArray) return ((NBTTagIntArray)d).d();
            return new int[0];
        }

        private Field longArrayField;
        private boolean longArrayFieldFlag = false;
        @Override
        public long[] getLongArray(Object d) {
            if (d instanceof NBTTagLongArray) {
                if (longArrayField == null && !longArrayFieldFlag) {
                    try {
                        longArrayField = NBTTagLongArray.class.getDeclaredField("b");
                        longArrayField.setAccessible(true);
                    } catch (NoSuchFieldException e) {
                        // ignore
                    }
                    longArrayFieldFlag = true;
                }
                if (longArrayField != null) {
                    try {
                        return (long[]) longArrayField.get(d);
                    } catch (IllegalAccessException e) {
                        //ignore
                    }
                }
            }
            return new long[0];
        }

        @Override
        public void listAdd(Object list, Object tag) {
            if (list instanceof NBTTagList && tag instanceof NBTBase) {
                ((NBTTagList)list).add((NBTBase) tag);
            }
        }

        @Override
        public void listAdd(Object list, int index, Object tag) {
            if (list instanceof NBTTagList && tag instanceof NBTBase) {
                ((NBTTagList)list).a(index, (NBTBase) tag);
            }
        }

        @Override
        public TagBase listRemove(Object list, int index) {
            if (list instanceof NBTTagList) {
                return wrap(((NBTTagList)list).remove(index));
            }
            return null;
        }

        @Override
        public TagBase listGet(Object list, int index) {
            if (list instanceof NBTTagList) {
                return wrap(((NBTTagList) list).i(index));
            }
            return null;
        }

        @Override
        public boolean listIsEmpty(Object list) {
            if (list instanceof NBTTagList) {
                return ((NBTTagList)list).isEmpty();
            }
            return false;
        }

        @Override
        public int listSize(Object list) {
            if (list instanceof NBTTagList) {
                return ((NBTTagList)list).size();
            }
            return 0;
        }

        @Override
        public void compoundSet(Object cmp, String k, Object v) {
            if (k != null && !k.isEmpty() && cmp instanceof NBTTagCompound && v instanceof NBTBase) {
                ((NBTTagCompound)cmp).set(k, (NBTBase) v);
            }
        }

        @Override
        public TagBase compoundGet(Object cmp, String k) {
            if (k != null && !k.isEmpty() && cmp instanceof NBTTagCompound) {
                return wrap(((NBTTagCompound) cmp).get(k));
            }
            return null;
        }

        @Override
        public void compoundRemove(Object cmp, String k) {
            if (k != null && !k.isEmpty() && cmp instanceof NBTTagCompound) {
                ((NBTTagCompound) cmp).remove(k);
            }
        }

        @Override
        public boolean compoundIsEmpty(Object cmp) {
            if (cmp instanceof NBTTagCompound) {
                return ((NBTTagCompound) cmp).isEmpty();
            }
            return false;
        }

        @Override
        public int compoundSize(Object cmp) {
            if (cmp instanceof NBTTagCompound) {
                return ((NBTTagCompound) cmp).d();
            }
            return 0;
        }

        @Override
        public boolean compoundHasKey(Object cmp, String k) {
            if (cmp instanceof NBTTagCompound && k != null && !k.isEmpty()) {
                return ((NBTTagCompound) cmp).hasKey(k);
            }
            return false;
        }

        @Override
        public Set<String> compoundKeySet(Object cmp) {
            if (cmp instanceof NBTTagCompound) {
                return ((NBTTagCompound) cmp).c();
            }
            return Collections.emptySet();
        }

        @Override
        public boolean itemCanModify(Object stack) {
            return stack instanceof CraftItemStack || stack instanceof net.minecraft.server.v1_12_R1.ItemStack;
        }

        private Field itemHandleField = null;
        private boolean itemHandleFieldFlag = false;
        private net.minecraft.server.v1_12_R1.ItemStack reflectGetItemHandle(CraftItemStack stack) {
            if (!itemHandleFieldFlag) {
                try {
                    itemHandleField = CraftItemStack.class.getDeclaredField("handle");
                    itemHandleField.setAccessible(true);
                } catch (NoSuchFieldException e) {
                    // ignore
                }
                itemHandleFieldFlag = true;
            }
            if (itemHandleField == null) return null;
            try {
                return (net.minecraft.server.v1_12_R1.ItemStack) itemHandleField.get(stack);
            } catch (IllegalAccessException e) {
                return null;
            }
        }

        @Override
        public TagCompound itemGetCompound(Object stack) {
            net.minecraft.server.v1_12_R1.ItemStack nmsStack = null;
            if (stack instanceof CraftItemStack) {
                nmsStack = reflectGetItemHandle((CraftItemStack) stack);
            } else if (stack instanceof net.minecraft.server.v1_12_R1.ItemStack) {
                nmsStack = (net.minecraft.server.v1_12_R1.ItemStack) stack;
            }
            if (nmsStack != null) {
                boolean f = nmsStack.getTag() == null;
                if (f) nmsStack.setTag(new NBTTagCompound());
                if (nmsStack.hasTag()) {
                    TagCompound rt = new TagCompound();
                    rt.wrapNMSIns(nmsStack.getTag());
                    return rt;
                } else {
                    if (f) nmsStack.setTag(null);
                }
            }
            return null;
        }

        @Override
        public void itemSetCompound(Object stack, Object tag) {
            if (tag instanceof NBTTagCompound) {
                net.minecraft.server.v1_12_R1.ItemStack nmsStack = null;
                if (stack instanceof CraftItemStack) {
                    nmsStack = reflectGetItemHandle((CraftItemStack) stack);
                } else if (stack instanceof net.minecraft.server.v1_12_R1.ItemStack) {
                    nmsStack = (net.minecraft.server.v1_12_R1.ItemStack) stack;
                }
                if (nmsStack != null) {
                    nmsStack.setTag((NBTTagCompound) tag);
                }
            }
        }

        @Override
        public boolean itemHasCompound(Object stack) {
            if (stack instanceof net.minecraft.server.v1_12_R1.ItemStack) {
                return ((net.minecraft.server.v1_12_R1.ItemStack) stack).hasTag();
            }
            return false;
        }

        @Override
        public ItemStack itemConvert(ItemStack stack) {
            return stack instanceof CraftItemStack ? stack : CraftItemStack.asCraftCopy(stack);
        }

        @Override
        public boolean testInstance(NBTType tag, Object obj) {
            return obj instanceof NBTBase && tag.ordinal() == ((NBTBase) obj).getTypeId();
        }

        private TagBase wrap(NBTBase obj) {
            TagBase base = generateWrapIns(NBTType.values()[obj.getTypeId()]);
            base.wrapNMSIns(obj);
            return base;
        }
    }

    private static class ReflectCall implements NBTMethods {

        private ConfigurationSection cs;
        ReflectCall(ConfigurationSection cs) {
            this.cs = cs;
        }

        private String[] order = new String[]{"end", "byte", "short", "int", "long", "float", "double", "byte_array", "string", "list", "compound", "int_array", "long_array"};
        private Class<?>[] types = new Class<?>[]{byte.class, short.class, int.class, long.class, float.class, double.class, byte[].class, String.class, int[].class, long[].class};
        private Class<?>[] kNBT = new Class<?>[order.length + 1];
        private ReflectConstructor[] rcNBT = new ReflectConstructor[order.length];
        private ReflectMethod[] rmNBT = new ReflectMethod[order.length - 4];
        private ReflectFieldGetter rfNBT;

        private ReflectMethod[] rmLst = new ReflectMethod[6];
        private ReflectMethod[] rmCmp = new ReflectMethod[7];
        private ReflectMethod[] rmNMS = new ReflectMethod[3];

        private Class<?> kOBC, kNMS;
        private Sheller bkTr, nmsTr;
        private final Object[] tb1 = new Object[1];

        @Override
        public boolean init() {
            boolean r = true;
            try {
                kNBT[order.length] = Class.forName(cs.getString("base"));
                for (int i = 0; i < order.length; i++) {
                    String t = i == 9 || i == 10 ? cs.getString(order[i].concat(".klass")) : cs.getString(order[i]);
                    int sharp = t.lastIndexOf('#'), ii = i > 8 ? i - 3 : i - 1;
                    kNBT[i] = Class.forName(sharp == -1 ? t : t.substring(0, sharp));
                    if (i == 0 || i == 9 || i == 10) {
                        rcNBT[i] = new ReflectConstructor(kNBT[i]);
                    } else {
                        rcNBT[i] = new ReflectConstructor(kNBT[i], types[ii]);
                    }
                    if (sharp != -1) {
                        if (i == order.length - 1) {
                            rfNBT = new ReflectFieldGetter(kNBT[i], t.substring(sharp + 1), types[ii]);
                        } else {
                            rmNBT[ii] = new ReflectMethod(kNBT[i], t.substring(sharp + 1), types[ii]);
                        }
                    }
                }
                for (ReflectConstructor rc : rcNBT) r = r && rc.isValid();
                for (ReflectMethod rm : rmNBT) r = r && rm.isValid();
                r = r && rfNBT.isValid();
            } catch (Exception e) {
                r = false;
            }
            if (r) {
                ConfigurationSection csc = cs.getConfigurationSection(order[9]);
                Class<?> kB = kNBT[order.length];
                rmLst[0] = new ReflectMethod(kNBT[9], csc.getString("get"), kB, int.class);
                rmLst[1] = new ReflectMethod(kNBT[9], csc.getString("set"), void.class, int.class, kB);
                rmLst[2] = new ReflectMethod(kNBT[9], csc.getString("add"), void.class, kB);
                rmLst[3] = new ReflectMethod(kNBT[9], csc.getString("remove"), kB, int.class);
                rmLst[4] = new ReflectMethod(kNBT[9], csc.getString("size"), int.class);
                rmLst[5] = new ReflectMethod(kNBT[9], csc.getString("isEmpty"), boolean.class);

                csc = cs.getConfigurationSection(order[10]);
                rmCmp[0] = new ReflectMethod(kNBT[10], csc.getString("get"), kB, String.class);
                rmCmp[1] = new ReflectMethod(kNBT[10], csc.getString("set"), void.class, String.class, kB);
                rmCmp[2] = new ReflectMethod(kNBT[10], csc.getString("remove"), void.class, String.class);
                rmCmp[3] = new ReflectMethod(kNBT[10], csc.getString("hasKey"), boolean.class, String.class);
                rmCmp[4] = new ReflectMethod(kNBT[10], csc.getString("size"), int.class);
                rmCmp[5] = new ReflectMethod(kNBT[10], csc.getString("isEmpty"), boolean.class);
                rmCmp[6] = new ReflectMethod(kNBT[10], csc.getString("keySet"), Set.class);

                for (ReflectMethod rm : rmLst) r = r && rm.isValid();
                for (ReflectMethod rm : rmCmp) r = r && rm.isValid();
            }
            if (r) try {
                ConfigurationSection scs = cs.getConfigurationSection("bukkitItemStackTransform");
                kOBC = Class.forName(scs.getString("target"));
                bkTr = new Sheller(scs.getStringList("step"));

                scs = cs.getConfigurationSection("nmsItemStackTransform");
                kNMS = Class.forName(scs.getString("target"));
                nmsTr = new Sheller(scs.getStringList("step"));
                rmNMS[0] = new ReflectMethod(kNMS, scs.getString("get"), kNBT[10]);
                rmNMS[1] = new ReflectMethod(kNMS, scs.getString("set"), void.class, kNBT[10]);
                rmNMS[2] = new ReflectMethod(kNMS, scs.getString("has"), boolean.class);
                for (ReflectMethod rm : rmNMS) r = r && rm.isValid();
            } catch (Exception e) {
                r = false;
            }
            if (!r) {
                Arrays.fill(kNBT, null);
                Arrays.fill(rcNBT, null);
                Arrays.fill(rmNBT, null);
                Arrays.fill(rmLst, null);
                Arrays.fill(rmCmp, null);
                Arrays.fill(rmNMS, null);
                kNBT = null;
                rcNBT = null;
                rmNBT = null;
                rmLst = null;
                rmCmp = null;
                rfNBT = null;
                rmNMS = null;
            }

            Arrays.fill(order, null);
            Arrays.fill(types, null);
            cs = null;
            order = null;
            types = null;

            return r;
        }

        @Override
        public Object newByte(byte d) {
            return rcNBT[1].tryInvoke(null, d);
        }

        @Override
        public Object newShort(short d) {
            return rcNBT[2].tryInvoke(null, d);
        }

        @Override
        public Object newInt(int d) {
            return rcNBT[3].tryInvoke(null, d);
        }

        @Override
        public Object newLong(long d) {
            return rcNBT[4].tryInvoke(null, d);
        }

        @Override
        public Object newFloat(float d) {
            return rcNBT[5].tryInvoke(null, d);
        }

        @Override
        public Object newDouble(double d) {
            return rcNBT[6].tryInvoke(null, d);
        }

        @Override
        public Object newString(String d) {
            return rcNBT[8].tryInvoke(null, d);
        }

        @Override
        public Object newByteArray(byte[] d) {
            return rcNBT[7].tryInvoke(null, (Object) d);
        }

        @Override
        public Object newIntArray(int[] d) {
            return rcNBT[11].tryInvoke(null, (Object) d);
        }

        @Override
        public Object newLongArray(long[] d) {
            return rcNBT[12].tryInvoke(null, (Object) d);
        }

        @Override
        public Object newList() {
            return rcNBT[9].tryInvoke(null);
        }

        @Override
        public Object newCompound() {
            return rcNBT[10].tryInvoke(null);
        }

        @Override
        public byte getByte(Object d) {
            return (byte) rmNBT[0].tryInvoke(d, (byte) 0);
        }

        @Override
        public short getShort(Object d) {
            return (short) rmNBT[1].tryInvoke(d, (short)0);
        }

        @Override
        public int getInt(Object d) {
            return (int) rmNBT[2].tryInvoke(d, 0);
        }

        @Override
        public long getLong(Object d) {
            return (long) rmNBT[3].tryInvoke(d, (long)0);
        }

        @Override
        public float getFloat(Object d) {
            return (float) rmNBT[4].tryInvoke(d, 0.0f);
        }

        @Override
        public double getDouble(Object d) {
            return (double) rmNBT[5].tryInvoke(d, 0.0);
        }

        @Override
        public String getString(Object d) {
            return (String) rmNBT[7].tryInvoke(d, "");
        }

        @Override
        public byte[] getByteArray(Object d) {
            return (byte[]) rmNBT[6].tryInvoke(d, new byte[0]);
        }

        @Override
        public int[] getIntArray(Object d) {
            return (int[]) rmNBT[8].tryInvoke(d, new int[0]);
        }

        @Override
        public long[] getLongArray(Object d) {
            return (long[]) rfNBT.tryInvoke(d, new long[0]);
        }

        @Override
        public void listAdd(Object list, Object tag) {
            rmLst[2].tryInvoke(list, null, tag);
        }

        @Override
        public void listAdd(Object list, int index, Object tag) {
            rmLst[1].tryInvoke(list, null, index, tag);
        }

        @Override
        public TagBase listRemove(Object list, int index) {
            return wrap(rmLst[3].tryInvoke(list, null, index));
        }

        @Override
        public TagBase listGet(Object list, int index) {
            return wrap(rmLst[0].tryInvoke(list, null, index));
        }

        @Override
        public boolean listIsEmpty(Object list) {
            return (boolean) rmLst[5].tryInvoke(list, false);
        }

        @Override
        public int listSize(Object list) {
            return (int) rmLst[4].tryInvoke(list, 0);
        }

        @Override
        public void compoundSet(Object cmp, String k, Object v) {
            rmCmp[1].tryInvoke(cmp, null, k, v);
        }

        @Override
        public TagBase compoundGet(Object cmp, String k) {
            return wrap(rmCmp[0].tryInvoke(cmp, null, k));
        }

        @Override
        public void compoundRemove(Object cmp, String k) {
            rmCmp[2].tryInvoke(cmp, null, k);
        }

        @Override
        public boolean compoundIsEmpty(Object cmp) {
            return (boolean) rmCmp[5].tryInvoke(cmp, false);
        }

        @Override
        public int compoundSize(Object cmp) {
            return (int) rmCmp[4].tryInvoke(cmp, 0);
        }

        @Override
        public boolean compoundHasKey(Object cmp, String k) {
            return (boolean) rmCmp[3].tryInvoke(cmp, false, k);
        }

        @Override
        public Set<String> compoundKeySet(Object cmp) {
            //noinspection unchecked
            return (Set<String>) rmCmp[6].tryInvoke(cmp, Collections.emptySet());
        }

        @Override
        public boolean itemCanModify(Object stack) {
            return kOBC.isInstance(stack);
        }

        private Object obc2nms(Object s) {
            tb1[0] = s;
            s = nmsTr.call(tb1);
            tb1[0] = null;
            return s;
        }

        @Override
        public TagCompound itemGetCompound(Object stack) {
            stack = obc2nms(stack);
            if (kNMS.isInstance(stack)) {
                Object obj = rmNMS[0].tryInvoke(stack, null);
                boolean f = obj == null;
                if (f) {
                    obj = newCompound();
                    rmNMS[1].tryInvoke(stack, null, obj);
                }
                if ((boolean) rmNMS[2].tryInvoke(stack, false)) {
                    TagCompound rt = new TagCompound();
                    rt.wrapNMSIns(obj);
                    return rt;
                } else {
                    if (f) rmNMS[1].tryInvoke(stack, null, (Object) null);
                }
            }
            return null;
        }

        @Override
        public void itemSetCompound(Object stack, Object tag) {
            stack = obc2nms(stack);
            if (kNMS.isInstance(stack) && kNBT[10].isInstance(tag)) {
                rmNMS[1].tryInvoke(stack, null, tag);
            }
        }

        @Override
        public boolean itemHasCompound(Object stack) {
            stack = obc2nms(stack);
            if (kNMS.isInstance(stack)) {
                return (boolean) rmNMS[2].tryInvoke(stack, false);
            }
            return false;
        }

        @Override
        public ItemStack itemConvert(ItemStack stack) {
            if (kOBC.isInstance(stack)) {
                return stack;
            } else {
                tb1[0] = stack;
                Object obj = bkTr.call(tb1);
                tb1[0] = null;
                if (kOBC.isInstance(obj) && obj instanceof ItemStack) {
                    return (ItemStack) obj;
                } else {
                    return stack;
                }
            }
        }

        @Override
        public boolean testInstance(NBTType type, Object obj) {
            int i = indexOfClass(kNBT, obj.getClass());
            return i >= 0 && NBTType.values()[i] == type;
        }

        private TagBase wrap(Object obj) {
            int i = indexOfClass(kNBT, obj.getClass());
            if (i < 0) return null;
            TagBase rt = generateWrapIns(NBTType.values()[i]);
            rt.wrapNMSIns(obj);
            return rt;
        }

        private int indexOfClass(Class<?>[] ks, Class<?> k) {
            for (int i = 0; i < ks.length; i++) {
                if (ks[i] == k) return i;
            }
            return -1;
        }

    }

    // entity methods
}
