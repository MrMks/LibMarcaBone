package com.github.mrmks.mc.marcabone.nbt;

public final class TagFloat extends TagBase {
    TagFloat(Object obj){
        super(obj);
    }
    public TagFloat(float data) {
        super(NBTUtils.newFloat(data));
    }

    @Override
    public NBTType getType() {
        return NBTType.FLOAT;
    }

    public float getData() {
        return NBTUtils.getFloatVal(this);
    }
}
