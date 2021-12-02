package com.github.mrmks.mc.dev_tools_b.nbt;

import java.util.Set;

public final class TagCompound extends TagBase {

    public TagCompound() {}

    @Override
    public EnumTagType getType() {
        return EnumTagType.COMPOUND;
    }

    public TagBase getTag(String key) {
        if (key == null) return null;
        return NBTUtils.compoundGet(this, key);
    }

    public void setTag(String key, TagBase tag) {
        if (key == null || tag == null) return;
        NBTUtils.compoundSet(this, key, tag);
    }

    public void setTagByte(String key, byte data) {
        setTag(key, new TagByte(data));
    }

    public void setTagShort(String key, short data) {
        setTag(key, new TagShort(data));
    }

    public void setTagInt(String key, int data) {
        setTag(key, new TagInt(data));
    }

    public void setTagLong(String key, long data) {
        setTag(key, new TagLong(data));
    }

    public void setTagFloat(String key, float data) {
        setTag(key, new TagFloat(data));
    }

    public void setTagDouble(String key, double data) {
        setTag(key, new TagDouble(data));
    }

    public void setTagByteArray(String key, byte[] data) {
        if (data == null) return;
        setTag(key, new TagByteArray(data));
    }

    public void setTagIntArray(String key, int[] data) {
        if (data == null) return;
        setTag(key, new TagIntArray(data));
    }

    public void setTagLongArray(String key, long[] data) {
        if (data == null) return;
        setTag(key, new TagLongArray(data));
    }

    public void removeTag(String key) {
        if (key == null) return;
        NBTUtils.compoundRemove(this, key);
    }

    public boolean hasKey(String key) {
        if (key == null) return false;
        return NBTUtils.compoundHasKey(this, key);
    }

    public boolean isEmpty() {
        return NBTUtils.compoundIsEmpty(this);
    }

    public Set<String> keySet() {
        return NBTUtils.compoundKeySet(this);
    }

    public int size() {
        return NBTUtils.compoundSize(this);
    }
}
