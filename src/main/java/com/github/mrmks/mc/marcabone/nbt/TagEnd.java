package com.github.mrmks.mc.marcabone.nbt;

public final class TagEnd extends TagBase {

    TagEnd(Object obj) {
        super(obj);
    }
    @Override
    public NBTType getType() {
        return NBTType.END;
    }
}
