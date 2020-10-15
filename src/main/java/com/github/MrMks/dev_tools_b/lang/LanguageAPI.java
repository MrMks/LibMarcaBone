package com.github.MrMks.dev_tools_b.lang;

import com.github.MrMks.dev_tools_b.utils.YamlConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LanguageAPI {
    public static final String LOCALE_KEY = "locale";
    public static final String TRANSLATION_KEY = "translation";
    public static LanguageAPI DEFAULT;

    private static final HashMap<String, LanguageAPI> registered = new HashMap<>();
    public static LanguageAPI load(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin != null && plugin.isEnabled())
            return load(plugin);
        else return null;
    }
    public static LanguageAPI load(Plugin plugin) {
        if (!registered.containsKey(plugin.getName())) {
            registered.put(plugin.getName(), new LanguageAPI(plugin));
        }
        return registered.get(plugin.getName());
    }

    public static void unload(Plugin plugin) {
        unload(plugin.getName());
    }

    public static void unload(String name) {
        registered.remove(name);
    }

    private final HashMap<String, LanguageFile> localeMap = new HashMap<>();
    private String defaultLocale = "";
    private final LanguageFile empty = new LanguageFile("", new HashMap<>());
    private LanguageAPI(Plugin plugin){
        YamlConfigLoader loader = new YamlConfigLoader(plugin, "lang/default.yml");
        if (!loader.exist()) loader.saveDefaultConfig();
        FileConfiguration configuration = loader.getConfig();
        if (configuration != null) {
            defaultLocale = configuration.getString(LOCALE_KEY).toLowerCase();
            HashMap<String, String> transMap = new HashMap<>();
            ConfigurationSection section = configuration.getConfigurationSection(TRANSLATION_KEY);
            Set<String> keys = section.getKeys(true);
            keys.forEach(key-> transMap.put(key, section.getString(key)));
            if (localeMap.containsKey(defaultLocale)) {
                localeMap.put(defaultLocale, new LanguageFile(defaultLocale, transMap));
            }
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
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(path);
            if (cfg.contains(LOCALE_KEY) && cfg.contains(TRANSLATION_KEY)) {
                String locale = cfg.getString(LOCALE_KEY).toLowerCase();
                if (localeMap.containsKey(locale)) {
                    logger.log(Level.WARNING, String.format("Can't read file §c%s§r as the same locale §2%s§r has been registered", path.getName(), locale));
                } else {
                    HashMap<String, String> transMap = new HashMap<>();
                    ConfigurationSection section = cfg.getConfigurationSection(TRANSLATION_KEY);
                    Set<String> keys = section.getKeys(true);
                    keys.forEach(key -> transMap.put(key, section.getString(key)));
                    if (!localeMap.containsKey(locale)) {
                        localeMap.put(locale, new LanguageFile(locale, transMap));
                    } else {
                        localeMap.get(locale).merge(transMap);
                    }
                }
            }
        }
    }

    public boolean hasPlayer(UUID uuid) {
        return PlayerLocaleManager.hasLocale(uuid);
    }

    public boolean hasKey(UUID uuid, String key) {
        return hasPlayer(uuid) && localeMap.getOrDefault(PlayerLocaleManager.getLocale(uuid), empty).has(key);
    }

    public String getTranslation(String locale, String key) {
        return localeMap.getOrDefault(locale, empty).getOrDefault(key, localeMap.getOrDefault(defaultLocale, empty).get(key));
    }

    public String getTranslationWithTag(String locale, String key, Map<String, String> map) {
        return translateWithTag(getTranslation(locale, key), map);
    }

    public String getTranslation(UUID uuid, String key){
        return getTranslation(PlayerLocaleManager.getLocale(uuid), key);
    }

    public String getTranslationWithTag(UUID uuid, String key, Map<String, String> map) {
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

    public LocalePlayer asLocalePlayer(UUID uuid){
        return new LocalePlayer(uuid, this);
    }
}
