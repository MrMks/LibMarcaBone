package com.github.mrmks.mc.dev_tools_b.nbt;

abstract class TagValued extends TagBase {
    TagValued(){}
    protected TagValued(Object data) {
        nmsInstance = NBTUtils.newInstance(getType(), data);
    }
}
