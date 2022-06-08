package com.github.mrmks.mc.marcabone.sheller;

import com.github.mrmks.mc.marcabone.utils.ArraySlice;

public interface Operation {
    default boolean init(ArraySlice<String> slice) {
        return true;
    }
    Object invoke(Object stack, DataTable table);
}
