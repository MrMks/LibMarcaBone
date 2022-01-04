package com.github.mrmks.mc.dev_tools_b.nbt;

public final class TagLong extends TagBase {
    TagLong(){}
    public TagLong(long data) {
        wrapNMSIns(NBTUtils.newLong(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.LONG;
    }

    public long getData() {
        return NBTUtils.getLongVal(this);
    }
}
