package com.github.mrmks.mc.marcabone.nbt;

public final class TagLongArray extends TagBase {
    TagLongArray(){}
    public TagLongArray(long[] data) {
        wrapNMSIns(NBTUtils.newLongArray(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.LONG_ARRAY;
    }

    public long[] getData() {
        return NBTUtils.getLongArrayVal(this);
    }
}
