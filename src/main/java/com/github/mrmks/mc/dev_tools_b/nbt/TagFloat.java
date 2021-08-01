package com.github.mrmks.mc.dev_tools_b.nbt;

public class TagFloat extends TagValued {
    TagFloat(){}
    public TagFloat(float data) {
        super(data);
    }

    @Override
    protected EnumTagType getType() {
        return EnumTagType.FLOAT;
    }

    public float getData() {
        return NBTUtils.getFloatVal(this);
    }
}
