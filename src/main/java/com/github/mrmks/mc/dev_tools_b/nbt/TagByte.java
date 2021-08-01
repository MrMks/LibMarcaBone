package com.github.mrmks.mc.dev_tools_b.nbt;

public class TagByte extends TagValued {
    TagByte(){}
    public TagByte(byte data) {
        super(data);
    }

    @Override
    protected EnumTagType getType() {
        return EnumTagType.BYTE;
    }

    public byte getData() {
        return NBTUtils.getByteVal(this);
    }
}
