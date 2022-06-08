package com.github.mrmks.mc.marcabone.nbt;

public final class TagIntArray extends TagBase {
    TagIntArray(){}
    public TagIntArray(int[] data) {
        wrapNMSIns(NBTUtils.newIntArray(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.INT_ARRAY;
    }

    public int[] getData() {
        return NBTUtils.getIntArrayVal(this);
    }
}
