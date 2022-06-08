package com.github.mrmks.mc.marcabone.lang;

import com.github.mrmks.mc.marcabone.Marcabone;
import com.github.mrmks.mc.marcabone.utils.YamlConfigurationLoader;
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

import static com.github.mrmks.mc.marcabone.lang.LanguageFile.EMPTY;

public class LanguageAPI {

    private static LanguageAPI buildIn;
    public static void init(Marcabone plugin) {
        Bukkit.getPluginManager().registerEvents(PlayerLocaleManager.getInstance().generateListener(), plugin);
        buildIn = new LanguageAPI(plugin);
    }
    public static void cleanup() {
        buildIn = null;
        PlayerLocaleManager.getInstance().clear();
    }
    public static LanguageAPI getBuildIn() {
        return buildIn;
    }

    public static final String LOCALE_KEY = "locale";
    public static final String TRANSLATION_KEY = "translation";


    private final HashMap<String, LanguageFile> localeMap = new HashMap<>();
    private LanguageFile defaultLocale = EMPTY;
    private final Plugin plugin;
    private final String[] ext;

    public LanguageAPI(Plugin plugin) {
        this.plugin = plugin;
        this.ext = new String[0];
        load0();
    }

    public LanguageAPI(Plugin plugin, String... ext) {
        this.plugin = plugin;
        this.ext = ext;
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
            loader.saveConfig();
            defaultLocale = new LanguageFile(configuration, LOCALE_KEY, TRANSLATION_KEY);
        }
        for (String e : ext) {
            loader = new YamlConfigurationLoader(plugin, "lang/" + e + ".yml");
            loader.saveDefaultConfig();
            configuration = loader.getConfig();
            if (configuration != null) {
                loader.saveConfig();
                LanguageFile lf = new LanguageFile(configuration, LOCALE_KEY, TRANSLATION_KEY);
                if (localeMap.containsKey(lf.getLocale())) {
                    localeMap.get(lf.getLocale()).merge(lf);
                } else {
                    localeMap.put(lf.getLocale(), lf);
                }
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