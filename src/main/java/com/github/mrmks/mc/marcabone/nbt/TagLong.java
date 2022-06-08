package com.github.mrmks.mc.marcabone.nbt;

public final class TagLong extends TagBase {
    TagLong(Object ins){
        super(ins);
    }
    public TagLong(long data) {
        super(NBTUtils.newLong(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.LONG;
    }

    public long getData() {
        return NBTUtils.getLongVal(this);
    }
}
