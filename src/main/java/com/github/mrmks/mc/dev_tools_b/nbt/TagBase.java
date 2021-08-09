package com.github.mrmks.mc.dev_tools_b.nbt;

public abstract class TagBase {

    // the type of nmsInstance will be defined in subclass, for example in TagInt, the value will be an instance of NMS.NBTTagInt;
    protected Object nmsInstance;

    protected Object getNMSIns() {
        return nmsInstance;
    }

    void wrapNMSIns(Object obj) {
        this.nmsInstance = obj;
    }

    protected boolean testInstance() {
        return NBTUtils.isInstanceOf(getType(), nmsInstance);
    }

    public abstract EnumTagType getType();
}