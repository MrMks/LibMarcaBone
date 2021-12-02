package com.github.mrmks.mc.dev_tools_b.nbt;

public final class TagInt extends TagValued {
    TagInt(){}
    public TagInt(int data) {
        super(data);
    }

    @Override
    public EnumTagType getType() {
        return EnumTagType.INT;
    }

    public int getData() {
        return NBTUtils.getIntVal(this);
    }
}
