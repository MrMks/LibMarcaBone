package com.github.mrmks.mc.dev_tools_b.lang;

import com.github.mrmks.mc.dev_tools_b.utils.YamlConfigurationLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.mrmks.mc.dev_tools_b.lang.LanguageFile.EMPTY;

public class LanguageAPI {
    private static LanguageAPI DEFAULT;

    public static void initDefault(LanguageAPI api) {
        if (DEFAULT == null) DEFAULT = api;
    }

    public static void reloadDefault() {
        DEFAULT.reload();
    }

    protected String LOCALE_KEY = "locale";
    protected String TRANSLATION_KEY = "translation";


    private final HashMap<String, LanguageFile> localeMap = new HashMap<>();
    private String defaultLocale = EMPTY.getLocale();
    private final Plugin plugin;

    public LanguageAPI(Plugin plugin) {
        this.plugin = plugin;
        load0();
    }

    public void reload() {
        defaultLocale = EMPTY.getLocale();
        localeMap.clear();
        load0();
    }

    private void load0() {
        YamlConfigurationLoader loader = new YamlConfigurationLoader(plugin, "lang/default.yml");
        loader.saveDefaultConfig();
        FileConfiguration configuration = loader.getConfig();
        if (configuration != null) {
            LanguageFile file = new LanguageFile(configuration, LOCALE_KEY, TRANSLATION_KEY);
            defaultLocale = file.getLocale();
            localeMap.put(file.getLocale(), file);
        }
        File file = new File(plugin.getDataFolder(), "lang");
        readTranslations(file, plugin.getLogger());
    }

    private void readTranslations(File path, Logger logger) {
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    readTranslations(file, logger);
                }
            }
        } else {
            if (path.getName().equals("default.yml")) return;
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(path);
            LanguageFile file = new LanguageFile(cfg, LOCALE_KEY, TRANSLATION_KEY);
            if (localeMap.containsKey(file.getLocale())) {
                logger.log(Level.WARNING,
                        String.format("Can't read file §c%s§r as the same locale §2%s§r has been registered", path.getName(), file.getLocale()));
                localeMap.get(file.getLocale()).merge(file);
            } else {
                localeMap.put(file.getLocale(), file);
            }
        }
    }

    public boolean hasKey(String locale, String key) {
        return localeMap.containsKey(locale) && localeMap.get(locale).has(key);
    }

    public boolean hasKey(UUID uuid, String key) {
        PlayerLocaleManager pm = PlayerLocaleManager.getInstance();
        return pm.has(uuid) && localeMap.getOrDefault(pm.get(uuid), EMPTY).has(key);
    }

    public String getTranslation(String locale, String key) {
        return localeMap.getOrDefault(locale, EMPTY).getOrDefault(key, localeMap.getOrDefault(defaultLocale, EMPTY).get(key));
    }

    public String getTranslation(String locale, String key, Map<String, String> map) {
        return translateWithTag(getTranslation(locale, key), map);
    }

    public String getTranslation(UUID uuid, String key){
        return getTranslation(PlayerLocaleManager.getInstance().get(uuid), key);
    }

    public String getTranslation(UUID uuid, String key, Map<String, String> map) {
        return translateWithTag(getTranslation(uuid, key),map);
    }

    private String translateWithTag(String line, Map<String, String> map) {
        if (line != null && !line.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                line = line.replace(String.format("<%s>", entry.getKey()), entry.getValue());
            }
            return line;
        } else return "";
    }

    public LanguageHelper helper(String locale) {
        return new LocaleHelper(locale, this);
    }

    public LanguageHelper helper(UUID uuid) {
        return new LocalePlayer(uuid, this);
    }

    public LanguageHelper helper(Player player) {
        return new LocalePlayer(player, this);
    }
}
