package com.github.mrmks.mc.marcabone.nbt;

public final class TagDouble extends TagBase {
    TagDouble(){}
    public TagDouble(double data) {
        wrapNMSIns(NBTUtils.newDouble(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.DOUBLE;
    }

    public double getData() {
        return NBTUtils.getDoubleVal(this);
    }
}
