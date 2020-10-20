package com.github.MrMks.dev_tools_b.lang;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagI18N {
    private static final Pattern pattern = Pattern.compile("#(?:i18n_lore|il)\\.([^<>#]+?)((?:<[^:]+?:[^:]+?>)*)#");
    public static String trans(LocalePlayer player, String line) {
        return transComplex(player, line).toR();
    }

    static TagPack transComplex(LocalePlayer player, String line) {
        String transSrc = line;
        String nonColor = line.replaceAll("§.","");
        Map<String,TagPack> map = new HashMap<>();
        if (nonColor.startsWith("#i18n_lore") || nonColor.startsWith("#il")) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String i18nKey = "i18n_lore." + matcher.group(1);
                if (player.has(i18nKey)) {
                    transSrc = player.trans(i18nKey);
                    String tmps = matcher.group(2);
                    if (tmps != null) {
                        String[] tags = matcher.group(2).split("<|><|>");
                        for (String tag : tags) {
                            if (tag != null && !tag.isEmpty()) {
                                String[] tmp = tag.split(":");
                                if (tmp.length == 2) {
                                    map.put(tmp[0], transComplex(player, tmp[1]));
                                }
                            }
                        }
                    }
                }
            }
        }
        return map.isEmpty() ? new TagPackSimple(transSrc) : new TagPackComplex(transSrc, map);
    }

    private interface TagPack {
        String toR();
    }

    private static class TagPackSimple implements TagPack {
        private final String str;
        TagPackSimple(String str){
            this.str = str;
        }

        @Override
        public String toR() {
            return str;
        }
    }

    private static class TagPackComplex implements TagPack {
        private final String str;
        private final Map<String, TagPack> map;

        TagPackComplex(String str, Map<String, TagPack> map) {
            this.str = str == null ? "" : str;
            this.map = map == null ? Collections.emptyMap() : map;
        }

        @Override
        public String toR() {
            String re = str;
            for (Map.Entry<String, TagPack> entry : map.entrySet()) {
                re = re.replace(String.format("<%s>",entry.getKey()), entry.getValue().toR());
            }
            return re;
        }
    }
}
