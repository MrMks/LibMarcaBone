package com.github.mrmks.mc.marcabone.nbt;

public final class TagDouble extends TagBase {
    TagDouble(Object ins){
        super(ins);
    }
    public TagDouble(double data) {
        super(NBTUtils.newDouble(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.DOUBLE;
    }

    public double getData() {
        return NBTUtils.getDoubleVal(this);
    }
}
