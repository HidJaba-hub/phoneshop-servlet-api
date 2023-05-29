package com.es.phoneshop.security;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class CustomDosProtectionService implements DosProtectionService {

    private static final long TIME_INTERVAL_MILLS = 60_000;
    private static final long MAX_REQUEST_COUNT = 100;

    private final Map<String, Long> countMap = new ConcurrentHashMap<>();
    private final Map<String, Long> timeMap = new ConcurrentHashMap<>();

    private CustomDosProtectionService() {

    }

    public static CustomDosProtectionService getInstance() {
        return CustomDosProtectionService.SingletonManager.INSTANCE.getSingleton();
    }

    @Override
    public boolean isAllowed(String ip) {
        Long currentTime = System.currentTimeMillis();
        Long lastRequestTime = timeMap.get(ip);
        if (lastRequestTime != null && currentTime - lastRequestTime > TIME_INTERVAL_MILLS) {
            countMap.remove(ip);
            timeMap.remove(ip);
        }
        Long requestCount = countMap.get(ip);
        if (requestCount == null) {
            requestCount = 0L;
        } else if (requestCount > MAX_REQUEST_COUNT) {
            return false;
        }
        countMap.put(ip, requestCount + 1);
        timeMap.put(ip, System.currentTimeMillis());
        return true;
    }

    public Map<String, Long> getCountMap() {
        return countMap;
    }

    public Map<String, Long> getTimeMap() {
        return timeMap;
    }

    public long getMaxRequestCount() {
        return MAX_REQUEST_COUNT;
    }

    private enum SingletonManager {
        INSTANCE;
        private static final CustomDosProtectionService singleton = new CustomDosProtectionService();

        public CustomDosProtectionService getSingleton() {
            return singleton;
        }
    }
}
