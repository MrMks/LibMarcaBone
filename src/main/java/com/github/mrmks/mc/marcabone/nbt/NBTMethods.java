package com.github.mrmks.mc.marcabone.nbt;

import org.bukkit.inventory.ItemStack;

import java.util.Set;

interface NBTMethods {
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

    Object listRemove(Object list, int index);

    Object listGet(Object list, int index);

    boolean listIsEmpty(Object list);

    int listSize(Object list);

    // compound methods
    void compoundSet(Object cmp, String k, Object v);

    Object compoundGet(Object cmp, String k);

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

    NBTType testInstance(Object obj);

    boolean testInstance(NBTType type, Object obj);
}
