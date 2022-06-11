package com.github.mrmks.mc.marcabone.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StringUtils {

    public static StringReplace replacer(String src) {
        return new StringReplace(src);
    }

    public static String replaceTag(String src, String key, String replacement) {
        if (src == null || src.isEmpty()) return src;
        if (key == null) return src;
        if (!src.contains("<" + key + ">")) return src;

        return replaceTag(new StringBuilder(src), key, replacement).toString();
    }

    @Deprecated
    public static String replaceTag(String src, Map<String, String> map) {
        if (src == null || src.isEmpty()) return src;
        if (map == null || map.isEmpty()) return src;
        StringBuilder bd = null;
        for (Map.Entry<String, String> e : map.entrySet()) {
            String k = e.getKey();
            if (src.contains('<' + k + '>')) {
                if (bd == null) bd = new StringBuilder(src);
                replaceTag(bd, k, e.getValue());
            }
        }
        return bd == null ? src : bd.toString();
    }

    public static String replaceTag(String str, String[] kvAry) {
        if (str == null || str.isEmpty() || kvAry == null || kvAry.length < 2) return str;
        int lmt = kvAry.length / 2;
        StringBuilder bd = new StringBuilder(str);
        String k, v;
        for (int i = 0; i < lmt; i++) {
            k = kvAry[i * 2]; v = kvAry[i * 2 + 1];
            if (k != null && !k.isEmpty() && bd.indexOf('<' + k + '>') >= 0) {
                replaceTag(bd, k, v);
            }
        }
        return bd.toString();
    }

    public static String replaceTag(String str, List<String> kvLst) {
        if (str == null || str.isEmpty() || kvLst == null || kvLst.size() < 2) return str;
        int lmt = kvLst.size() / 2;
        StringBuilder bd = new StringBuilder(str);
        String k, v;
        for (int i = 0; i < lmt; i++) {
            k = kvLst.get(i * 2); v = kvLst.get(i * 2 + 1);
            if (k != null && !k.isEmpty() && bd.indexOf('<' + k + '>') >= 0) {
                replaceTag(bd, k, v);
            }
        }
        return bd.toString();
    }

    static StringBuilder replaceTag(StringBuilder bd, String k, String v) {
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

    public static String replace(String str, String k, String v) {
        if (str == null || str.isEmpty()) return str;
        if (k == null || k.isEmpty()) return str;

        return replace(new StringBuilder(str), k, v).toString();
    }

    public static String replace(String str, String[] kvAry) {
        if (str == null || str.isEmpty() || kvAry == null || kvAry.length < 2) return str;
        int lmt = kvAry.length / 2;
        StringBuilder bd = new StringBuilder(str);
        String k;
        for (int i = 0; i < lmt; i++) {
            if ((k = kvAry[i * 2]) != null && !k.isEmpty()) replace(bd, k, kvAry[i * 2 + 1]);
        }
        return bd.toString();
    }

    public static String replace(String str, List<String> kvLst) {
        if (str == null || str.isEmpty() || kvLst == null || kvLst.size() < 2) return str;
        int lmt = kvLst.size() / 2;
        StringBuilder bd = new StringBuilder(str);
        String k;
        for (int i = 0; i < lmt; i++) {
            if ((k = kvLst.get(i * 2)) != null && !k.isEmpty()) replace(bd, k, kvLst.get(i * 2 + 1));
        }
        return bd.toString();
    }

    static StringBuilder replace(StringBuilder bd, String k, String v) {
        if (v == null) v = "";
        int kl = k.length(), df = v.length() - kl;
        int ks = bd.length() / kl / 2;
        int[] ki = df == 0 ? null : new int[Math.max(ks, 1)];
        ks = 0;

        for (int i = 0; i < bd.length(); i++) {
            int c = bd.charAt(i);
            if (c == k.charAt(0)) {
                boolean match = true;
                for (int j = 1; j < kl; ++j) {
                    if (!(match = i + j < bd.length() && bd.charAt(i + j) == k.charAt(j))) break;
                }
                if (match) {
                    if (df == 0) {
                        bd.replace(i, i + kl, v);
                    } else {
                        if (ks == ki.length) ki = Arrays.copyOf(ki, ks << 1);
                        ki[ks++] = i;
                        i += kl - 1;
                    }
                }
            }
        }

        if (ks > 0) {
            int vl = v.length();
            if (df > 0) {
                int ts = df * ks, ol = bd.length(), oei = ol;
                bd.setLength(bd.length() + ts);
                for (; ks > 0; ks--) {
                    int ei = oei - 1;
                    for (int i = (oei = ki[ks - 1]) + kl - 1; ei > i; ei--) bd.setCharAt(ei + ts, bd.charAt(ei));
                    ts -= df;
                    for (int i = 0; i < vl; i++) bd.setCharAt(oei + ts + i, v.charAt(i));
                }
            } else {
                int ts = 0;
                for (int i = 0; i < ks; i++) {
                    int bi = ki[i];
                    for (int j = 0; j < vl; j ++) bd.setCharAt(bi + ts, v.charAt(j));
                    ts += df;
                    for (int j = bi + kl; j < (i == ks - 1 ? bd.length() : ki[i + 1]); j++) bd.setCharAt(j + ts, bd.charAt(j));
                }
                bd.setLength(bd.length() + ts);
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

    public static class StringReplace {
        private final String str;
        private StringBuilder bd;
        StringReplace(String str) {
            if (str == null) throw new IllegalArgumentException("Source string can't be null");
            this.str = str;
        }

        public StringReplace replaceTag(String key, String value) {
            if (key != null) {
                if (value == null) value = "";
                if (bd == null) bd = new StringBuilder(str);
                StringUtils.replaceTag(bd, key, value);
            }
            return this;
        }

        public StringReplace replace(String key, String value) {
            if (key != null) {
                if (value == null) value = "";
                if (bd == null) bd = new StringBuilder(str);
                StringUtils.replace(bd, key, value);
            }
            return this;
        }

        @Override
        public String toString() {
            return bd == null ? str : bd.toString();
        }
    }
}
