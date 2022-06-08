package com.github.mrmks.mc.marcabone.nbt;

public final class TagFloat extends TagBase {
    TagFloat(){}
    public TagFloat(float data) {
        wrapNMSIns(NBTUtils.newFloat(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.FLOAT;
    }

    public float getData() {
        return NBTUtils.getFloatVal(this);
    }
}