package com.github.mrmks.mc.dev_tools_b.nbt;

public class TagShort extends TagValued {
    TagShort(){}
    public TagShort(short data) {
        super(data);
    }

    @Override
    protected EnumTagType getType() {
        return EnumTagType.SHORT;
    }

    public short getData() {
        return NBTUtils.getShortVal(this);
    }
}
