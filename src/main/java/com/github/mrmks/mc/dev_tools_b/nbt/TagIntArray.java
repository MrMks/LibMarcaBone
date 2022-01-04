package com.github.mrmks.mc.dev_tools_b.nbt;

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
