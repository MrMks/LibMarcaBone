package com.github.mrmks.mc.marcabone.nbt;

import org.bukkit.inventory.ItemStack;

public final class NBTItem {
    private final boolean modifiable;
    private final boolean same;
    final ItemStack stack;

    public NBTItem(ItemStack stack) {
        this.stack = NBTUtils.wrapCopy(stack);
        this.same = stack == this.stack;
        modifiable = NBTUtils.isItemTagModifiable(this.stack);
    }

    public boolean isModifiable() {
        return modifiable;
    }

    public boolean isSource() {
        return same;
    }

    public boolean hasTag() {
        return NBTUtils.itemStackHasTag(stack);
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

    public ItemStack getItem() {
        return stack;
    }
}
