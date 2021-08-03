package com.github.mrmks.mc.dev_tools_b.pcmd;

class CommandUtils {

    static String pureName(String name) {
        if (name == null) return "null";
        if (name.isEmpty()) return "empty";
        return name;
    }

    static String[] pureAliases(String name, String[] aliases) {
        if (aliases == null) return new String[0];
        if (aliases.length == 0) return aliases;

        int i = 0, j = 0;
        boolean[] tmp = new boolean[aliases.length];
        for (String str : aliases) {
            tmp[j] = str != null && !str.isEmpty() && !str.equals(name);
            if (tmp[j++]) i++;
        }
        String[] ret = new String[i];
        i = 0; j = 0;
        for (;i < tmp.length; i++) {
            if (tmp[i]) ret[j++] = aliases[i];
        }
        return ret;
    }

    static String selectString(String cmd, String manual, boolean[] ary, int index) {
        ary[index] = cmd == null || cmd.isEmpty();
        return ary[index] ? manual : cmd;
    }

    static String nonNull(String str, String def) {
        return str == null ? (def == null ? "" : def) : str;
    }
}
