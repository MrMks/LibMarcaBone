package com.github.mrmks.mc.marcabone.nbt;

public final class TagShort extends TagBase {
    TagShort(){}
    public TagShort(short data) {
        wrapNMSIns(NBTUtils.newShort(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.SHORT;
    }

    public short getData() {
        return NBTUtils.getShortVal(this);
    }
}
