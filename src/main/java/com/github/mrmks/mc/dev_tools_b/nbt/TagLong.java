package com.github.mrmks.mc.dev_tools_b.nbt;

public class TagLong extends TagValued {
    TagLong(){}
    public TagLong(long data) {
        super(data);
    }

    @Override
    protected EnumTagType getType() {
        return EnumTagType.LONG;
    }

    public long getData() {
        return NBTUtils.getLongVal(this);
    }
}
