package com.github.mrmks.mc.dev_tools_b.pcmd;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;

public class ArgSlice extends ArraySlice<String> {
    public ArgSlice(String[] src) {
        this(src, 0, src.length);
    }

    public ArgSlice(String[] src, int begin) {
        this(src, begin, src.length);
    }

    public ArgSlice(String[] src, int begin, int end) {
        super(src, begin, end);
    }
}
