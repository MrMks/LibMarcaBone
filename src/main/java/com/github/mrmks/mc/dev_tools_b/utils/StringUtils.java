package com.github.mrmks.mc.dev_tools_b.utils;

import java.util.Map;

public class StringUtils {

    public static String replace(String src, String key, String replacement) {
        if (src == null || src.isEmpty()) return src;
        if (key == null) return src;
        if (!src.contains("<" + key + ">")) return src;

        return replace(new StringBuilder(src), key, replacement).toString();
    }

    public static String replace(String src, Map<String, String> map) {
        if (src == null || src.isEmpty()) return src;
        if (map == null || map.isEmpty()) return src;
        StringBuilder bd = null;
        for (Map.Entry<String, String> e : map.entrySet()) {
            String k = e.getKey();
            if (src.contains('<' + k + '>')) {
                if (bd == null) bd = new StringBuilder(src);
                replace(bd, k, e.getValue());
            }
        }
        return bd == null ? src : bd.toString();
    }

    static StringBuilder replace(StringBuilder bd, String k, String v) {
        if (v == null) v = "";
        int m = -1, df = k.length() + 2 - v.length();
        boolean lst = false;
        for (int i = 0; i < bd.length(); i++){
            int c = bd.charAt(i);
            boolean lstF = lst;
            lst = false;
            switch (c) {
                default:
                    if (m >= 0) {
                        m = m < k.length() && k.charAt(m) == c ? m + 1 : -1;
                    }
                    break;
                case '<':
                    if (!lstF) m = 0;
                    else bd.deleteCharAt(i - 1);
                    break;
                case '>':
                    if (m == k.length()) {
                        bd.replace(i - m - 1, i + 1, v);
                        i -= df;
                    }
                    m = -1;
                    break;
                case '\\':
                    lst = true;
                    break;
            }
        }
        return bd;
    }
}
