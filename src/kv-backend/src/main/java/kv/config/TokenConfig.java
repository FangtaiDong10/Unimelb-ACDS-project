package kv.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TokenConfig {
    public static final String TOKEN_KEY = "token";
    public static final Map<String, Integer> tokenTimeMap = new HashMap<>();
    public static final Map<String, String> tokenUsernameMap = new HashMap<>();

    @Scheduled(cron = "*/5 * * * * ?")
    private void refreshToken() {
        synchronized (tokenTimeMap) {
            for (String token : tokenTimeMap.keySet()) {
                Integer expiry = tokenTimeMap.get(token);
                if (expiry > 0) {
                    tokenTimeMap.put(token, expiry - 5);
                } else {
                    tokenTimeMap.remove(token);
                    synchronized (tokenUsernameMap) {
                        tokenUsernameMap.remove(token);
                    }
                }
            }
        }
    }
}
