package com.github.mrmks.mc.dev_tools_b.data;

import java.io.File;

public class DataManager {
    private static DataWriter writer;

    public static void append(File file, IData data) {
        if (writer == null) writer = new DataWriter();
        writer.append(file, data);
    }

    public static void append(File file, ICopyData data) {
        if (writer == null) writer = new DataWriter();
        writer.append(file, data);
    }
}
