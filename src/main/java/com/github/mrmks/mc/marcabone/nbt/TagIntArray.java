package com.github.mrmks.mc.marcabone.nbt;

public final class TagIntArray extends TagBase {
    TagIntArray(Object ins){
        super(ins);
    }
    public TagIntArray(int[] data) {
        super(NBTUtils.newIntArray(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.INT_ARRAY;
    }

    public int[] getData() {
        return NBTUtils.getIntArrayVal(this);
    }
}
