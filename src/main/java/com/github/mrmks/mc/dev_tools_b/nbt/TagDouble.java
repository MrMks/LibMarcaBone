package com.github.mrmks.mc.dev_tools_b.nbt;

public class TagDouble extends TagValued {
    TagDouble(){}
    public TagDouble(double data) {
        super(data);
    }

    @Override
    protected EnumTagType getType() {
        return EnumTagType.DOUBLE;
    }

    public double getData() {
        return NBTUtils.getDoubleVal(this);
    }
}
