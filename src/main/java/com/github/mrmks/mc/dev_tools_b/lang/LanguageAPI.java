package com.github.mrmks.mc.dev_tools_b.lang;

import com.github.mrmks.mc.dev_tools_b.DevToolsB;
import com.github.mrmks.mc.dev_tools_b.utils.YamlConfigurationLoader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.mrmks.mc.dev_tools_b.lang.LanguageFile.EMPTY;

public class LanguageAPI {

    public static void init(DevToolsB plugin) {
        Bukkit.getPluginManager().registerEvents(PlayerLocaleManager.getInstance().generateListener(), plugin);
    }
    public static void cleanup() {
        PlayerLocaleManager.getInstance().clear();
    }

    protected String LOCALE_KEY = "locale";
    protected String TRANSLATION_KEY = "translation";


    private final HashMap<String, LanguageFile> localeMap = new HashMap<>();
    private LanguageFile defaultLocale = EMPTY;
    private final Plugin plugin;

    public LanguageAPI(Plugin plugin) {
        this.plugin = plugin;
        load0();
    }

    public void reload() {
        defaultLocale = EMPTY;
        localeMap.clear();
        load0();
    }

    private void load0() {
        YamlConfigurationLoader loader = new YamlConfigurationLoader(plugin, "lang/default.yml");
        loader.saveDefaultConfig();
        FileConfiguration configuration = loader.getConfig();
        if (configuration != null) {
            defaultLocale = new LanguageFile(configuration, LOCALE_KEY, TRANSLATION_KEY);
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
                        String.format("Reading file §c%s§r with a registered locale §2%s§r", path.getName(), file.getLocale()));
                localeMap.get(file.getLocale()).merge(file);
            } else {
                localeMap.put(file.getLocale(), file);
            }
        }
    }

    @Deprecated
    public boolean hasKey(UUID uuid, String key) {

        PlayerLocaleManager pm = PlayerLocaleManager.getInstance();
        return pm.has(uuid) && localeMap.getOrDefault(pm.get(uuid), EMPTY).has(key);
    }

    @Deprecated
    public String getTranslation(String locale, String key) {
        return localeMap.getOrDefault(locale, EMPTY).getOrDefault(key, localeMap.getOrDefault(defaultLocale.getLocale(), EMPTY).get(key));
    }

    @Deprecated
    public String getTranslation(UUID uuid, String key){
        return getTranslation(PlayerLocaleManager.getInstance().get(uuid), key);
    }

    public LanguageHelper helper(String locale) {
        if (locale == null) return helper();
        LanguageFile file = localeMap.get(locale);
        return file == null ? helper() : new LocaleHelper(file, defaultLocale);
    }

    public LanguageHelper helper(UUID uuid) {
        return helper(PlayerLocaleManager.getInstance().get(uuid));
    }

    public LanguageHelper helper(Player player) {
        if (player == null) return helper();
        return helper(player.getUniqueId());
    }

    public LanguageHelper helper() {
        return new LocaleHelper(defaultLocale);
    }
}
