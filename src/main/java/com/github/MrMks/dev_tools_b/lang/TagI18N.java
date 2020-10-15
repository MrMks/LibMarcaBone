package com.github.MrMks.dev_tools_b.lang;

import java.util.Map;

public class TagI18N {
    static String trans(String line, Map<String, String> map) {
        if (line != null && !line.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                line = line.replace(String.format("<%s>", entry.getKey()), entry.getValue());
            }
            return line;
        } else return "";
    }
}
