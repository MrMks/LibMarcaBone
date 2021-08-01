package com.github.mrmks.mc.dev_tools_b.nbt;

public class TagString extends TagValued {
    TagString(){}
    public TagString(String str) {
        super(str);
    }

    @Override
    protected EnumTagType getType() {
        return EnumTagType.STRING;
    }

    public String getData() {
        return NBTUtils.getStringVal(this);
    }
}
