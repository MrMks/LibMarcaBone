package com.github.mrmks.mc.dev_tools_b.sheller;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;

public interface Operation {
    default boolean init(ArraySlice<String> slice) {
        return true;
    }
    Object invoke(Object stack, DataTable table);
}
