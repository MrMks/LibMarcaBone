package com.github.mrmks.mc.marcabone.utils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;

public class ReflectConstructor {
    private boolean valid = false, f;
    private MethodHandle hd;
    private Constructor<?> ct;

    public ReflectConstructor(Class<?> klass, Class<?>... aType) {
        if (klass == null) return;
        try {
            hd = MethodHandles.publicLookup().findConstructor(klass, MethodType.methodType(void.class, aType));
            valid = f = true;
        } catch (Throwable e) {
            try {
                ct = klass.getDeclaredConstructor(aType);
                ct.setAccessible(true);
                valid = true;
                f = false;
            } catch (Throwable ignored) {
            }
        }
    }

    public boolean isValid() {
        return valid;
    }

    public Object invoke(Object... args) throws Throwable {
        return f ? hd.invokeWithArguments(args) : ct.newInstance(args);
    }

    public Object tryInvoke(Object _def, Object... args) {
        try {
            return invoke(args);
        } catch (Throwable tr) {
            return _def;
        }
    }
}
