package com.github.mrmks.mc.dev_tools_b.nbt;

public class TagLongArray extends TagValued {
    TagLongArray(){}
    public TagLongArray(long[] data) {
        super(data);
    }

    @Override
    public EnumTagType getType() {
        return EnumTagType.LONG_ARRAY;
    }

    public long[] getData() {
        return NBTUtils.getLongArrayVal(this);
    }
}
