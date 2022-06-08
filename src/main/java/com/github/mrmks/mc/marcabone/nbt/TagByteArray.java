package com.github.mrmks.mc.marcabone.nbt;

public final class TagByteArray extends TagBase {
    TagByteArray(Object ins){
        super(ins);
    }
    public TagByteArray(byte[] data) {
        super(NBTUtils.newByteArray(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.BYTE_ARRAY;
    }

    public byte[] getData() {
        return NBTUtils.getByteArrayVal(this);
    }
}
