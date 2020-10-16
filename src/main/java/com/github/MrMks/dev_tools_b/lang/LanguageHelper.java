package com.github.MrMks.dev_tools_b.lang;

import java.util.Map;

public interface LanguageHelper {
    String trans(String code);
    String trans(String code, Map<String, String> map);
}
