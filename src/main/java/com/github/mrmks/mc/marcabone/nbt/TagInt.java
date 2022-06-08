package com.github.mrmks.mc.marcabone.nbt;

public final class TagInt extends TagBase {
    TagInt(Object ins){
        super(ins);
    }
    public TagInt(int data) {
        super(NBTUtils.newInt(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.INT;
    }

    public int getData() {
        return NBTUtils.getIntVal(this);
    }
}
