package com.github.mrmks.mc.dev_tools_b.utils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public class ReflectMethod {
    private boolean v = false, f;
    private MethodHandle hd;
    private Method mt;

    public ReflectMethod(Class<?> klass, String name, Class<?>rType, Class<?>... args) {
        try {
            hd = MethodHandles.lookup().findVirtual(klass, name, MethodType.methodType(rType, args));
            v = f = true;
        } catch (Throwable e) {
            try {
                mt = klass.getDeclaredMethod(name, args);
                mt.setAccessible(true);
                v = true;
                f = false;
            } catch (Throwable ignored) {}
        }
    }

    public boolean isValid() {
        return v;
    }

    public Object invoke(Object base, Object... args) throws Throwable {
        Object[] a = new Object[args.length + 1];
        a[0] = base;
        System.arraycopy(args, 0, a, 1, args.length);
        return f ? hd.invokeWithArguments(a) : mt.invoke(base, args);
    }

    public Object tryInvoke(Object base, Object _def, Object... args) {
        try {
            return invoke(base, args);
        } catch (Throwable tr) {
            return _def;
        }
    }
}
