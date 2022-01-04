package com.github.mrmks.mc.dev_tools_b.nbt;

public final class TagInt extends TagBase {
    TagInt(){}
    public TagInt(int data) {
        wrapNMSIns(NBTUtils.newInt(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.INT;
    }

    public int getData() {
        return NBTUtils.getIntVal(this);
    }
}
