package com.github.mrmks.mc.marcabone.data;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataWriter {
    private final Object queueLock = new Object[0];
    private final ConcurrentLinkedQueue<SmallEntry> queue = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<File, SmallEntry> map = new ConcurrentHashMap<>();
    private final Object listLock = new Object[0];
    private final Queue<SmallEntry> list;
    private final ArrayList<Worker> workers;

    public DataWriter() {
        this(1);
    }

    public DataWriter(int size) {
        this.list = new LinkedList<>();
        this.workers = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Worker worker = new Worker();
            worker.start();
            workers.add(worker);
        }
    }

    public void append(File file, IData data) {
        append(file, data.saveCopy());
    }

    public void append(File file, ICopyData data) {
        synchronized (queueLock) {
            boolean nFLag = queue.isEmpty();
            SmallEntry entry = map.get(file);
            if (entry != null) {
                entry.update(data);
            }
            else {
                entry = new SmallEntry(file, data);
                queue.add(entry);
                map.put(file, entry);
            }
            if (nFLag) queueLock.notify();
        }
    }

    private class Worker extends Thread {
        SmallEntry entry;
        final Object entryLock = new Object[0];
        @Override
        public void run() {
            while (!isInterrupted()) {
                synchronized (listLock) {
                    if (!list.isEmpty()) {
                        synchronized (entryLock) {
                            entry = list.poll();
                        }
                    }
                }
                if (entry == null) {
                    synchronized (queueLock) {
                        if (queue.isEmpty()) {
                            try {
                                queueLock.wait();
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                        if (queue.isEmpty()) continue;
                        synchronized (entryLock) {
                            entry = queue.poll();
                        }
                    }
                }
                // the entry is supposed to be non-null;
                if (entry != null) {
                    boolean pass = false;
                    File file = entry.file;
                    for(Worker worker : workers) {
                        if (worker != this && worker.getCertainEntry().equals(file)) {
                            pass = true;
                            break;
                        }
                    }
                    if (pass) {
                        synchronized (entryLock) {
                            synchronized (listLock) {
                                list.offer(entry);
                            }
                            entry = null;
                        }
                    } else {
                        synchronized (queueLock) {
                            map.remove(entry.file);
                        }
                        entry.data.writeTo(entry.file);
                        synchronized (entryLock) {
                            entry = null;
                        }
                    }
                }
            }
        }

        private File getCertainEntry() {
            synchronized (entryLock) {
                return entry.file;
            }
        }
    }

    private static class SmallEntry {
        private final File file;
        private ICopyData data;

        private SmallEntry(File file, ICopyData data) {
            this.file = file;
            this.data = data;
        }

        private void update(ICopyData data) {
            this.data = data;
        }
    }

}
