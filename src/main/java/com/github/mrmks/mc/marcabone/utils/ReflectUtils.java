package com.github.mrmks.mc.marcabone.utils;

import org.bukkit.Bukkit;

public class ReflectUtils {
    private static String nmsPackage;
    private static String obcPackage;
    private static String obcVersion;

    private static boolean isInit = false;
    private static void init() {
        if (!isInit) {
            obcPackage = Bukkit.getServer().getClass().getPackage().getName();
            obcVersion = obcPackage.substring(obcPackage.lastIndexOf('.') + 1);
            nmsPackage = "net.minecraft.server." + obcVersion;
            isInit = true;
        }
    }

    public static String getObcVersion() {
        init();
        return obcVersion;
    }

    public static Class<?> loadNMSClass(String klassName) {
        init();
        return loadClass(nmsPackage, klassName);
    }

    public static Class<?> loadOBCClass(String klassName) {
        init();
        return loadClass(obcPackage, klassName);
    }

    private static Class<?> loadClass(String pkg, String kls) {
        Class<?> ret = null;
        try {ret = Class.forName(pkg + '.' + kls);} catch (Throwable ignored){}
        return ret;
    }

}
