package com.es.phoneshop.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class SyncObjectPool {

    private final static Map<Integer, Object> syncObjects = new HashMap<>();
    private static final int MAX_POOL_SIZE = 50;
    private static final ReentrantLock lock = new ReentrantLock();
    private static Integer lastObjectKey = 0;

    public static Object getSyncObject(int idToCheck) {
        if (!lock.isLocked()) {
            if (lock.tryLock()) {
                try {
                    syncObjects.putIfAbsent(idToCheck, new Object());
                    if (lastObjectKey == 0) {
                        lastObjectKey = idToCheck;
                    }
                } finally {
                    if (syncObjects.size() < MAX_POOL_SIZE) lock.unlock();
                    else cleanPool();
                }
            }
        }
        return syncObjects.get(idToCheck);
    }

    private static void cleanPool() {
        syncObjects.remove(lastObjectKey);
        lastObjectKey = 0;
        lock.unlock();
    }

}
