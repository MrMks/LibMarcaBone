package com.github.mrmks.mc.dev_tools_b.nbt;

public class TagIntArray extends TagValued {
    TagIntArray(){}
    public TagIntArray(int[] data) {
        super(data);
    }

    @Override
    public EnumTagType getType() {
        return EnumTagType.INT_ARRAY;
    }

    public int[] getData() {
        return NBTUtils.getIntArrayVal(this);
    }
}
