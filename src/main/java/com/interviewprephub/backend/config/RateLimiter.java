package com.interviewprephub.backend.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiter {

    private final int maxRequests;
    private final long windowMillis;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimiter(int maxRequests, long duration, TimeUnit unit) {
        this.maxRequests = maxRequests;
        this.windowMillis = unit.toMillis(duration);
    }

    public boolean isAllowed(String key) {
        long now = System.currentTimeMillis();
        Bucket bucket = buckets.compute(key, (k, existing) -> {
            if (existing == null || now - existing.start >= windowMillis) {
                return new Bucket(now);
            }
            existing.count.incrementAndGet();
            return existing;
        });
        return bucket.count.get() <= maxRequests;
    }

    private static class Bucket {
        final long start;
        final AtomicInteger count;

        Bucket(long start) {
            this.start = start;
            this.count = new AtomicInteger(1);
        }
    }
}
