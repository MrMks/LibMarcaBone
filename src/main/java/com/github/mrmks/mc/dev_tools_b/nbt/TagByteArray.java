package com.github.mrmks.mc.dev_tools_b.nbt;

public final class TagByteArray extends TagBase {
    TagByteArray(){}
    public TagByteArray(byte[] data) {
        wrapNMSIns(NBTUtils.newByteArray(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.BYTE_ARRAY;
    }

    public byte[] getData() {
        return NBTUtils.getByteArrayVal(this);
    }
}
