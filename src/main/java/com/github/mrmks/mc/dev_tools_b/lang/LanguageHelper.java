package com.github.mrmks.mc.dev_tools_b.lang;

import java.util.Map;

public interface LanguageHelper {
    boolean has(String code);
    String trans(String code);
    String trans(String code, String def);
    String trans(String code, String key, String value);
    String trans(String code, String def, String key, String value);
    String trans(String code, Map<String, String> map);
    String trans(String code, String def, Map<String, String> map);
}
