package com.github.mrmks.mc.marcabone.nbt;

public final class TagString extends TagBase {
    TagString(Object ins){
        super(ins);
    }
    public TagString(String str) {
        super(NBTUtils.newString(str));
    }

    @Override
    public NBTType getType() {
        return NBTType.STRING;
    }

    public String getData() {
        return NBTUtils.getStringVal(this);
    }
}
