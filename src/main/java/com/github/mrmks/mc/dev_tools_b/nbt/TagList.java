package com.github.mrmks.mc.dev_tools_b.nbt;

public final class TagList extends TagBase {

    public TagList() {}

    @Override
    public EnumTagType getType() {
        return EnumTagType.LIST;
    }

    public void addTag(TagBase tag) {
        if (tag == null) return;
        NBTUtils.listAdd(this, tag);
    }

    public void addTag(int index, TagBase tag) {
        if (tag == null) return;
        NBTUtils.listAdd(this, index,tag);
    }

    public TagBase getTag(int index) {
        return NBTUtils.listGet(this, index);
    }

    public TagBase removeTag(int index) {
        return NBTUtils.listRemove(this, index);
    }

    public boolean isEmpty() {
        return NBTUtils.listIsEmpty(this);
    }

    public int size() {
        return NBTUtils.listSize(this);
    }
}
