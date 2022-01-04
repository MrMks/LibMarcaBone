package com.github.mrmks.mc.dev_tools_b.nbt;

public final class TagByte extends TagBase {
    TagByte(){}
    public TagByte(byte data) {
        wrapNMSIns(NBTUtils.newByte(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.BYTE;
    }

    public byte getData() {
        return NBTUtils.getByteVal(this);
    }
}
