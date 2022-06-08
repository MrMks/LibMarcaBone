package com.github.mrmks.mc.marcabone.utils;

import java.util.ArrayList;
import java.util.List;
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

    public static String[] split(String src, char sp) {
        return split(src, sp, false);
    }

    public static String[] split(String src, char sp, boolean rmEmpty) {
        int cur = src.indexOf(sp), last = -1;
        if (cur < 0) return rmEmpty && src.isEmpty() ? new String[0] : new String[]{src};
        ArrayList<String> list = new ArrayList<>(4);
        do {
            if (last + 1 < cur || (!rmEmpty && last + 1 == cur))
                list.add(src.substring(last + 1, cur));
            last = cur;
        } while ((cur = src.indexOf(sp, cur + 1)) > 0);
        if (last + 1 < src.length() || (!rmEmpty && last + 1 == src.length()))
            list.add(src.substring(last + 1));
        return list.toArray(new String[0]);
    }

    public static String append(List<String> lst, String sp) {
        if (lst == null || lst.isEmpty()) return "";
        if (lst.size() == 1) return lst.get(0);
        StringBuilder bd = new StringBuilder();
        for (String str : lst) bd.append(str).append(sp);
        return bd.substring(0, bd.length() - sp.length());
    }

    public static String append(List<String> lst, char sp) {
        if (lst == null || lst.isEmpty()) return "";
        if (lst.size() == 1) return lst.get(0);
        StringBuilder bd = new StringBuilder();
        for (String str : lst) bd.append(str).append(sp);
        return bd.substring(0, bd.length() - 1);
    }
}
