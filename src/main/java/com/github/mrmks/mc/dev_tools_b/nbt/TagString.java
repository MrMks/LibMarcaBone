package com.github.mrmks.mc.dev_tools_b.nbt;

public final class TagString extends TagValued {
    TagString(){}
    public TagString(String str) {
        super(str);
    }

    @Override
    public EnumTagType getType() {
        return EnumTagType.STRING;
    }

    public String getData() {
        return NBTUtils.getStringVal(this);
    }
}
