package com.github.mrmks.mc.marcabone.nbt;

public final class TagShort extends TagBase {
    TagShort(Object ins){
        super(ins);
    }
    public TagShort(short data) {
        super(NBTUtils.newShort(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.SHORT;
    }

    public short getData() {
        return NBTUtils.getShortVal(this);
    }
}
