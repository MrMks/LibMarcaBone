package com.github.mrmks.mc.dev_tools_b.nbt;

import org.bukkit.inventory.ItemStack;

public class NBTItem {
    private final boolean modifiable;
    private final ItemStack stack;
    public NBTItem(ItemStack stack) {
        modifiable = NBTUtils.isItemTagModifiable(stack);
        this.stack = stack;
    }

    public boolean isModifiable() {
        return modifiable;
    }

    public TagCompound getTag() {
        return NBTUtils.getItemStackTag(stack);
    }

    public void setTag(TagCompound tag) {
        NBTUtils.setItemStackTag(stack, tag);
    }

    public void makeTag() {
        if (getTag() == null) setTag(new TagCompound());
    }
}
