package be.rayanhaddou.equipmentrental.security;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimiter {

    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_SECONDS = 60;

    // key -> timestamps (seconds) of attempts
    private final Map<String, Deque<Long>> attempts = new ConcurrentHashMap<>();

    /**
     * Returns true if a login attempt is allowed under the current limit.
     * If allowed, this method records the attempt.
     */
    public boolean tryConsume(String key) {
        long now = Instant.now().getEpochSecond();
        long threshold = now - WINDOW_SECONDS;

        Deque<Long> deque = attempts.computeIfAbsent(key, k -> new ArrayDeque<>());

        synchronized (deque) {
            // remove attempts outside the time window
            while (!deque.isEmpty() && deque.peekFirst() < threshold) {
                deque.pollFirst();
            }

            if (deque.size() >= MAX_ATTEMPTS) {
                return false;
            }

            deque.addLast(now);
            return true;
        }
    }

    /**
     * Optional: call this on successful login to clean up.
     */
    public void reset(String key) {
        attempts.remove(key);
    }
}