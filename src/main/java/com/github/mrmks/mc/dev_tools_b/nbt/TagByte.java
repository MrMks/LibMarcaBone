package com.github.mrmks.mc.dev_tools_b.nbt;

public final class TagByte extends TagValued {
    TagByte(){}
    public TagByte(byte data) {
        super(data);
    }

    @Override
    public EnumTagType getType() {
        return EnumTagType.BYTE;
    }

    public byte getData() {
        return NBTUtils.getByteVal(this);
    }
}
