package com.github.mrmks.mc.marcabone.nbt;

public abstract class TagBase {

    // the type of nmsInstance will be defined in subclass, for example in TagInt, the value will be an instance of NMS.NBTTagInt;
    private Object nmsInstance;
    protected TagBase() {}
    protected TagBase(Object nmsIns) {
        this.nmsInstance = nmsIns;
    }

    final Object getNMSIns() {
        if (nmsInstance == null) nmsInstance = NBTUtils.createEmptyInstance(getType());
        return nmsInstance;
    }

    final Object getNMSIns0() {
        return nmsInstance;
    }

    public abstract NBTType getType();
}