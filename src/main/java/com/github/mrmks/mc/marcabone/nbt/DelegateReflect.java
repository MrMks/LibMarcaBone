package com.github.mrmks.mc.marcabone.nbt;

import com.github.mrmks.mc.marcabone.sheller.Sheller;
import com.github.mrmks.mc.marcabone.utils.ReflectConstructor;
import com.github.mrmks.mc.marcabone.utils.ReflectFieldGetter;
import com.github.mrmks.mc.marcabone.utils.ReflectMethod;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

class DelegateReflect implements NBTMethods {

    private ConfigurationSection cs;

    DelegateReflect(ConfigurationSection cs) {
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
        return (short) rmNBT[1].tryInvoke(d, (short) 0);
    }

    @Override
    public int getInt(Object d) {
        return (int) rmNBT[2].tryInvoke(d, 0);
    }

    @Override
    public long getLong(Object d) {
        return (long) rmNBT[3].tryInvoke(d, (long) 0);
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
    public Object listRemove(Object list, int index) {
        return rmLst[3].tryInvoke(list, null, index);
    }

    @Override
    public Object listGet(Object list, int index) {
        return rmLst[0].tryInvoke(list, null, index);
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
    public Object compoundGet(Object cmp, String k) {
        return rmCmp[0].tryInvoke(cmp, null, k);
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
                return new TagCompound(obj);
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
    public NBTType testInstance(Object obj) {
        int i = indexOfClass(kNBT, obj.getClass());
        return i < 0 ? null : NBTType.values()[i];
    }

    @Override
    public boolean testInstance(NBTType type, Object obj) {
        int i = indexOfClass(kNBT, obj.getClass());
        return i >= 0 && NBTType.values()[i] == type;
    }

    private int indexOfClass(Class<?>[] ks, Class<?> k) {
        for (int i = 0; i < ks.length; i++) {
            if (ks[i] == k) return i;
        }
        return -1;
    }

}
