package com.github.mrmks.mc.dev_tools_b.nbt;

public enum EnumTagType {
    END, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BYTE_ARRAY, STRING, LIST, COMPOUND, INT_ARRAY, LONG_ARRAY;

    String getClassName() {
        switch (this) {
            case BYTE:
                return "NBTTagByte";
            case SHORT:
                return "NBTTagShort";
            case INT:
                return "NBTTagInt";
            case LONG:
                return "NBTTagLong";
            case FLOAT:
                return "NBTTagFloat";
            case DOUBLE:
                return "NBTTagDouble";
            case BYTE_ARRAY:
                return "NBTTagByteArray";
            case STRING:
                return "NBTTagString";
            case LIST:
                return "NBTTagList";
            case COMPOUND:
                return "NBTTagCompound";
            case INT_ARRAY:
                return "NBTTagIntArray";
            case LONG_ARRAY:
                return "NBTTagLongArray";
            case END:
                return "NBTTagEnd";
            default:
                return "";
        }
    }

    Object getTestParams() {
        switch (this) {
            case BYTE:
                return (byte)0;
            case SHORT:
                return (short)0;
            case INT:
                return 0;
            case LONG:
                return 0L;
            case FLOAT:
                return 0.0F;
            case DOUBLE:
                return 0.0D;
            case BYTE_ARRAY:
                return new byte[]{0};
            case STRING:
                return "0";
            case INT_ARRAY:
                return new int[]{0};
            case LONG_ARRAY:
                return new long[]{0L};
            default:
                return null;
        }
    }

    boolean hasConstructorParams() {
        switch (this) {
            case BYTE:
            case SHORT:
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case BYTE_ARRAY:
            case STRING:
            case INT_ARRAY:
            case LONG_ARRAY:
                return true;
            default:
            case LIST:
            case COMPOUND:
            case END:
                return false;
        }
    }

    Class<?> getConstructorParamTypes() {
        switch (this) {
            case BYTE:
                return byte.class;
            case SHORT:
                return short.class;
            case INT:
                return int.class;
            case LONG:
                return long.class;
            case FLOAT:
                return float.class;
            case DOUBLE:
                return double.class;
            case STRING:
                return String.class;
            case INT_ARRAY:
                return int[].class;
            case BYTE_ARRAY:
                return byte[].class;
            case LONG_ARRAY:
                return long[].class;
            default:
                return null;
        }
    }

    String getGetterName() {
        switch (this) {
            case BYTE:
                return "g";
            case SHORT:
                return "f";
            case INT:
                return "e";
            case LONG:
            case INT_ARRAY:
                return "d";
            case FLOAT:
                return "i";
            case DOUBLE:
                return "asDouble";
            case STRING:
                return "c_";
            case BYTE_ARRAY:
                return "c";
            default:
                return "";
        }
    }
}
