package com.github.mrmks.mc.marcabone.nbt;

public final class TagLongArray extends TagBase {
    TagLongArray(Object ins){
        super(ins);
    }
    public TagLongArray(long[] data) {
        super(NBTUtils.newLongArray(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.LONG_ARRAY;
    }

    public long[] getData() {
        return NBTUtils.getLongArrayVal(this);
    }
}
