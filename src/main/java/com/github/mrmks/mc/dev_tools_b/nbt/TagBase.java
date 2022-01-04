package com.github.mrmks.mc.dev_tools_b.nbt;

public abstract class TagBase {

    // the type of nmsInstance will be defined in subclass, for example in TagInt, the value will be an instance of NMS.NBTTagInt;
    private Object nmsInstance;

    final Object getNMSIns() {
        if (!hasNMSIns()) emptyNMSIns();
        return nmsInstance;
    }

    final void wrapNMSIns(Object obj) {
        this.nmsInstance = obj;
    }

    final boolean hasNMSIns() {
        return nmsInstance != null;
    }

    final void emptyNMSIns() {
        switch (getType()) {
            case COMPOUND:
                nmsInstance = NBTUtils.newCompound();
                break;
            case LIST:
                nmsInstance = NBTUtils.newList();
                break;
            case BYTE:
                nmsInstance = NBTUtils.newByte((byte) 0);
                break;
            case SHORT:
                nmsInstance = NBTUtils.newShort((short) 0);
                break;
            case INT:
                nmsInstance = NBTUtils.newInt(0);
                break;
            case LONG:
                nmsInstance = NBTUtils.newLong(0L);
                break;
            case FLOAT:
                nmsInstance = NBTUtils.newFloat(0);
                break;
            case DOUBLE:
                nmsInstance = NBTUtils.newDouble(0.0d);
                break;
            case STRING:
                nmsInstance = NBTUtils.newString("");
                break;
            case INT_ARRAY:
                nmsInstance = NBTUtils.newIntArray(new int[0]);
                break;
            case BYTE_ARRAY:
                nmsInstance = NBTUtils.newByteArray(new byte[0]);
                break;
            case LONG_ARRAY:
                nmsInstance = NBTUtils.newLongArray(new long[0]);
                break;
        }
    }

    protected final boolean testInstance() {
        return NBTUtils.testInstance(getType(), nmsInstance);
    }

    public abstract NBTType getType();
}