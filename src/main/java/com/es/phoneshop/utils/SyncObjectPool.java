package com.es.phoneshop.utils;

import org.apache.maven.shared.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class SyncObjectPool {

    private final static Map<String, Object> syncObjects = new HashMap<>();
    private static final int MAX_POOL_SIZE = 50;
    private static final ReentrantLock lock = new ReentrantLock();
    private static String lastObjectKey = "";

    public static Object getSyncObject(String idToCheck) {
        if (!lock.isLocked()) {
            if (lock.tryLock()) {
                try {
                    syncObjects.putIfAbsent(idToCheck, new Object());
                    if (StringUtils.isEmpty(lastObjectKey)) {
                        lastObjectKey = idToCheck;
                    }
                } finally {
                    if (syncObjects.size() < MAX_POOL_SIZE) {
                        lock.unlock();
                    } else {
                        cleanPool();
                    }
                }
            }
        }
        return syncObjects.get(idToCheck);
    }

    private static void cleanPool() {
        syncObjects.remove(lastObjectKey);
        lastObjectKey = "";
        lock.unlock();
    }

    public static void cleanPool(String idToRemove) {
        if (lastObjectKey.equals(idToRemove)) {
            lastObjectKey = "";
        }
        syncObjects.remove(idToRemove);
    }

}
