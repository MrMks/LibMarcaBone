package com.github.mrmks.mc.dev_tools_b.cmd.utils;

public class UsageBuild {
    private String string;

    public UsageBuild append(ArgumentType type, String arg) {
        if (string == null) {
            string = type.wrap(arg);
        } else {
            string = string.concat(" ").concat(type.wrap(arg));
        }
        return this;
    }

    public String toString() {
        return string == null ? "" : string;
    }

    public enum ArgumentType {
        PLAIN("", ""), REPLACED("\u0167o", "\u0167r"), REQUIRED("<",">"), OPTIONAL("[", "]");

        final String pre, sub;
        ArgumentType(String pre, String sub) {
            this.pre = pre;
            this.sub = sub;
        }

        String wrap(String arg) {
            return pre + arg + sub;
        }
    }
}
