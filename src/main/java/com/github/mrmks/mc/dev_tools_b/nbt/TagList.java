package com.github.mrmks.mc.dev_tools_b.nbt;

public class TagList extends TagBase {

    public TagList() {
        this(true);
    }

    protected TagList(boolean def) {
        if (def)
            nmsInstance = NBTUtils.newList();
    }

    @Override
    protected EnumTagType getType() {
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