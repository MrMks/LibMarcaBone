package com.github.mrmks.mc.marcabone.lang;

import java.util.Map;

public interface LanguageHelper {
    boolean has(String code);
    String trans(String code);
    String trans(String code, String def);
    String trans(String code, String key, String value);
    String trans(String code, String def, String key, String value);
    String trans(String code, String[] kv);
    String trans(String code, String def, String[] kv);
    @Deprecated
    String trans(String code, Map<String, String> map);
    @Deprecated
    String trans(String code, String def, Map<String, String> map);
}
