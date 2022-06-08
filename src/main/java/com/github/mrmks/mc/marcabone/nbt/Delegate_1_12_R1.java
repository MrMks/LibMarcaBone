package com.github.mrmks.mc.marcabone.nbt;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;

class Delegate_1_12_R1 implements NBTMethods {

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public Object newByte(byte d) {
        return new NBTTagByte(d);
    }

    @Override
    public Object newShort(short d) {
        return new NBTTagShort(d);
    }

    @Override
    public Object newInt(int d) {
        return new NBTTagInt(d);
    }

    @Override
    public Object newLong(long d) {
        return new NBTTagLong(d);
    }

    @Override
    public Object newFloat(float d) {
        return new NBTTagFloat(d);
    }

    @Override
    public Object newDouble(double d) {
        return new NBTTagDouble(d);
    }

    @Override
    public Object newString(String d) {
        return new NBTTagString(d);
    }

    @Override
    public Object newByteArray(byte[] d) {
        return new NBTTagByteArray(d);
    }

    @Override
    public Object newIntArray(int[] d) {
        return new NBTTagIntArray(d);
    }

    @Override
    public Object newLongArray(long[] d) {
        return new NBTTagLongArray(d);
    }

    @Override
    public Object newList() {
        return new NBTTagList();
    }

    @Override
    public Object newCompound() {
        return new NBTTagCompound();
    }

    @Override
    public byte getByte(Object d) {
        if (d instanceof NBTTagByte) {
            return ((NBTTagByte) d).g();
        }
        return 0;
    }

    @Override
    public short getShort(Object d) {
        if (d instanceof NBTTagShort) return ((NBTTagShort) d).f();
        return 0;
    }

    @Override
    public int getInt(Object d) {
        if (d instanceof NBTTagInt) return ((NBTTagInt) d).e();
        return 0;
    }

    @Override
    public long getLong(Object d) {
        if (d instanceof NBTTagLong) return ((NBTTagLong) d).d();
        return 0;
    }

    @Override
    public float getFloat(Object d) {
        if (d instanceof NBTTagFloat) return ((NBTTagFloat) d).i();
        return 0;
    }

    @Override
    public double getDouble(Object d) {
        if (d instanceof NBTTagDouble) return ((NBTTagDouble) d).asDouble();
        return 0;
    }

    @Override
    public String getString(Object d) {
        if (d instanceof NBTTagString) return ((NBTTagString) d).c_();
        return "";
    }

    @Override
    public byte[] getByteArray(Object d) {
        if (d instanceof NBTTagByteArray) return ((NBTTagByteArray) d).c();
        return new byte[0];
    }

    @Override
    public int[] getIntArray(Object d) {
        if (d instanceof NBTTagIntArray) return ((NBTTagIntArray) d).d();
        return new int[0];
    }

    private Field longArrayField;
    private boolean longArrayFieldFlag = false;

    @Override
    public long[] getLongArray(Object d) {
        if (d instanceof NBTTagLongArray) {
            if (longArrayField == null && !longArrayFieldFlag) {
                try {
                    longArrayField = NBTTagLongArray.class.getDeclaredField("b");
                    longArrayField.setAccessible(true);
                } catch (NoSuchFieldException e) {
                    // ignore
                }
                longArrayFieldFlag = true;
            }
            if (longArrayField != null) {
                try {
                    return (long[]) longArrayField.get(d);
                } catch (IllegalAccessException e) {
                    //ignore
                }
            }
        }
        return new long[0];
    }

    @Override
    public void listAdd(Object list, Object tag) {
        if (list instanceof NBTTagList && tag instanceof NBTBase) {
            ((NBTTagList) list).add((NBTBase) tag);
        }
    }

    @Override
    public void listAdd(Object list, int index, Object tag) {
        if (list instanceof NBTTagList && tag instanceof NBTBase) {
            ((NBTTagList) list).a(index, (NBTBase) tag);
        }
    }

    @Override
    public Object listRemove(Object list, int index) {
        if (list instanceof NBTTagList) {
            return ((NBTTagList) list).remove(index);
        }
        return null;
    }

    @Override
    public Object listGet(Object list, int index) {
        if (list instanceof NBTTagList) {
            return ((NBTTagList) list).i(index);
        }
        return null;
    }

    @Override
    public boolean listIsEmpty(Object list) {
        if (list instanceof NBTTagList) {
            return ((NBTTagList) list).isEmpty();
        }
        return false;
    }

    @Override
    public int listSize(Object list) {
        if (list instanceof NBTTagList) {
            return ((NBTTagList) list).size();
        }
        return 0;
    }

    @Override
    public void compoundSet(Object cmp, String k, Object v) {
        if (k != null && !k.isEmpty() && cmp instanceof NBTTagCompound && v instanceof NBTBase) {
            ((NBTTagCompound) cmp).set(k, (NBTBase) v);
        }
    }

    @Override
    public Object compoundGet(Object cmp, String k) {
        if (k != null && !k.isEmpty() && cmp instanceof NBTTagCompound) {
            return ((NBTTagCompound) cmp).get(k);
        }
        return null;
    }

    @Override
    public void compoundRemove(Object cmp, String k) {
        if (k != null && !k.isEmpty() && cmp instanceof NBTTagCompound) {
            ((NBTTagCompound) cmp).remove(k);
        }
    }

    @Override
    public boolean compoundIsEmpty(Object cmp) {
        if (cmp instanceof NBTTagCompound) {
            return ((NBTTagCompound) cmp).isEmpty();
        }
        return false;
    }

    @Override
    public int compoundSize(Object cmp) {
        if (cmp instanceof NBTTagCompound) {
            return ((NBTTagCompound) cmp).d();
        }
        return 0;
    }

    @Override
    public boolean compoundHasKey(Object cmp, String k) {
        if (cmp instanceof NBTTagCompound && k != null && !k.isEmpty()) {
            return ((NBTTagCompound) cmp).hasKey(k);
        }
        return false;
    }

    @Override
    public Set<String> compoundKeySet(Object cmp) {
        if (cmp instanceof NBTTagCompound) {
            return ((NBTTagCompound) cmp).c();
        }
        return Collections.emptySet();
    }

    @Override
    public boolean itemCanModify(Object stack) {
        return stack instanceof CraftItemStack || stack instanceof ItemStack;
    }

    private Field itemHandleField = null;
    private boolean itemHandleFieldFlag = false;

    private ItemStack reflectGetItemHandle(CraftItemStack stack) {
        if (!itemHandleFieldFlag) {
            try {
                itemHandleField = CraftItemStack.class.getDeclaredField("handle");
                itemHandleField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                // ignore
            }
            itemHandleFieldFlag = true;
        }
        if (itemHandleField == null) return null;
        try {
            return (ItemStack) itemHandleField.get(stack);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    @Override
    public TagCompound itemGetCompound(Object stack) {
        ItemStack nmsStack = null;
        if (stack instanceof CraftItemStack) {
            nmsStack = reflectGetItemHandle((CraftItemStack) stack);
        } else if (stack instanceof ItemStack) {
            nmsStack = (ItemStack) stack;
        }
        if (nmsStack != null) {
            boolean f = nmsStack.getTag() == null;
            if (f) nmsStack.setTag(new NBTTagCompound());
            if (nmsStack.hasTag()) {
                return new TagCompound(nmsStack.getTag());
            } else {
                if (f) nmsStack.setTag(null);
            }
        }
        return null;
    }

    @Override
    public void itemSetCompound(Object stack, Object tag) {
        if (tag instanceof NBTTagCompound) {
            ItemStack nmsStack = null;
            if (stack instanceof CraftItemStack) {
                nmsStack = reflectGetItemHandle((CraftItemStack) stack);
            } else if (stack instanceof ItemStack) {
                nmsStack = (ItemStack) stack;
            }
            if (nmsStack != null) {
                nmsStack.setTag((NBTTagCompound) tag);
            }
        }
    }

    @Override
    public boolean itemHasCompound(Object stack) {
        if (stack instanceof ItemStack) {
            return ((ItemStack) stack).hasTag();
        }
        return false;
    }

    @Override
    public org.bukkit.inventory.ItemStack itemConvert(org.bukkit.inventory.ItemStack stack) {
        return stack instanceof CraftItemStack ? stack : CraftItemStack.asCraftCopy(stack);
    }

    @Override
    public NBTType testInstance(Object obj) {
        return NBTType.values()[((NBTBase)obj).getTypeId()];
    }

    @Override
    public boolean testInstance(NBTType tag, Object obj) {
        return obj instanceof NBTBase && tag.ordinal() == ((NBTBase) obj).getTypeId();
    }
}
