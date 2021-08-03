package com.github.mrmks.mc.dev_tools_b.lang;

import java.util.Map;

class LanguageUtils {
    static String transWithTag(String str, Map<String, String> map) {
        if (str == null || str.isEmpty()) return "";
        if (map == null || map.isEmpty())
            return str;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String k = entry.getKey(), v = entry.getValue();
            if (k != null && !k.isEmpty()) {
                v = v == null ? "" : v;
                str = str.replace("<" + entry.getKey() + ">", v);
            }
        }
        return str;
    }
}
