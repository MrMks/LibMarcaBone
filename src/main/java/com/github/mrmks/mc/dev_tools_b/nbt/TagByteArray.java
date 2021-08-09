package com.github.mrmks.mc.dev_tools_b.nbt;

public class TagByteArray extends TagValued {
    TagByteArray(){}
    public TagByteArray(byte[] data) {
        super(data);
    }

    @Override
    public EnumTagType getType() {
        return EnumTagType.BYTE_ARRAY;
    }

    public byte[] getData() {
        return NBTUtils.getByteArrayVal(this);
    }
}
