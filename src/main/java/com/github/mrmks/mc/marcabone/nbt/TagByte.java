package com.github.mrmks.mc.marcabone.nbt;

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
